package uk.ac.aber.dcs.aberfitness.glados.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;


/**
 * Converts IOExceptions into InternalServer Error responses within the
 * Javax Ws layer
 */
@Provider
public class IoExceptionWrapper implements ExceptionMapper<IOException> {
    @Override
    public Response toResponse(IOException exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
