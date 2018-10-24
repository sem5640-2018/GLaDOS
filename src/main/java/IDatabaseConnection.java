import java.io.IOException;
import java.net.ConnectException;

public interface IDatabaseConnection {
    void connectToDatabase() throws ConnectException;

    void disconnectFromDb() throws ConnectException;

    void addLogData(LogData newLogEntry) throws IOException;

    LogData getLogEntry(String logId) throws IOException;

    LogData findLogEntry(LogData searchCriteria) throws IOException;

    void removeLogData(String logId) throws IOException;
}
