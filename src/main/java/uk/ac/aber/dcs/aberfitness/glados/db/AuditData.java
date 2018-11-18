package uk.ac.aber.dcs.aberfitness.glados.db;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * A class representing a log or audit entry.
 */
@Entity(name = "AuditData")
public class AuditData implements Serializable {
    @Id
    @Column(name = "logId", unique = true, nullable = false)
    private String logId;

    @Column(name = "serviceName", nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceNames serviceName;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @Column(name = "content")
    private String content;

    @Column(name = "userId", nullable = false)
    private String userId;

    /**
     * Constructs a new AuditData instance which represents a log or audit message
     *
     * @param timestamp   The time of the log message
     * @param content     The message content of this log entry
     * @param userId      The user associated with this log entry
     * @param serviceName The service associated with this log entry
     */
    public AuditData(final Instant timestamp,
                     final String content, final String userId, final ServiceNames serviceName) {
        this.logId = UUID.randomUUID().toString();
        this.timestamp = timestamp;
        this.content = content;
        this.userId = userId;
        this.serviceName = serviceName;
    }

    /**
     * Implements a copy constructor which is invoked when switching
     * the outer serialisation methods by the extending class
     *
     * @param other The existing AuditData to copy
     */
    public AuditData(AuditData other) {
        this.logId = other.logId;
        this.timestamp = other.timestamp;
        this.content = other.content;
        this.userId = other.userId;
        this.serviceName = other.serviceName;
    }

    /**
     * Returns the timestamp of the specified log message
     *
     * @return Date object with the log's timestamp
     */
    public Instant getTimestamp() {
        return timestamp;
    }


    /**
     * Returns the message associated with the log message
     *
     * @return A string containing the message
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns the unique message ID associated to the log message
     *
     * @return A string containing the user ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Returns this log entries unique ID
     *
     * @return A string containing the unique log ID
     */
    public String getLogId() {
        return logId;
    }

    /**
     * Returns the service which created this log
     *
     * @return ServiceName enum with the micro-service name
     */
    public ServiceNames getServiceName() {
        return serviceName;
    }

    /**
     * Overrides and implements the equality operator for AuditData objects.
     * This is marked final as deriving classes should only serialise
     * not implement operators and is agnostic of the serialising method.
     *
     * @param obj The object to compare to
     * @return If all fields are equal, else false
     */
    @Override
    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!AuditData.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final AuditData other = (AuditData) obj;
        return this.logId.equals(other.logId) && this.timestamp.equals(other.timestamp)
                && this.content.equals(other.content) &&
                this.userId.equals(other.userId) && this.serviceName == other.serviceName;

    }

    @Override
    public final int hashCode() {
        return Objects.hash(logId, timestamp, content, userId, serviceName);
    }

    /**
     * A protected setter for serialisers which use reflection to set fields
     * such as GSON. This allows the caller to ask the underlying struct
     * to set a new UUID which would normally be done in the constructor.
     * This call is package-private.
     */
    void generateLogId() {
        this.logId = UUID.randomUUID().toString();
    }


    /**
     * Returns if all the data fields within AuditData are populated
     * and not null. If any fields are null a false is returned
     *
     * @return True if all fields are populated, else false
     */
    public final boolean isValid() {
        // GSON can return a log with all fields set to null
        Field[] logDataFields = AuditData.class.getDeclaredFields();

        // We use reflection to check all fields of this class are not null
        return Stream.of(logDataFields).allMatch(it -> {
            try {
                return it.get(this) != null;
            } catch (IllegalAccessException e) {
                // Safety net in case reflection fails
                e.printStackTrace();
                return false;
            }
        });
    }


}
