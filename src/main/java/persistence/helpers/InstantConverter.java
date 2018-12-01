package persistence.helpers;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Instant;

@Converter
public class InstantConverter implements AttributeConverter<Instant, String> {

    /**
     * Converts the provided instant into a string
     * @param instant The instant to convert
     * @return String representation
     */
    @Override
    public String convertToDatabaseColumn(Instant instant){
        return instant.toString();
    }

    /**
     * Converts the provided string back to an instant
     * @param instantString The string representation
     * @return An instant object
     */
    @Override
    public Instant convertToEntityAttribute(String instantString){
        return Instant.parse(instantString);
    }

}

