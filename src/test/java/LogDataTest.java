import org.junit.Assert;
import org.junit.Test;
import uk.ac.aber.dcs.aberfitness.glados.db.LogData;
import uk.ac.aber.dcs.aberfitness.glados.db.LoggingLevels;
import uk.ac.aber.dcs.aberfitness.glados.db.ServiceNames;

import javax.json.*;
import java.time.Instant;
import java.util.List;

public class LogDataTest {
    @Test
    public void LogDataGetters() {
        Instant now = Instant.now();
        String sampleMsg = "Hello world";
        LoggingLevels logLevel = LoggingLevels.DEBUG;
        String userId = "test123";
        ServiceNames testName = ServiceNames.GLADOS;

        final LogData testInstance = new LogData(now, logLevel, sampleMsg, userId, testName);

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
        final LogData testInstanceOne = new LogData(now, LoggingLevels.DEBUG,
                "test", "id1", ServiceNames.GLADOS);
        final LogData testInstanceTwo = new LogData(now, LoggingLevels.DEBUG,
                "test", "id1", ServiceNames.GLADOS);

        Assert.assertNotEquals(testInstanceOne.getLogId(), testInstanceTwo.getLogId());
    }

    @Test
    public void SerialisesToJsonCorrectly() {
        Instant now = Instant.now();
        String sampleMsg = "Hello world";
        LoggingLevels logLevel = LoggingLevels.DEBUG;
        String userId = "test123";
        ServiceNames serviceName = ServiceNames.GLADOS;

        final LogData testInstance = new LogData(now, logLevel, sampleMsg, userId, serviceName);
        JsonObject returnedJson = testInstance.toJson();

        Assert.assertNotEquals(returnedJson.getString("logId"), "");
        Assert.assertEquals(returnedJson.getString("timestamp"), now.toString());
        Assert.assertEquals(returnedJson.getString("userId"), userId);
        Assert.assertEquals(returnedJson.getString("logLevel"), logLevel.toString());
        Assert.assertEquals(returnedJson.getString("content"), sampleMsg);
        Assert.assertEquals(returnedJson.getString("serviceName"), serviceName.toString());
    }

    private JsonObject createFakeJson(Instant time, String msg, String userId,
                                      String logId, LoggingLevels logLevel, ServiceNames serviceName) {

        JsonObjectBuilder newJsonObject = Json.createObjectBuilder();


        newJsonObject.add("logId", logId)
                .add("timestamp", time.toString())
                .add("userId", userId)
                .add("logLevel", logLevel.toString())
                .add("content", msg)
                .add("serviceName", serviceName.toString());
        return newJsonObject.build();
    }

    @Test
    public void SerialisesFromJsonCorrectly() {
        Instant now = Instant.now();
        String sampleMsg = "Hello world";
        LoggingLevels logLevel = LoggingLevels.DEBUG;
        String userId = "test123";
        String fakeId = "12345";
        ServiceNames serviceName = ServiceNames.GLADOS;

        JsonObject testJson = createFakeJson(now, sampleMsg, userId, fakeId, logLevel, serviceName);

        LogData returnedClass = LogData.fromJson(testJson);
        Assert.assertEquals(returnedClass.getLogId(), fakeId);
        Assert.assertEquals(returnedClass.getUserId(), userId);
        Assert.assertEquals(returnedClass.getLogLevel(), logLevel);
        Assert.assertEquals(returnedClass.getContent(), sampleMsg);
        Assert.assertEquals(returnedClass.getTimestamp(), now);
    }

    @Test
    public void SerialisesFromListCorrectly() {
        Instant now = Instant.now();
        String sampleMsg = "Hello world";
        LoggingLevels logLevel = LoggingLevels.DEBUG;
        String userId = "test123";
        ServiceNames serviceName = ServiceNames.GLADOS;

        JsonObject testJson = createFakeJson(now, sampleMsg, userId, "101", logLevel, serviceName);
        JsonObject testJson2 = createFakeJson(now, sampleMsg, userId, "102", logLevel, serviceName);
        JsonObject testJson3 = createFakeJson(now, sampleMsg, userId, "103", logLevel, serviceName);


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
        LoggingLevels logLevel = LoggingLevels.DEBUG;
        String userId = "test123";
        ServiceNames serviceName = ServiceNames.GLADOS;

        final LogData testInstance = new LogData(now, logLevel, sampleMsg, userId, serviceName);

        JsonObject returnedJson = testInstance.toJson();
        LogData returnedObject = LogData.fromJson(returnedJson);

        Assert.assertEquals(returnedObject, testInstance);

    }


}
