package UnitTest;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import helpers.AuditDataHelpers;
import org.apache.logging.log4j.core.Core;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.ac.aber.dcs.aberfitness.glados.api.AuditApi;
import uk.ac.aber.dcs.aberfitness.glados.db.*;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class AuditApiTest {
    @Mock
    private DatabaseConnection dbMock;

    // Ensure we replace the injected concrete type with the mock db connection
    @InjectMocks
    private AuditApi apiInstance;

    @Before
    public void setUp() {
        apiInstance = new AuditApi();
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Returns a sample AuditData entry for unit testing
     * @return A sample AuditData object
     */
    private AuditData createExampleLogData(String userId) {
        return new AuditDataNoSerial(Instant.now(), LoggingLevels.DEBUG, "TestContent",
                userId, ServiceNames.GLADOS);
    }

    @Test
    public void findAllCallReturnsAll() throws IOException {
        String userOne = "abc123";
        String userTwo = "bcd234";
        AuditData dummyLogOne = createExampleLogData(userOne);
        AuditData dummyLogTwo = createExampleLogData(userTwo);

        List<AuditData> mockedData = Arrays.asList(dummyLogOne, dummyLogTwo);
        when(dbMock.getAllLogEntries()).thenReturn(mockedData);

        // This should return JSON
        Response returnedBody = apiInstance.getAllEntries(0, 10, "any");
        String returnedJson = (String)returnedBody.getEntity();

        Assert.assertThat(returnedJson, CoreMatchers.containsString(userOne));
        Assert.assertThat(returnedJson, CoreMatchers.containsString(userTwo));
    }

    @Test
    public void getLogById() throws IOException {
        String userId = "test1";
        AuditData newLogData = createExampleLogData(userId);
        String testId = newLogData.getLogId();

        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        when(dbMock.getLogEntry(arg.capture())).thenReturn(newLogData);

        String returnedJson = (String)apiInstance.getAuditById(testId).getEntity();

        // Check arg forwards correctly
        Assert.assertEquals(testId, arg.getValue());

        // Check result
        Assert.assertThat(returnedJson, CoreMatchers.containsString(userId));
    }

    @Test
    public void getLogByIdDoesNotExist() throws IOException {
        String doesNotExist = "bar";

        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        when(dbMock.getLogEntry(arg.capture())).thenReturn(null);

        Response returned = apiInstance.getAuditById(doesNotExist);
        String returnedJson = (String)returned.getEntity();

        // Check result
        Assert.assertEquals(404, returned.getStatus());
    }




}
