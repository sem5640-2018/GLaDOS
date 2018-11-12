package uk.ac.aber.dcs.aberfitness.glados.api;

import uk.ac.aber.dcs.aberfitness.glados.db.AuditData;
import uk.ac.aber.dcs.aberfitness.glados.db.AuditDataJson;
import uk.ac.aber.dcs.aberfitness.glados.db.DatabaseConnection;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Stateless
@Path("audit")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuditApi {
    @Inject
    DatabaseConnection dbConnection;

    @GET
    @Path("/all")
    public Response getAllEntries(@QueryParam("from") int from,
                                  @QueryParam("to") int to,
                                  @QueryParam("orderBy") String orderBy) throws IOException {
        // TODO add limits / ranges
        final JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        List<AuditData> foundEntries = dbConnection.getAllLogEntries();

        // Ensure we have JSON serialisable elements
        foundEntries.forEach(e->jsonArray.add(new AuditDataJson(e).toJson()));
        JsonArray json = jsonArray.build();

        return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    public Response getAuditById(@QueryParam("logId") String logId) throws IOException {
        AuditData foundEntry = dbConnection.getLogEntry(logId);

        if (foundEntry == null){
            return Response.status(404, "Not Found").build();
        }

        JsonObject json = new AuditDataJson(foundEntry).toJson();
        return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();
    }


}
