package rest.helpers;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.Instant;

public class InstantSerialiser extends XmlAdapter<String, Instant> {
    @Override
    public Instant unmarshal(String instantString){
        return Instant.parse(instantString);
    }

    @Override
    public String marshal(Instant instant){
        return instant.toString();
    }
}
