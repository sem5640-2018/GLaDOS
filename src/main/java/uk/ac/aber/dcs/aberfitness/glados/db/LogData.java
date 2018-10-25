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
import java.util.UUID;

/**
 * A class representing a log or audit entry.
 *
 */
public class LogData {
    /**
     * Constructs a new LogData instance which represents a log or audit message
     * @param timestamp The time of the log message
     * @param logLevel The level associated with this log message
     * @param content The message content of this log entry
     * @param userId The user associated with this log entry
     */
    public LogData(final Instant timestamp, final LoggingLevel logLevel,
                   final String content, final String userId) {
        this.logId = UUID.randomUUID().toString();
        this.timestamp = timestamp;
        this.logLevel = logLevel;
        this.content = content;
        this.userId = userId;
    }

    /**
     * Returns the timestamp of the specified log message
     * @return Date object with the log's timestamp
     */
    public Instant getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the logging level of the underlying log message
     * @return LoggingLevel enum with the current level
     */
    public LoggingLevel getLogLevel() {
        return logLevel;
    }


    /**
     * Returns the message associated with the log message
     * @return A string containing the message
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns the unique message ID associated to the log message
     * @return A string containing the user ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Returns this log entries unique ID
     * @return A string containing the unique log ID
     */
    public String getLogId() {
        return logId;
    }

    /**
     * Serialises the current object into a new JSON object
     * @return A json object for the current record
     */
    public JsonObject toJson(){
        JsonObjectBuilder newJson = Json.createObjectBuilder();
        newJson.add("logId", this.logId)
               .add("timestamp", this.timestamp.toString())
               .add("userId", this.userId)
               .add("logLevel", this.logLevel.toString())
               .add("content", this.content);
        return newJson.build();
    }

    public static LogData fromJson(JsonObject jsonObject){
        GsonBuilder gsonBuilder = new GsonBuilder();
        // Register a custom adaptor to convert to instant from strings
        gsonBuilder.registerTypeAdapter(Instant.class, GsonTimeDeserialiser.INSTANT_DESERIALISER);

        Gson gson = gsonBuilder.create();
        return gson.fromJson(jsonObject.toString(), LogData.class);
    }

    public static List<LogData> fromJson(JsonArray jsonArray){
        GsonBuilder gsonBuilder = new GsonBuilder();
        // Register a custom adaptor to convert to instant from strings
        gsonBuilder.registerTypeAdapter(Instant.class, GsonTimeDeserialiser.INSTANT_DESERIALISER);

        Gson gson = gsonBuilder.create();
        LogData[] converted = gson.fromJson(jsonArray.toString(), LogData[].class);
        return Arrays.asList(converted);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }

        if (!LogData.class.isAssignableFrom(obj.getClass())){
            return false;
        }

        final LogData other = (LogData) obj;
        return this.logId.equals(other.logId) && this.timestamp.equals(other.timestamp)
                && this.logLevel == other.logLevel && this.content.equals(other.content) &&
                this.userId.equals(other.userId);

    }

    private String logId;
    private Instant timestamp;
    private LoggingLevel logLevel;
    private String content;
    private String userId;
}
