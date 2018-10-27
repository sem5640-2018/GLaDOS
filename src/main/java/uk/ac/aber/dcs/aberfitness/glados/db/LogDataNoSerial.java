package uk.ac.aber.dcs.aberfitness.glados.db;

import uk.ac.aber.dcs.aberfitness.glados.db.LogData;
import uk.ac.aber.dcs.aberfitness.glados.db.LoggingLevels;
import uk.ac.aber.dcs.aberfitness.glados.db.ServiceNames;

import java.time.Instant;

/**
 * A class which is the equivalent of a plain struct
 * This can be converted to any other serialisable format
 * by constructing the required format
 */
public class LogDataNoSerial extends LogData {
    /**
     * Creates a new LogDataNoSerial class with the specified data
     */
    public LogDataNoSerial(final Instant timestamp, final LoggingLevels logLevel,
                           final String content, final String userId, final ServiceNames serviceName){
        super(timestamp, logLevel, content, userId, serviceName);
    }

    /**
     * Converts an existing LogDataX class which can serialise into
     * a class which only holds data
     */
    public LogDataNoSerial(LogData other){
        super(other);
    }
}
