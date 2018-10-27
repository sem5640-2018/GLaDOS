package helpers;

import org.junit.Assert;
import uk.ac.aber.dcs.aberfitness.glados.db.LogData;

public class LogDataHelpers {
    /**
     * Compares all fields except UUID which cannot be set through JSON
     */
    static public void isAlmostEqual(LogData one, LogData two){
        Assert.assertEquals(one.getUserId(), two.getUserId());
        Assert.assertEquals(one.getLogLevel(), two.getLogLevel());
        Assert.assertEquals(one.getContent(), two.getContent());
        Assert.assertEquals(one.getTimestamp(), two.getTimestamp());

        Assert.assertNotEquals(one.getLogId(), two.getLogLevel());
    }
}
