import com.google.gson.JsonParseException;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.aber.dcs.aberfitness.glados.db.LogDataNoSerial;
import uk.ac.aber.dcs.aberfitness.glados.db.LogDataJson;
import uk.ac.aber.dcs.aberfitness.glados.db.LoggingLevels;
import uk.ac.aber.dcs.aberfitness.glados.db.ServiceNames;

import javax.json.*;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class LogDataJsonTest {
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
    public void ConvertsFromOtherSerialisingTypeCorrectly(){
        LogDataNoSerial noSerial = new LogDataNoSerial(Instant.now(), LoggingLevels.DEBUG,
                "test", "abc123", ServiceNames.GLADOS);

        LogDataJson convertedInstance = new LogDataJson(noSerial);
        Assert.assertNotNull(convertedInstance);

        // These are equiv if converted correctly
        Assert.assertEquals(noSerial.getLogId(), convertedInstance.getLogId());
    }

    @Test
    public void SerialisesToJsonCorrectly() {
        Instant now = Instant.now();
        String sampleMsg = "Hello world";
        LoggingLevels logLevel = LoggingLevels.DEBUG;
        String userId = "test123";
        ServiceNames serviceName = ServiceNames.GLADOS;

        final LogDataJson testInstance = new LogDataJson(now, logLevel, sampleMsg, userId, serviceName);
        JsonObject returnedJson = testInstance.toJson();

        Assert.assertNotEquals(returnedJson.getString("logId"), "");
        Assert.assertEquals(returnedJson.getString("timestamp"), now.toString());
        Assert.assertEquals(returnedJson.getString("userId"), userId);
        Assert.assertEquals(returnedJson.getString("logLevel"), logLevel.toString());
        Assert.assertEquals(returnedJson.getString("content"), sampleMsg);
        Assert.assertEquals(returnedJson.getString("serviceName"), serviceName.toString());
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

        LogDataJson returnedClass = LogDataJson.fromJson(testJson);
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

        List<LogDataJson> processedArray = LogDataJson.fromJson(returnedArray);

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

        final LogDataJson testInstance = new LogDataJson(now, logLevel, sampleMsg, userId, serviceName);

        JsonObject returnedJson = testInstance.toJson();
        LogDataJson returnedObject = LogDataJson.fromJson(returnedJson);

        Assert.assertEquals(returnedObject, testInstance);
    }


    @Test
    public void JsonParseExceptionIsThrownForBlank() {
        JsonObject blankObj = Json.createObjectBuilder().build();
        JsonArray blankArray = Json.createArrayBuilder().build();

        assertThrows(JsonParseException.class, ()-> { LogDataJson.fromJson(blankObj); });
        assertThrows(JsonParseException.class, ()-> { LogDataJson.fromJson(blankArray); });

    }

    @Test
    public void JsonParseExceptionIsThrownForPartial(){
        JsonObjectBuilder partialObj = Json.createObjectBuilder();
        partialObj.add("logId", "123");
        partialObj.add("timestamp", Instant.now().toString());

        JsonObject partialJson = partialObj.build();

        assertThrows(JsonParseException.class, () -> {LogDataJson.fromJson(partialJson);});
    }

    
}
