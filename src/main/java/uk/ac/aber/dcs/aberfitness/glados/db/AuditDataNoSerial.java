package uk.ac.aber.dcs.aberfitness.glados.db;

import java.time.Instant;

/**
 * A class which is the equivalent of a plain struct
 * This can be converted to any other serialisable format
 * by constructing the required format
 */
public class AuditDataNoSerial extends AuditData {
    /**
     * Creates a new AuditDataNoSerial class with the specified data
     * @param timestamp The time of the log message
     * @param logLevel The level associated with this log message
     * @param content The message content of this log entry
     * @param userId The user associated with this log entry
     * @param serviceName The service associated with this log entry
     */
    public AuditDataNoSerial(final Instant timestamp, final LoggingLevels logLevel,
                             final String content, final String userId, final ServiceNames serviceName){
        super(timestamp, logLevel, content, userId, serviceName);
    }

    /**
     * Converts an existing LogDataX class which can serialise into
     * a class which only holds data
     * @param other An instance of AuditData or deriving type to convert from
     */
    public AuditDataNoSerial(AuditData other){
        super(other);
    }
}
