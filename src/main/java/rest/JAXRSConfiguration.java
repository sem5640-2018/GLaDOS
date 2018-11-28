package rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

// Defines the base endpoint path
@ApplicationPath("/api")
public class JAXRSConfiguration extends Application {
    public JAXRSConfiguration() {}
}
