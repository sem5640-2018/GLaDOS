package UnitTest;

import com.google.gson.JsonParseException;
import helpers.AuditDataHelpers;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.aber.dcs.aberfitness.glados.db.*;

import javax.json.*;
import javax.json.stream.JsonParser;
import java.io.StringReader;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AuditDataJsonTest {
    private JsonObject createFakeJson(AuditData auditData) {

        JsonObjectBuilder newJsonObject = Json.createObjectBuilder();


        newJsonObject.add("timestamp", auditData.getTimestamp().toString())
                .add("userId", auditData.getUserId())
                .add("logLevel", auditData.getLogLevel().toString())
                .add("content", auditData.getContent())
                .add("serviceName", auditData.getServiceName().toString());
        return newJsonObject.build();
    }

    @Test
    public void ConvertsFromOtherSerialisingTypeCorrectly() {
        AuditDataNoSerial noSerial = new AuditDataNoSerial(Instant.now(), LoggingLevels.DEBUG,
                "test", "abc123", ServiceNames.GLADOS);

        AuditDataJson convertedInstance = new AuditDataJson(noSerial);
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

        final AuditDataJson testInstance = new AuditDataJson(now, logLevel, sampleMsg, userId, serviceName);
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

        AuditData referenceInstance = new AuditDataNoSerial(now, logLevel, sampleMsg, userId, serviceName);

        JsonObject testJson = createFakeJson(referenceInstance);

        AuditDataJson returnedClass = AuditDataJson.fromJson(testJson);
        AuditDataHelpers.isAlmostEqual(referenceInstance, returnedClass);
    }

    @Test
    public void SerialisesFromListCorrectly() {
        Instant now = Instant.now();
        String sampleMsg = "Hello world";
        LoggingLevels logLevel = LoggingLevels.DEBUG;
        String userId = "test123";
        ServiceNames serviceName = ServiceNames.GLADOS;

        final AuditDataJson testObjOne = new AuditDataJson(now, logLevel, sampleMsg, "101", serviceName);
        final AuditDataJson testObjTwo = new AuditDataJson(now, logLevel, sampleMsg, "102", serviceName);
        final AuditDataJson testObjThree = new AuditDataJson(now, logLevel, sampleMsg, "103", serviceName);

        JsonObject testJson = createFakeJson(testObjOne);
        JsonObject testJson2 = createFakeJson(testObjTwo);
        JsonObject testJson3 = createFakeJson(testObjThree);


        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        jsonArrayBuilder.add(testJson).add(testJson2).add(testJson3);
        JsonArray returnedArray = jsonArrayBuilder.build();

        List<AuditDataJson> processedArray = AuditDataJson.fromJson(returnedArray);

        Assert.assertEquals(processedArray.size(), 3);
        AuditDataHelpers.isAlmostEqual(testObjOne, processedArray.get(0));
        AuditDataHelpers.isAlmostEqual(testObjTwo, processedArray.get(1));
        AuditDataHelpers.isAlmostEqual(testObjThree, processedArray.get(2));
    }

    @Test
    public void SerialisesSelfCorrectly() {
        Instant now = Instant.now();
        String sampleMsg = "Hello world";
        LoggingLevels logLevel = LoggingLevels.DEBUG;
        String userId = "test123";
        ServiceNames serviceName = ServiceNames.GLADOS;

        final AuditDataJson testInstance = new AuditDataJson(now, logLevel, sampleMsg, userId, serviceName);

        JsonObject returnedJson = testInstance.toJson();
        AuditDataJson returnedObject = AuditDataJson.fromJson(returnedJson);

        Assert.assertTrue(returnedObject.isValid());
        AuditDataHelpers.isAlmostEqual(testInstance, returnedObject);
    }


    @Test
    public void JsonParseExceptionIsThrownForBlank() {
        JsonObject blankObj = Json.createObjectBuilder().build();
        JsonArray blankArray = Json.createArrayBuilder().build();

        assertThrows(JsonParseException.class, () -> {
            AuditDataJson.fromJson(blankObj);
        });
        assertThrows(JsonParseException.class, () -> {
            AuditDataJson.fromJson(blankArray);
        });

    }

    @Test
    public void JsonParseExceptionIsThrownForPartial() {
        JsonObjectBuilder partialObj = Json.createObjectBuilder();
        partialObj.add("logId", "123");
        partialObj.add("timestamp", Instant.now().toString());

        JsonObject partialJson = partialObj.build();

        assertThrows(JsonParseException.class, () -> {
            AuditDataJson.fromJson(partialJson);
        });
    }

    @Test
    public void JsonParseExceptionIsThrownForBadFieldName() {
        final AuditDataJson testInstance = new AuditDataJson(Instant.now(), LoggingLevels.DEBUG,
                "test", "abc123", ServiceNames.GLADOS);

        JsonObject fakeJson = createFakeJson(testInstance);

        // Do a text replace
        String replacedString = fakeJson.toString().replace("userId", "boom");

        // then re-parse (to satisfy typing)
        JsonParser parser = Json.createParser(new StringReader(replacedString));
        parser.next();
        JsonObject replacedJson = parser.getObject();

        assertThrows(JsonParseException.class, () -> {
            AuditDataJson.fromJson(replacedJson);
        });
    }
}
