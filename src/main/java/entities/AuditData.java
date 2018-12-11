package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A class representing a log or audit entry.
 */
@Table(name = "AuditData")
@NamedQueries({
@NamedQuery(name="getLogEntry", query="SELECT record FROM AuditData record " +
        "WHERE record.logId = :logId"),
@NamedQuery(name="findLogEntry", query = "SELECT record FROM AuditData record" +
        " WHERE record.userId = :userId" +
        " AND record.timestamp > :minTime" +
        " AND record.timestamp < :maxTime"),
@NamedQuery(name="getAllLogEntries", query="SELECT records FROM AuditData records")
}
)

@Entity(name = "AuditData")
public class AuditData implements Serializable {
    @Id
    @Column(name = "logId", unique = true, nullable = false, length = 36)
    private String logId;

    @Column(name = "serviceName", nullable = false, length = 20)
    private String serviceName;

    @Column(name = "timestamp", nullable = false)
    private String timestamp;

    @Column(name = "content")
    private String content;

    @Column(name = "userId", nullable = false)
    private String userId;

    public AuditData(){}

    /**
     * Constructs a new AuditData instance which represents a log or audit message
     *
     * @param timestamp   The time of the log message
     * @param content     The message content of this log entry
     * @param userId      The user associated with this log entry
     * @param serviceName The service associated with this log entry
     */
    public AuditData(final String timestamp,
                     final String content, final String userId, final String serviceName) {
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
     * @return String object with the log's timestamp
     */
    public String getTimestamp() {
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
     * @return ServiceName string with the micro-service name
     */
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Overrides and implements the equality operator for AuditData Beanobjects.
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
    public void generateLogId() {
        this.logId = UUID.randomUUID().toString();
    }


    /**
     * Returns if all the data fields within AuditData Beanare populated
     * and not null. If any fields are null a false is returned
     *
     * @return True if all fields are populated, else false
     */
    public final boolean isValid() {
        return logId != null &&
               timestamp != null &&
               content != null &&
               userId != null &&
               serviceName != null;
    }


}
