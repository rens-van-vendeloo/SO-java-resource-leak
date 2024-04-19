package org.acme.rest.client;

import java.net.ConnectException;
import java.util.function.Supplier;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;

public class ClientUtilities {
    public static boolean handleConnectionRefused(Supplier<Response> restClientCall) {
        try (Response response = restClientCall.get()) {
            // Handle the response here
            // This is the common handling logic
            return true;
        } catch (ProcessingException e) {
            if (e.getCause() instanceof ConnectException) {
                // Handle connection refused error
                System.out.println("Connection refused: " + e.getMessage());
                return false;
            } else {
                // Handle other errors
                throw e;
            }
        }
    }
}
