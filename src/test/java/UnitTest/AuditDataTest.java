package UnitTest;

import entities.AuditData;
import org.junit.Assert;
import org.junit.Test;
import beans.helpers.ServiceNames;

import java.time.Instant;

public class AuditDataTest {

    @Test
    public void LogDataGetters() {
        Instant now = Instant.now();
        String sampleMsg = "Hello world";
        String userId = "test123";
        String testName = ServiceNames.GLADOS.toString();

        final AuditData testInstance = new AuditData(now.toEpochMilli(), sampleMsg, userId, testName);

        Assert.assertEquals(testInstance.getTimestamp(), now.toEpochMilli());
        Assert.assertEquals(testInstance.getContent(), sampleMsg);
        Assert.assertEquals(testInstance.getUserId(), userId);
        Assert.assertEquals(testInstance.getServiceName(), testName);
    }

    @Test
    public void LogDataGeneratesUniqueLogId() {
        Instant now = Instant.now();

        // Regardless of other fields the log id should always be unique
        final AuditData testInstanceOne = new AuditData(now.toEpochMilli(), "test", "id1", ServiceNames.GLADOS.toString());
        final AuditData testInstanceTwo = new AuditData(now.toEpochMilli(),
                "test", "id1", ServiceNames.GLADOS.toString());

        Assert.assertNotEquals(testInstanceOne.getLogId(), testInstanceTwo.getLogId());
    }

    @Test
    public void LogDataCopyConstructor() {
        final AuditData testInstanceOne = new AuditData(Instant.now().toEpochMilli(),
                "test", "id1", ServiceNames.GLADOS.toString());

        final AuditData testInstanceTwo = new AuditData(testInstanceOne);

        Assert.assertEquals(testInstanceOne, testInstanceTwo);
    }


}
