import org.junit.Assert;
import org.junit.Test;
import uk.ac.aber.dcs.aberfitness.glados.db.LogData;
import uk.ac.aber.dcs.aberfitness.glados.db.LoggingLevel;

import java.util.Date;

public class LogDataTest {
    @Test
    public void LogDataGetters(){
        Date now = new Date();
        String sampleMsg = "Hello world";
        LoggingLevel logLevel = LoggingLevel.DEBUG;
        String userId = "test123";

        final LogData testInstance = new LogData(now, logLevel, sampleMsg, userId);

        Assert.assertEquals(testInstance.getTimestamp(), now);
        Assert.assertEquals(testInstance.getContent(), sampleMsg);
        Assert.assertEquals(testInstance.getLogLevel(), logLevel);
        Assert.assertEquals(testInstance.getUserId(), userId);
    }

    @Test
    public void LogDataGeneratesUniqueLogId(){
        Date now = new Date();

        // Regardless of other fields the log id should always be unique
        final LogData testInstanceOne = new LogData(now, LoggingLevel.DEBUG, "test", "id1");
        final LogData testInstanceTwo = new LogData(now, LoggingLevel.DEBUG, "test", "id1");

        Assert.assertNotEquals(testInstanceOne.getLogId(), testInstanceTwo.getLogId());
    }

}
