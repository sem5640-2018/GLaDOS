package entities;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;

public class GsonTimeDeserialiser {
    public static final JsonDeserializer<Instant> INSTANT_DESERIALISER = new JsonDeserializer<Instant>() {

        // Adapted from https://stackoverflow.com/a/36418842
        // Author: zyexal , Posted : 2016/04/05

        @Override
        public Instant deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
            try {

                // if provided as String - '2011-12-03T10:15:30+01:00[Europe/Paris]'
                if(jsonPrimitive.isString()){
                    return Instant.parse(jsonPrimitive.getAsString());
                }

            } catch(RuntimeException e){
                throw new JsonParseException("Unable to parse Instant Timestamp", e);
            }
            throw new JsonParseException("Unable to parse Instant Timestamp");
        }
    };


}
