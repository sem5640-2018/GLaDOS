import com.google.gson.JsonParseException;
import helpers.LogDataHelpers;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.aber.dcs.aberfitness.glados.db.*;

import javax.json.*;
import javax.json.stream.JsonParser;
import java.io.StringReader;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class LogDataJsonTest {
    private JsonObject createFakeJson(LogData logData) {

        JsonObjectBuilder newJsonObject = Json.createObjectBuilder();


        newJsonObject.add("timestamp", logData.getTimestamp().toString())
                .add("userId", logData.getUserId())
                .add("logLevel", logData.getLogLevel().toString())
                .add("content", logData.getContent())
                .add("serviceName", logData.getServiceName().toString());
        return newJsonObject.build();
    }

    @Test
    public void ConvertsFromOtherSerialisingTypeCorrectly() {
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

        LogData referenceInstance = new LogDataNoSerial(now, logLevel, sampleMsg, userId, serviceName);

        JsonObject testJson = createFakeJson(referenceInstance);

        LogDataJson returnedClass = LogDataJson.fromJson(testJson);
        LogDataHelpers.isAlmostEqual(referenceInstance, returnedClass);
    }

    @Test
    public void SerialisesFromListCorrectly() {
        Instant now = Instant.now();
        String sampleMsg = "Hello world";
        LoggingLevels logLevel = LoggingLevels.DEBUG;
        String userId = "test123";
        ServiceNames serviceName = ServiceNames.GLADOS;

        final LogDataJson testObjOne = new LogDataJson(now, logLevel, sampleMsg, "101", serviceName);
        final LogDataJson testObjTwo = new LogDataJson(now, logLevel, sampleMsg, "102", serviceName);
        final LogDataJson testObjThree = new LogDataJson(now, logLevel, sampleMsg, "103", serviceName);

        JsonObject testJson = createFakeJson(testObjOne);
        JsonObject testJson2 = createFakeJson(testObjTwo);
        JsonObject testJson3 = createFakeJson(testObjThree);


        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        jsonArrayBuilder.add(testJson).add(testJson2).add(testJson3);
        JsonArray returnedArray = jsonArrayBuilder.build();

        List<LogDataJson> processedArray = LogDataJson.fromJson(returnedArray);

        Assert.assertEquals(processedArray.size(), 3);
        LogDataHelpers.isAlmostEqual(testObjOne, processedArray.get(0));
        LogDataHelpers.isAlmostEqual(testObjTwo, processedArray.get(1));
        LogDataHelpers.isAlmostEqual(testObjThree, processedArray.get(2));
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

        Assert.assertTrue(returnedObject.isValid());
        LogDataHelpers.isAlmostEqual(testInstance, returnedObject);
    }


    @Test
    public void JsonParseExceptionIsThrownForBlank() {
        JsonObject blankObj = Json.createObjectBuilder().build();
        JsonArray blankArray = Json.createArrayBuilder().build();

        assertThrows(JsonParseException.class, () -> {
            LogDataJson.fromJson(blankObj);
        });
        assertThrows(JsonParseException.class, () -> {
            LogDataJson.fromJson(blankArray);
        });

    }

    @Test
    public void JsonParseExceptionIsThrownForPartial() {
        JsonObjectBuilder partialObj = Json.createObjectBuilder();
        partialObj.add("logId", "123");
        partialObj.add("timestamp", Instant.now().toString());

        JsonObject partialJson = partialObj.build();

        assertThrows(JsonParseException.class, () -> {
            LogDataJson.fromJson(partialJson);
        });
    }

    @Test
    public void JsonParseExceptionIsThrownForBadFieldName() {
        final LogDataJson testInstance = new LogDataJson(Instant.now(), LoggingLevels.DEBUG,
                "test", "abc123", ServiceNames.GLADOS);

        JsonObject fakeJson = createFakeJson(testInstance);

        // Do a text replace
        String replacedString = fakeJson.toString().replace("userId", "boom");

        // then re-parse (to satisfy typing)
        JsonParser parser = Json.createParser(new StringReader(replacedString));
        parser.next();
        JsonObject replacedJson = parser.getObject();

        assertThrows(JsonParseException.class, () -> {
            LogDataJson.fromJson(replacedJson);
        });
    }
}
