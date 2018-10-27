package uk.ac.aber.dcs.aberfitness.glados.api;

import uk.ac.aber.dcs.aberfitness.glados.db.DatabaseConnection;

import uk.ac.aber.dcs.aberfitness.glados.db.LogData;
import uk.ac.aber.dcs.aberfitness.glados.db.LogDataJson;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Stateless
@Path("log")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LogApi {
    @Inject
    DatabaseConnection dbConnection;

    @GET
    public JsonArray getAllEntries() throws IOException {
        // TODO add limits / ranges
        final JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        List<LogData> foundEntries = dbConnection.getAllLogEntries();

        // Ensure we have JSON serialisable elements
        foundEntries.forEach(e->jsonArray.add(new LogDataJson(e).toJson()));
        return jsonArray.build();
    }



}
