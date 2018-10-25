import org.junit.Assert;
import org.junit.Test;
import uk.ac.aber.dcs.aberfitness.glados.db.LogData;
import uk.ac.aber.dcs.aberfitness.glados.db.LoggingLevel;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.time.Instant;

public class LogDataTest {
    @Test
    public void LogDataGetters() {
        Instant now = Instant.now();
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
    public void LogDataGeneratesUniqueLogId() {
        Instant now = Instant.now();

        // Regardless of other fields the log id should always be unique
        final LogData testInstanceOne = new LogData(now, LoggingLevel.DEBUG, "test", "id1");
        final LogData testInstanceTwo = new LogData(now, LoggingLevel.DEBUG, "test", "id1");

        Assert.assertNotEquals(testInstanceOne.getLogId(), testInstanceTwo.getLogId());
    }

    @Test
    public void SerialisesToJsonCorrectly() {
        Instant now = Instant.now();
        String sampleMsg = "Hello world";
        LoggingLevel logLevel = LoggingLevel.DEBUG;
        String userId = "test123";

        final LogData testInstance = new LogData(now, logLevel, sampleMsg, userId);
        JsonObject returnedJson = testInstance.toJson();

        Assert.assertNotEquals(returnedJson.getString("logId"), "");
        Assert.assertEquals(returnedJson.getString("timestamp"), now.toString());
        Assert.assertEquals(returnedJson.getString("userId"), userId);
        Assert.assertEquals(returnedJson.getString("logLevel"), logLevel.toString());
        Assert.assertEquals(returnedJson.getString("content"), sampleMsg);
    }

    @Test
    public void SerialisesFromJsonCorrectly() {
        Instant now = Instant.now();
        String sampleMsg = "Hello world";
        LoggingLevel logLevel = LoggingLevel.DEBUG;
        String userId = "test123";
        String fakeId = "12345";

        JsonObjectBuilder newJsonObject = Json.createObjectBuilder();


        newJsonObject.add("logId", fakeId)
                     .add("timestamp", now.toString())
                     .add("userId", userId)
                     .add("logLevel", logLevel.toString())
                     .add("content", sampleMsg);
        JsonObject testJson = newJsonObject.build();

        LogData returnedClass = LogData.fromJson(testJson);
        Assert.assertEquals(returnedClass.getLogId(), fakeId);
        Assert.assertEquals(returnedClass.getUserId(), userId);
        Assert.assertEquals(returnedClass.getLogLevel(), logLevel);
        Assert.assertEquals(returnedClass.getContent(), sampleMsg);
        Assert.assertEquals(returnedClass.getTimestamp(), now);
    }

    @Test
    public void SerialisesSelfCorrectly() {
        Instant now = Instant.now();
        String sampleMsg = "Hello world";
        LoggingLevel logLevel = LoggingLevel.DEBUG;
        String userId = "test123";

        final LogData testInstance = new LogData(now, logLevel, sampleMsg, userId);

        JsonObject returnedJson = testInstance.toJson();
        LogData returnedObject = LogData.fromJson(returnedJson);

        Assert.assertEquals(returnedObject, testInstance);

    }


}
