package com.turygin.api.server.cofig;


import com.turygin.api.model.ErrorDTO;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Exception handler that catches all the unhandled exceptions in endpoints.
 */
@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {

    private static final Logger LOG = LogManager.getLogger(ExceptionHandler.class);

    /**
     * Generates a generic error response in case of an unhandled exception within an endpoint.
     * @param exception unhandled exception from the endpoint
     * @return a generic error response
     */
    @Override
    public Response toResponse(Exception exception) {
        LOG.error("Default exception handler caught an exception: ", exception);
        ErrorDTO errorResponse = new ErrorDTO(Response.Status.BAD_REQUEST, "Could not fulfill the request.");
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .build();
    }
}
