package UnitTest;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.aber.dcs.aberfitness.glados.db.*;
import uk.ac.aber.dcs.aberfitness.glados.db.AuditDataNoSerial;
import uk.ac.aber.dcs.aberfitness.glados.db.AuditData;

import java.time.Instant;

public class AuditDataTest {

    @Test
    public void LogDataGetters() {
        Instant now = Instant.now();
        String sampleMsg = "Hello world";
        LoggingLevels logLevel = LoggingLevels.DEBUG;
        String userId = "test123";
        ServiceNames testName = ServiceNames.GLADOS;

        final AuditData testInstance = new AuditDataNoSerial(now, logLevel, sampleMsg, userId, testName);

        Assert.assertEquals(testInstance.getTimestamp(), now);
        Assert.assertEquals(testInstance.getContent(), sampleMsg);
        Assert.assertEquals(testInstance.getLogLevel(), logLevel);
        Assert.assertEquals(testInstance.getUserId(), userId);
        Assert.assertEquals(testInstance.getServiceName(), testName);
    }

    @Test
    public void LogDataGeneratesUniqueLogId() {
        Instant now = Instant.now();

        // Regardless of other fields the log id should always be unique
        final AuditData testInstanceOne = new AuditDataNoSerial(now, LoggingLevels.DEBUG,
                "test", "id1", ServiceNames.GLADOS);
        final AuditData testInstanceTwo = new AuditDataNoSerial(now, LoggingLevels.DEBUG,
                "test", "id1", ServiceNames.GLADOS);

        Assert.assertNotEquals(testInstanceOne.getLogId(), testInstanceTwo.getLogId());
    }

    @Test
    public void LogDataCopyConstructor(){
        final AuditData testInstanceOne = new AuditDataNoSerial(Instant.now(), LoggingLevels.DEBUG,
                "test", "id1", ServiceNames.GLADOS);

        final AuditData testInstanceTwo = new AuditDataNoSerial(testInstanceOne);

        Assert.assertEquals(testInstanceOne, testInstanceTwo);
    }



}
