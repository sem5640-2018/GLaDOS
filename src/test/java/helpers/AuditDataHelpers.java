package helpers;

import entities.AuditData;
import org.junit.Assert;

public class AuditDataHelpers {
    /**
     * Compares all fields except UUID which cannot be set through JSON
     */
    static public void isAlmostEqual(AuditData one, AuditData two){
        Assert.assertEquals(one.getUserId(), two.getUserId());
        Assert.assertEquals(one.getContent(), two.getContent());
        Assert.assertEquals(one.getTimestamp(), two.getTimestamp());
    }
}
