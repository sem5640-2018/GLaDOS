package uk.ac.aber.dcs.aberfitness.glados.api;

import com.google.gson.JsonParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.aber.dcs.aberfitness.glados.db.AuditData;
import uk.ac.aber.dcs.aberfitness.glados.db.AuditDataJson;
import uk.ac.aber.dcs.aberfitness.glados.db.DatabaseConnection;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Stateless
@Path("audit")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuditApi {
    // Instant MIN
    private static final String DEFAULT_EMPTY_TIME = "-1000000000-01-01T00:00:00Z";
    private static final Logger log = LogManager.getLogger(AuditApi.class.getName());

    @Inject
    DatabaseConnection dbConnection;

    @GET
    public Response getAuditById(@QueryParam("logId") String logId) throws IOException {
        AuditData foundEntry = dbConnection.getLogEntry(logId);

        if (foundEntry == null){
            log.debug("Could not find logId {}", logId);
            return Response.status(404, "Not Found").build();
        }

        JsonObject json = new AuditDataJson(foundEntry).toJson();
        return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/all")
    public Response getAllEntries(@QueryParam("from") int from,
                                  @QueryParam("to") int to,
                                  @QueryParam("orderBy") String orderBy) throws IOException {
        // TODO limit returned entries
        log.trace("getAllEntries:\n from:{}\n to:{}\n orderBy:{}\n", from, to, orderBy);

        List<AuditData> foundEntries = dbConnection.getAllLogEntries();

        // Ensure we have JSON serialisable elements
        return CreateJsonResponseFromList(foundEntries);
    }

    @GET
    @Path("/find")
    public Response findLogEntry(
            @QueryParam("userId") String userId,
            @DefaultValue(DEFAULT_EMPTY_TIME) @QueryParam("fromTime") String fromTime,
            @DefaultValue(DEFAULT_EMPTY_TIME) @QueryParam("toTime") String toTime) throws IOException {

        log.trace("findLogEntry:\n fromTime:{}\n toTime:{}", fromTime, toTime);

        // We need to take the current time for searching if not provided
        if (toTime.equals(DEFAULT_EMPTY_TIME)){
            toTime = Instant.now().toString();
            log.debug("findLogEntry: Using current time {}", toTime);
        }
        
        List<AuditData> foundEntries = dbConnection.findLogEntry(userId, fromTime, toTime);

        if (foundEntries.isEmpty()){
            return Response.status(404).build();
        }

        return CreateJsonResponseFromList(foundEntries);
    }

    @POST
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postNewEntry(JsonObject httpBody) throws IOException {
        boolean failedToParse = false;
        AuditData newEntity = null;
        try {
            newEntity = AuditDataJson.fromJson(httpBody);
        } catch (JsonParseException e) {
            failedToParse = true;
            log.info("Failed to parse POSTED JSON:\n{}", e.toString());
        }

        if (failedToParse || !newEntity.isValid()){
            return Response.status(400).build();
        }

        dbConnection.addLogData(newEntity);
        return Response.noContent().build();
    }

    private Response CreateJsonResponseFromList(List<AuditData> foundEntries) {
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        foundEntries.forEach(e->jsonArray.add(new AuditDataJson(e).toJson()));
        JsonArray json = jsonArray.build();
        return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();
    }


}
