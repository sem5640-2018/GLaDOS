package entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class AuditDataJson extends AuditData {

    /**
     * Constructs a new AuditDataJson instance which implements a
     * JSON serialiser for the underlying class
     * @param timestamp The time of the log message in millis since Epoch
     * @param content The message content of this log entry
     * @param userId The user associated with this log entry
     * @param serviceName The micro-service associated with this log entry
     */
    public AuditDataJson(final long timestamp,
                         final String content, final String userId, final String serviceName){
        super(timestamp, content, userId, serviceName);
    }

    /**
     * Constructs a new AuditDataJson from a differing serialising implementation
     * to a JSON serialiser
     * @param existingAuditData An existing differing type of serialising JSON
     */
    public AuditDataJson(AuditData existingAuditData){
        super(existingAuditData);
    }


    /**
     * Serialises the current object into a new JSON object
     * @return A json object for the current record
     */
    public JsonObject toJson(){
        JsonObjectBuilder newJson = Json.createObjectBuilder();
        newJson.add("logId", getLogId())
                .add("timestamp", getTimestamp())
                .add("userId", getUserId())
                .add("content", getContent())
                .add("serviceName", getServiceName());
        return newJson.build();
    }

    /**
     * Serialises from a single JSON object into a AuditDataJson
     * object, which implements AuditData
     * @param jsonObject The JSON representing the LogDataObject
     * @return AuditDataJson object for the log entry
     * @throws JsonParseException If all fields are not present and valid
     */
    public static AuditDataJson fromJson(JsonObject jsonObject) throws JsonParseException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        // Register a custom adaptor to convert to instant from strings
        gsonBuilder.registerTypeAdapter(Instant.class, GsonTimeDeserialiser.INSTANT_DESERIALISER);

        Gson gson = gsonBuilder.create();
        AuditDataJson returnedObj = gson.fromJson(jsonObject.toString(), AuditDataJson.class);
        returnedObj.generateLogId();

        if (!returnedObj.isValid()){
            throw new JsonParseException("Partial or empty JSON was received");
        }

        return returnedObj;
    }

    /**
     * Serialises from multiple JSON Objects into a list of AuditDataJson
     * objects, all of which implement AuditData
     * @param jsonArray The array containing JSON arrays
     * @return List of AuditData objects
     * @throws JsonParseException If any AuditData objects are invalid or there are none present
     */
    public static List<AuditDataJson> fromJson(JsonArray jsonArray) throws JsonParseException{
        GsonBuilder gsonBuilder = new GsonBuilder();
        // Register a custom adaptor to convert to instant from strings
        gsonBuilder.registerTypeAdapter(Instant.class, GsonTimeDeserialiser.INSTANT_DESERIALISER);

        Gson gson = gsonBuilder.create();
        AuditDataJson[] converted = gson.fromJson(jsonArray.toString(), AuditDataJson[].class);
        Stream.of(converted).forEach(AuditData::generateLogId);

        boolean allValid = Stream.of(converted).allMatch(AuditData::isValid);

        if (converted.length == 0 || !allValid){
            throw new JsonParseException("Partial, invalid or empty JSON array was received");
        }

        return Arrays.asList(converted);

    }
}
