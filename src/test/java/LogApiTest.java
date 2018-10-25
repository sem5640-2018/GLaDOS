import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.ac.aber.dcs.aberfitness.glados.api.LogApi;
import uk.ac.aber.dcs.aberfitness.glados.db.DatabaseConnection;
import uk.ac.aber.dcs.aberfitness.glados.db.LogData;
import uk.ac.aber.dcs.aberfitness.glados.db.LoggingLevel;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class LogApiTest {
    @Mock
    private DatabaseConnection dbMock;

    // Ensure we replace the injected concrete type with
    @InjectMocks
    private LogApi apiInstance;

    @Before
    public void setUp() {
        apiInstance = new LogApi();
        MockitoAnnotations.initMocks(this);
    }



    @Test
    public void findAllCallReturnsAll() throws IOException {
        LogData entryOne = new LogData(Instant.now(), LoggingLevel.DEBUG, "testContent", "abc123");
        LogData entryTwo = new LogData(Instant.now(), LoggingLevel.WARNING, "testContent2", "xyz987");

        List<LogData> mockedData = List.of(entryOne, entryTwo);

        JsonArray returnedData = apiInstance.getAllEntries();



    }

}
