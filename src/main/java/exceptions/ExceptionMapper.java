package exceptions;

import javax.ws.rs.core.Response;

/**
 * Defines a mapping between Java exceptions to response codes
 * @param <E> The exception to map to a response code
 */
public interface ExceptionMapper<E extends Throwable> {
    Response toResponse(E exception);
}
