package UnitTest;

import helpers.AuditDataHelpers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.ac.aber.dcs.aberfitness.glados.api.AuditApi;
import uk.ac.aber.dcs.aberfitness.glados.db.*;

import javax.json.JsonArray;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

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
     *
     * @return A sample AuditData object
     */
    private AuditData createExampleLogData() {
        return new AuditData(Instant.now(), "TestContent",
                "abc123", ServiceNames.GLADOS);
    }

    @Test
    public void findAllCallReturnsAll() throws IOException {
        AuditData dummyLogOne = createExampleLogData();
        AuditData dummyLogTwo = createExampleLogData();

        List<AuditData> mockedData = Arrays.asList(dummyLogOne, dummyLogTwo);
        when(dbMock.getAllLogEntries()).thenReturn(mockedData);

        // This should return JSON
        JsonArray returnedJson = apiInstance.getAllEntries();
        List<AuditDataJson> returnedEntries = AuditDataJson.fromJson(returnedJson);

        Assert.assertEquals(returnedEntries.size(), mockedData.size());

        for (int i = 0; i < returnedEntries.size(); i++) {
            AuditDataHelpers.isAlmostEqual(returnedEntries.get(i), mockedData.get(i));
        }
    }


}
