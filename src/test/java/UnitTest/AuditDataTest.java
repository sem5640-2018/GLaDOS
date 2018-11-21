package UnitTest;

import org.junit.Assert;
import org.junit.Test;
import entities.ServiceNames;
import beans.AuditDataBean;

import java.time.Instant;

public class AuditDataTest {

    @Test
    public void LogDataGetters() {
        Instant now = Instant.now();
        String sampleMsg = "Hello world";
        String userId = "test123";
        ServiceNames testName = ServiceNames.GLADOS;

        final AuditDataBean testInstance = new AuditDataBean(now, sampleMsg, userId, testName);

        Assert.assertEquals(testInstance.getTimestamp(), now);
        Assert.assertEquals(testInstance.getContent(), sampleMsg);
        Assert.assertEquals(testInstance.getUserId(), userId);
        Assert.assertEquals(testInstance.getServiceName(), testName);
    }

    @Test
    public void LogDataGeneratesUniqueLogId() {
        Instant now = Instant.now();

        // Regardless of other fields the log id should always be unique
        final AuditDataBean testInstanceOne = new AuditDataBean(now, "test", "id1", ServiceNames.GLADOS);
        final AuditDataBean testInstanceTwo = new AuditDataBean(now,
                "test", "id1", ServiceNames.GLADOS);

        Assert.assertNotEquals(testInstanceOne.getLogId(), testInstanceTwo.getLogId());
    }

    @Test
    public void LogDataCopyConstructor() {
        final AuditDataBean testInstanceOne = new AuditDataBean(Instant.now(),
                "test", "id1", ServiceNames.GLADOS);

        final AuditDataBean testInstanceTwo = new AuditDataBean(testInstanceOne);

        Assert.assertEquals(testInstanceOne, testInstanceTwo);
    }


}
