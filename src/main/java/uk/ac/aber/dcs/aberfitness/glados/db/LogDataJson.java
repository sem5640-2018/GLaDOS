package uk.ac.aber.dcs.aberfitness.glados.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.ac.aber.dcs.aberfitness.glados.api.GsonTimeDeserialiser;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class LogDataJson extends LogData {

    /**
     * Constructs a new LogDataJson instance which implements a
     * JSON serialiser for the underlying class
     * @param timestamp The time of the log message
     * @param logLevel The level associated with this log message
     * @param content The message content of this log entry
     * @param userId The user associated with this log entry
     */
    public LogDataJson(final Instant timestamp, final LoggingLevels logLevel,
                          final String content, final String userId, final ServiceNames serviceName){
        super(timestamp, logLevel, content, userId, serviceName);
    }

    /**
     * Constructs a new LogDataJson from a differing serialising implementation
     * to a JSON serialiser
     * @param existingLogData An existing differing type of serialising JSON
     */
    public LogDataJson(LogData existingLogData){
        super(existingLogData);
    }


    /**
     * Serialises the current object into a new JSON object
     * @return A json object for the current record
     */
    public JsonObject toJson(){
        JsonObjectBuilder newJson = Json.createObjectBuilder();
        newJson.add("logId", getLogId())
                .add("timestamp", getTimestamp().toString())
                .add("userId", getUserId())
                .add("logLevel", getLogLevel().toString())
                .add("content", getContent())
                .add("serviceName", getServiceName().toString());
        return newJson.build();
    }

    public static LogDataJson fromJson(JsonObject jsonObject){
        GsonBuilder gsonBuilder = new GsonBuilder();
        // Register a custom adaptor to convert to instant from strings
        gsonBuilder.registerTypeAdapter(Instant.class, GsonTimeDeserialiser.INSTANT_DESERIALISER);

        Gson gson = gsonBuilder.create();
        return gson.fromJson(jsonObject.toString(), LogDataJson.class);
    }

    public static List<LogDataJson> fromJson(JsonArray jsonArray){
        GsonBuilder gsonBuilder = new GsonBuilder();
        // Register a custom adaptor to convert to instant from strings
        gsonBuilder.registerTypeAdapter(Instant.class, GsonTimeDeserialiser.INSTANT_DESERIALISER);

        Gson gson = gsonBuilder.create();
        LogDataJson[] converted = gson.fromJson(jsonArray.toString(), LogDataJson[].class);
        return Arrays.asList(converted);
    }
}
