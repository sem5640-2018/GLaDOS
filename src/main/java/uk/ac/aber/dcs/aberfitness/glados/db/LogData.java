package uk.ac.aber.dcs.aberfitness.glados.db;

import java.time.Instant;
import java.util.UUID;

/**
 * A class representing a log or audit entry. This class is marked abstract
 * to force extending classes to implement to X serialisation. For example
 * toJson in the LogDataJson class
 */
public abstract class LogData {
    private ServiceNames serviceName;
    private String logId;
    private Instant timestamp;
    private LoggingLevels logLevel;
    private String content;
    private String userId;

    /**
     * Constructs a new LogData instance which represents a log or audit message
     * @param timestamp The time of the log message
     * @param logLevel The level associated with this log message
     * @param content The message content of this log entry
     * @param userId The user associated with this log entry
     */
    protected LogData(final Instant timestamp, final LoggingLevels logLevel,
                      final String content, final String userId, final ServiceNames serviceName) {
        this.logId = UUID.randomUUID().toString();
        this.timestamp = timestamp;
        this.logLevel = logLevel;
        this.content = content;
        this.userId = userId;
        this.serviceName = serviceName;
    }

    /**
     * Implements a copy constructor which is invoked when switching
     * the outer serialisation methods by the extending class
     * @param other The existing LogData to copy
     */
    protected LogData(LogData other){
        this.logId = other.logId;
        this.timestamp = other.timestamp;
        this.logLevel = other.logLevel;
        this.content = other.content;
        this.userId = other.userId;
        this.serviceName = other.serviceName;
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
     * @return LoggingLevels enum with the current level
     */
    public LoggingLevels getLogLevel() {
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
     * Returns the service which created this log
     * @return ServiceName enum with the micro-service name
     */
    public ServiceNames getServiceName() { return serviceName; }

    @Override
    public final boolean equals(Object obj) {
        if (obj == null){
            return false;
        }

        if (!LogData.class.isAssignableFrom(obj.getClass())){
            return false;
        }

        final LogData other = (LogData) obj;
        return this.logId.equals(other.logId) && this.timestamp.equals(other.timestamp)
                && this.logLevel == other.logLevel && this.content.equals(other.content) &&
                this.userId.equals(other.userId) && this.serviceName == other.serviceName;

    }


}
