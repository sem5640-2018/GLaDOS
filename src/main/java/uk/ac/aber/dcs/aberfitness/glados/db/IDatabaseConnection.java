package uk.ac.aber.dcs.aberfitness.glados.db;

import java.io.IOException;
import java.net.ConnectException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Specifies an interface which handles connections to a NoSql db
 */
public interface IDatabaseConnection {
    /**
     * Attempts to connect to a db. If the connection
     * fails an exception is thrown
     * @throws ConnectException If connection could not be made
     */
    void connectToDatabase() throws ConnectException;

    /**
     * Attempts to disconnect from a db. If the connection
     * does not gracefully close an exception is thrown.
     * If the connection did not already exist an exception is thrown
     * @throws ConnectException If the connection was not closed gracefully
     */
    void disconnectFromDb() throws ConnectException;

    /**
     * Adds a log entry to the db containing the given values
     * If the request fails an exception is thrown
     * @param newLogEntry The log entry to add
     * @throws IOException If the entry could not be added
     */
    void addLogData(AuditData newLogEntry) throws IOException;

    /**
     * Returns the specified log entry from the db if it exists.
     * If the specified entry does not exist a NoSuchElementException is thrown
     * If the request fails for any other reason an IOException is thrown
     * @param logId The unique ID of the log to find
     * @return The log entry from the db
     * @throws NoSuchElementException If the requested ID does not exist
     * @throws IOException If the request could not be completed
     */
    AuditData getLogEntry(String logId) throws IOException, NoSuchElementException;

    /**
     * Returns all matching log entries based on the search criteria passed
     * in. If no elements were found an empty list is instead returned.
     * If the request fails an IOException is thrown
     * @param searchCriteria A list of criteria to search for. Empty fields are ignored
     * @return All matching entries in a list. An empty list if none are found
     * @throws IOException If the request could not be completed
     */
    List<AuditData> findLogEntry(AuditData searchCriteria) throws IOException;

    /**
     * Returns all log entries within the DB as a list.
     * If the request fails an IOException is thrown
     * @return A list containing all log entries from the database
     * @throws IOException If the request could not be completed
     */
    // TODO add limits
    List<AuditData> getAllLogEntries() throws IOException;

    /**
     * Attempts to remove the given logId. The entry is allowed to not exist too.
     * If the request fails an IOException is thrown
     * @param logId The unique ID of the log entry to remove
     * @throws IOException If the request could not be completed
     */
    void removeLogData(String logId) throws IOException;
}
