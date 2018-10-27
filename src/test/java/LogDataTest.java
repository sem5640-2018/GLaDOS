import org.junit.Assert;
import org.junit.Test;
import uk.ac.aber.dcs.aberfitness.glados.db.LogDataNoSerial;
import uk.ac.aber.dcs.aberfitness.glados.db.LogData;
import uk.ac.aber.dcs.aberfitness.glados.db.LoggingLevels;
import uk.ac.aber.dcs.aberfitness.glados.db.ServiceNames;

import java.time.Instant;

public class LogDataTest {
    @Test
    public void LogDataGetters() {
        Instant now = Instant.now();
        String sampleMsg = "Hello world";
        LoggingLevels logLevel = LoggingLevels.DEBUG;
        String userId = "test123";
        ServiceNames testName = ServiceNames.GLADOS;

        final LogData testInstance = new LogDataNoSerial(now, logLevel, sampleMsg, userId, testName);

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
        final LogData testInstanceOne = new LogDataNoSerial(now, LoggingLevels.DEBUG,
                "test", "id1", ServiceNames.GLADOS);
        final LogData testInstanceTwo = new LogDataNoSerial(now, LoggingLevels.DEBUG,
                "test", "id1", ServiceNames.GLADOS);

        Assert.assertNotEquals(testInstanceOne.getLogId(), testInstanceTwo.getLogId());
    }



}
