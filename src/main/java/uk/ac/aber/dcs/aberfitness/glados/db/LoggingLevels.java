package uk.ac.aber.dcs.aberfitness.glados.db;

/**
 * Holds various logging levels available to logging
 * mechanisms. The order of precedence is:
 * Critical -> Error -> Warning -> Audit -> Info -> Debug
 */
public enum LoggingLevels {

    CRITICAL,
    ERROR,
    WARNING,
    AUDIT,
    INFO,
    DEBUG
}
