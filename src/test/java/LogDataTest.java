import org.junit.Assert;
import org.junit.Test;
import uk.ac.aber.dcs.aberfitness.glados.db.LogData;
import uk.ac.aber.dcs.aberfitness.glados.db.LoggingLevel;

import javax.json.*;
import java.time.Instant;
import java.util.List;

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

    private JsonObject createFakeJson(Instant time, String msg, String userId,
                                      String logId, LoggingLevel logLevel){

        JsonObjectBuilder newJsonObject = Json.createObjectBuilder();


        newJsonObject.add("logId", logId)
                .add("timestamp", time.toString())
                .add("userId", userId)
                .add("logLevel", logLevel.toString())
                .add("content", msg);
        return newJsonObject.build();
    }

    @Test
    public void SerialisesFromJsonCorrectly() {
        Instant now = Instant.now();
        String sampleMsg = "Hello world";
        LoggingLevel logLevel = LoggingLevel.DEBUG;
        String userId = "test123";
        String fakeId = "12345";

        JsonObject testJson = createFakeJson(now, sampleMsg, userId, fakeId, logLevel);

        LogData returnedClass = LogData.fromJson(testJson);
        Assert.assertEquals(returnedClass.getLogId(), fakeId);
        Assert.assertEquals(returnedClass.getUserId(), userId);
        Assert.assertEquals(returnedClass.getLogLevel(), logLevel);
        Assert.assertEquals(returnedClass.getContent(), sampleMsg);
        Assert.assertEquals(returnedClass.getTimestamp(), now);
    }

    @Test
    public void SerialisesFromListCorrectly(){
        Instant now = Instant.now();
        String sampleMsg = "Hello world";
        LoggingLevel logLevel = LoggingLevel.DEBUG;
        String userId = "test123";

        JsonObject testJson = createFakeJson(now, sampleMsg, userId, "101", logLevel);
        JsonObject testJson2 = createFakeJson(now, sampleMsg, userId, "102", logLevel);
        JsonObject testJson3 = createFakeJson(now, sampleMsg, userId, "103", logLevel);


        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        jsonArrayBuilder.add(testJson).add(testJson2).add(testJson3);
        JsonArray returnedArray = jsonArrayBuilder.build();

        List<LogData> processedArray = LogData.fromJson(returnedArray);

        Assert.assertEquals(processedArray.size(), 3);
        Assert.assertEquals(processedArray.get(0).getLogId(), "101");
        Assert.assertEquals(processedArray.get(1).getLogId(), "102");
        Assert.assertEquals(processedArray.get(2).getLogId(), "103");
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
