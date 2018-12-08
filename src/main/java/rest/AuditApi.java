package rest;

import entities.AuditData;
import entities.AuditDataJson;

import oauth.gatekeeper.GatekeeperInfo;
import oauth.gatekeeper.UserType;
import persistence.DatabaseConnection;
import rest.helpers.AuthStates;
import rest.helpers.Authorization;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Stateless
@Path("/audit")
public class AuditApi {
    // Instant MIN
    private static final String DEFAULT_EMPTY_TIME = "-1000000000-01-01T00:00:00Z";

    @Context
    private HttpServletRequest request;
    @Context
    private Response response;

    @EJB
    private DatabaseConnection dbConnection;

    @EJB
    private Authorization auth;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuditById(@QueryParam("logId") String logId) throws IOException {
        if (!checkLogin()) {
            return response;
        }

        AuditData foundEntry = dbConnection.getLogEntry(logId);

        if (foundEntry == null) {
            return Response.status(404, "Not Found").build();
        }

        if (!checkRequestedIdPermissions(foundEntry.getUserId())) {
            return Response.status(401, "You do not have permission to view this entry").build();
        }


        JsonObject json = new AuditDataJson(foundEntry).toJson();
        return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();
    }

//    @GET
//    @Path("/all")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getAllEntries(@QueryParam("from") int from,
//                                  @QueryParam("to") int to,
//                                  @QueryParam("orderBy") String orderBy) throws IOException {
//        // TODO limit returned entries
//
//        List<AuditData> foundEntries = dbConnection.getAllLogEntries();
//
//        // Ensure we have JSON serialisable elements
//        return CreateJsonResponseFromList(foundEntries);
//    }

    @GET
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findLogEntry(
            @QueryParam("userId") String userId,
            @DefaultValue(DEFAULT_EMPTY_TIME) @QueryParam("fromTime") String fromTime,
            @DefaultValue(DEFAULT_EMPTY_TIME) @QueryParam("toTime") String toTime) throws IOException {
        if (!checkLogin()) {
            return response;
        }

        if (!checkRequestedIdPermissions(userId)){
            return Response.status(401, "You cannot lookup this user's log entries").build();
        }

        // We need to take the current time for searching if not provided
        if (toTime.equals(DEFAULT_EMPTY_TIME)) {
            toTime = Instant.now().toString();
        }

        Instant startingTime = Instant.parse(fromTime);
        Instant endingTime = Instant.parse(toTime);

        List<AuditData> foundEntries = dbConnection.findLogEntry(userId, startingTime, endingTime);

        if (foundEntries.isEmpty()) {
            return Response.status(404).build();
        }

        return CreateJsonResponseFromList(foundEntries);
    }

    @POST
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postNewEntry(AuditData newEntity) throws IOException {
        if (!checkLogin()) {
            return response;
        }

        if (newEntity == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("A null entry was generated").build();
        }

        if (!auth.getUserInfo().getUserId().isEmpty()){
            // The sub field is populated so this is not a client credential
            return Response.status(401, "Users cannot POST audit logs")
                    .build();
        }

        newEntity.generateLogId();

        if (!newEntity.isValid()) {
            return Response.status(400).entity("JSON contained null fields").build();
        }

        dbConnection.addLogData(newEntity);
        return Response.noContent().build();
    }

    private boolean checkLogin() {
        AuthStates result = auth.verifyAccessToken(request);
        if (result != AuthStates.Authorized) {
            generateErrorMessage(result);
            return false;
        }

        return true;
    }

    /**
     * Checks the requested ID belongs to the current user or
     * an administrator
     *
     * @return True if the request is associated with the user or an admin
     */
    private boolean checkRequestedIdPermissions(String requestedId) {
        GatekeeperInfo userInfo = auth.getUserInfo();
        if (userInfo.getUserId().equals(requestedId)) {
            return true;
        }

        // If not associated they must be an admin
        return userInfo.getUserType() == UserType.administrator;

    }

    /**
     * Generates a HTTP response based on the the auth status returned
     *
     * @param state The auth state to generate an error for
     */
    private void generateErrorMessage(AuthStates state) {
        switch (state) {
            case NoHeader:
                response = Response.status(401, "No Authorization Header").build();
                return;
            case InvalidToken:
                response = Response.status(400, "Authorization Header is incorrect or malformed")
                        .build();
                return;
            case NoToken:
                response = Response.status(400, "The bearer token was missing in the request")
                        .build();
                return;
            case Unauthorized:
                response = Response.status(401, "The token presented was invalid")
                        .build();
                return;
            case ValidToken:
                throw new RuntimeException("A valid token was not verified");
            case Authorized:
                throw new RuntimeException("A authorized token was treated as an error");
            default:
                throw new RuntimeException("Unhandled case in generateErrorMessage");


        }
    }

    private Response CreateJsonResponseFromList(List<AuditData> foundEntries) {
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        foundEntries.forEach(e -> jsonArray.add(new AuditDataJson(e).toJson()));
        JsonArray json = jsonArray.build();
        return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();
    }


}
