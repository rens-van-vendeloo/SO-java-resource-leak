package org.acme.rest.client;

import java.net.ConnectException;
import java.util.function.Supplier;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;

public class ClientUtilities {
    public static Response handleConnectionRefused(Supplier<Response> restClientCall) {
        Response response = null;
        try {
            response = restClientCall.get();
        } catch (ProcessingException e) {
            if (e.getCause() instanceof ConnectException) {
                // Handle connection refused error
                System.out.println("Connection refused: " + e.getMessage());
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                // Handle other errors
                throw e;
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return Response.status(Response.Status.OK).build();
    }
}
