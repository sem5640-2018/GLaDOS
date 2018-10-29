package uk.ac.aber.dcs.aberfitness.glados.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import uk.ac.aber.dcs.aberfitness.glados.api.GsonTimeDeserialiser;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class LogDataJson extends LogData {

    /**
     * Constructs a new LogDataJson instance which implements a
     * JSON serialiser for the underlying class
     * @param timestamp The time of the log message
     * @param logLevel The level associated with this log message
     * @param content The message content of this log entry
     * @param userId The user associated with this log entry
     * @param serviceName The micro-service associated with this log entry
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

    /**
     * Serialises from a single JSON object into a LogDataJson
     * object, which implements LogData
     * @param jsonObject The JSON representing the LogDataObject
     * @return LogDataJson object for the log entry
     * @throws JsonParseException If all fields are not present and valid
     */
    public static LogDataJson fromJson(JsonObject jsonObject) throws JsonParseException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        // Register a custom adaptor to convert to instant from strings
        gsonBuilder.registerTypeAdapter(Instant.class, GsonTimeDeserialiser.INSTANT_DESERIALISER);

        Gson gson = gsonBuilder.create();
        LogDataJson returnedObj = gson.fromJson(jsonObject.toString(), LogDataJson.class);
        returnedObj.generateLogId();

        if (!returnedObj.isValid()){
            throw new JsonParseException("Partial or empty JSON was received");
        }

        return returnedObj;
    }

    /**
     * Serialises from multiple JSON Objects into a list of LogDataJson
     * objects, all of which implement LogData
     * @param jsonArray The array containing JSON arrays
     * @return List of LogData objects
     * @throws JsonParseException If any LogData objects are invalid or there are none present
     */
    public static List<LogDataJson> fromJson(JsonArray jsonArray) throws JsonParseException{
        GsonBuilder gsonBuilder = new GsonBuilder();
        // Register a custom adaptor to convert to instant from strings
        gsonBuilder.registerTypeAdapter(Instant.class, GsonTimeDeserialiser.INSTANT_DESERIALISER);

        Gson gson = gsonBuilder.create();
        LogDataJson[] converted = gson.fromJson(jsonArray.toString(), LogDataJson[].class);
        Stream.of(converted).forEach(LogData::generateLogId);

        boolean allValid = Stream.of(converted).allMatch(LogData::isValid);

        if (converted.length == 0 || !allValid){
            throw new JsonParseException("Partial, invalid or empty JSON array was received");
        }

        return Arrays.asList(converted);

    }
}
