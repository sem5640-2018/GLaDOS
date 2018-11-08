package helpers;

import org.junit.Assert;
import uk.ac.aber.dcs.aberfitness.glados.db.AuditData;

public class AuditDataHelpers {
    /**
     * Compares all fields except UUID which cannot be set through JSON
     */
    static public void isAlmostEqual(AuditData one, AuditData two){
        Assert.assertEquals(one.getUserId(), two.getUserId());
        Assert.assertEquals(one.getLogLevel(), two.getLogLevel());
        Assert.assertEquals(one.getContent(), two.getContent());
        Assert.assertEquals(one.getTimestamp(), two.getTimestamp());

        Assert.assertNotEquals(one.getLogId(), two.getLogLevel());
    }
}
