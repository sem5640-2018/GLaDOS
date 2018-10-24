package uk.ac.aber.dcs.aberfitness.glados.db;

import java.util.Date;
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
    public LogData(final Date timestamp, final LoggingLevel logLevel,
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
    public Date getTimestamp() {
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

    private String logId;
    private Date timestamp;
    private LoggingLevel logLevel;
    private String content;
    private String userId;
}
