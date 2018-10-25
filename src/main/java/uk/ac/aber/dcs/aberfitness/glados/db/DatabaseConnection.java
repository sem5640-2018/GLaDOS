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
    public void addLogData(LogData newLogEntry) throws IOException {

    }

    @Override
    public LogData getLogEntry(String logId) throws IOException, NoSuchElementException {
        return null;
    }

    @Override
    public List<LogData> findLogEntry(LogData searchCriteria) throws IOException {
        return null;
    }

    @Override
    public List<LogData> getAllLogEntries() throws IOException {
        return null;
    }

    @Override
    public void removeLogData(String logId) throws IOException {

    }
}
