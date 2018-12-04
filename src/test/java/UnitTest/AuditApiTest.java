package UnitTest;

import entities.AuditData;
import entities.AuditDataJson;
import rest.AuditApi;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import persistence.DatabaseConnection;
import rest.helpers.ServiceNames;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class AuditApiTest {
    @Mock
    private DatabaseConnection dbMock;

    // Ensure we replace the injected concrete type with the mock db connection
    @InjectMocks
    private AuditApi apiInstance;

    private final String EMPTY_TIME = Instant.MIN.toString();

    @Before
    public void setUp() {
        apiInstance = new AuditApi();
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Returns a sample AuditData entry for unit testing
     *
     * @return A sample AuditData object
     */
    private AuditData createExampleLogData(String userId) {
        return new AuditData(Instant.now(), "TestContent",
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


        when(dbMock.getLogEntry(any())).thenReturn(null);

        Response returned = apiInstance.getAuditById(doesNotExist);

        // Check result
        Assert.assertEquals(404, returned.getStatus());
    }

    @Test
    public void findLogByUserName() throws IOException{
        String userId = "test2";
        AuditData logData = createExampleLogData(userId);
        List<AuditData> logDataList = new ArrayList<>();
        logDataList.add(logData);

        ArgumentCaptor<String> userArg = ArgumentCaptor.forClass(String.class);


        when(dbMock.findLogEntry(userArg.capture(), any(), any())).thenReturn(logDataList);

        Response returned = apiInstance.findLogEntry(userId, EMPTY_TIME, EMPTY_TIME);
        String json = (String) returned.getEntity();

        Assert.assertEquals(userId, userArg.getValue());
        Assert.assertEquals(200, returned.getStatus());
        Assert.assertThat(json, CoreMatchers.containsString(userId));
    }

    @Test
    public void findLogHasNothing() throws IOException{
        List<AuditData> logDataList = new ArrayList<>();

        when(dbMock.findLogEntry(any(), any(), any())).thenReturn(logDataList);

        Response returned = apiInstance.findLogEntry("notThere", EMPTY_TIME, EMPTY_TIME);

        Assert.assertEquals(404, returned.getStatus());
    }

    @Test
    public void findLogOptionalToTime() throws IOException {
        // Checks the optional to time is updated to the current time
        String userId = "abc123";
        AuditData logData = createExampleLogData(userId);
        List<AuditData> logDataList = new ArrayList<>();
        logDataList.add(logData);

        ArgumentCaptor<Instant> fromTime = ArgumentCaptor.forClass(Instant.class);
        ArgumentCaptor<Instant> toTime = ArgumentCaptor.forClass(Instant.class);

        when(dbMock.findLogEntry(any(), fromTime.capture(), toTime.capture())).thenReturn(logDataList);

        apiInstance.findLogEntry(userId, EMPTY_TIME, EMPTY_TIME);
        Assert.assertEquals(fromTime.getValue().toString(), EMPTY_TIME);
        Assert.assertNotEquals(toTime.getValue().toString(), EMPTY_TIME);

    }

/*
    @Test
    public void postNewEntry() throws IOException {
        String userId = "newId1";
        AuditData logData = createExampleLogData(userId);
        AuditDataJson serialiser = new AuditDataJson(logData);

        Response response = apiInstance.postNewEntry(serialiser.toJson());
        verify(dbMock, times(1)).addLogData(notNull());

        Assert.assertEquals(204, response.getStatus());
    }

    @Test
    public void postNewEntryWithBadInput() throws IOException {
        JsonObject badHtml = Json.createObjectBuilder().add("foo", 10).build();

        Response response = apiInstance.postNewEntry(badHtml);
        verify(dbMock, times(0)).addLogData(any());

        Assert.assertEquals(400, response.getStatus());
    }

*/

}
