package uk.ac.aber.dcs.aberfitness.glados.db;

import java.io.IOException;
import java.net.ConnectException;
import java.util.List;
import java.util.NoSuchElementException;

public class DatabaseConnection implements IDatabaseConnection{
    // TODO
    public DatabaseConnection(){
    }

    @Override
    public void connectToDatabase() throws ConnectException {

    }

    @Override
    public void disconnectFromDb() throws ConnectException {

    }

    @Override
    public void addLogData(AuditData newLogEntry) throws IOException {

    }

    @Override
    public AuditData getLogEntry(String logId) throws IOException, NoSuchElementException {
        return null;
    }

    @Override
    public List<AuditData> findLogEntry(String userId, String fromTime, String toTime) throws IOException {
        return null;
    }

    @Override
    public List<AuditData> getAllLogEntries() throws IOException {
        return null;
    }

    @Override
    public void removeLogData(String logId) throws IOException {

    }
}
