package uk.ac.aber.dcs.aberfitness.glados.api;

import com.google.gson.JsonParseException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;


/**
 * Converts IOExceptions into InternalServer Error responses within the
 * Javax Ws layer
 */
@Provider
public class JsonParseExceptionWrapper implements ExceptionMapper<JsonParseException> {
    @Override
    public Response toResponse(JsonParseException exception) {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
