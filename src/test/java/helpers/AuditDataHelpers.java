package helpers;

import org.junit.Assert;
import beans.AuditDataBean;

public class AuditDataHelpers {
    /**
     * Compares all fields except UUID which cannot be set through JSON
     */
    static public void isAlmostEqual(AuditDataBean one, AuditDataBean two){
        Assert.assertEquals(one.getUserId(), two.getUserId());
        Assert.assertEquals(one.getContent(), two.getContent());
        Assert.assertEquals(one.getTimestamp(), two.getTimestamp());
    }
}
