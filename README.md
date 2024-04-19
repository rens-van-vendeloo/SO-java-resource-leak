This repository is part of the following [Stack Overflow question](https://stackoverflow.com/questions/78353106/why-does-my-lambda-function-cause-a-resource-leak-in-java-using-quarkus):

I have a Quarkus microservice (let's call it A) which makes a POST request to another microservice (let's call it B) using the RestClient. If A makes a request to B, but B is down (i.e. not started yet), then microservice A is automatically stopped by Quarkus due to an unhandled exception, which is normal behaviour. To fix this without having to wrap every call in a try-catch block I have tried the following: 

I tried using the [@ClientExceptionMapper](https://quarkus.io/guides/rest-client#using-clientexceptionmapper) but that did not work for this exception. In my understanding because thist ClientExceptionMapper handles RuntimeExceptions, while the exception I am encountering is a ProcessException as seen in the debugger output:

`2024-04-19 11:49:21,649 ERROR [io.qua.run.Application] (Quarkus Main Thread) Failed to start application (with profile [dev]): jakarta.ws.rs.ProcessingException: io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: no further information: localhost/127.0.0.1:8081`

So to handle this exception without having to write a try-catch block around every HTTP request in my microservice (which is honestly not often, but I want to learn something new), I decided to wrap the function call in a utility method using lambda expressions.

So instead of: `testClient.makePost(someDataClass);`

it becomes:  `ClientUtilities.handleConnectionRefused(() -> testClient.makePost(someDataClass));`

Where the ClientUtilities is defined as follows:

```
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
```

**This fixes the application shutdown, because the exception is now handled. But it generates a warning inside my IDE (which is VS Code).**

The warning is `Resource leak: '<unassigned Closeable value>' is never closed` given at this line `ClientUtilities.handleConnectionRefused(() -> testClient.makePost(someDataClass));`. Specifically the part of `testClient.makePost(someDataClass)`

**As I am still new to Java (Computer Science Student) and willing to learn and explore programming concepts, the chance that I made a mistake is high. I tried to do some research on this topic, but did not become any wiser.**

I have uploaded a sample project at [Github](https://github.com/rens-van-vendeloo/SO-java-resource-leak) which demonstrates this problem.

**I wish to know why this warning arises and if possible how to correctly implement my solution so it does not generate a warning and thus becomes less prone to a resource leak.**
