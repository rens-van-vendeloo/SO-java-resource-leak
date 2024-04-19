package org.acme;

import org.acme.rest.client.ClientUtilities;
import org.acme.data.SomeDataClass;
import org.acme.rest.client.TestClient;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;

@QuarkusMain
public class Main {
    public static void main(String... args) {
        System.out.println("Quarkus initialising...");
        Quarkus.run(MyApp.class,
        (exitCode, exception) -> {
            // do whatever
        },
        args);
    }
    
    public static class MyApp implements QuarkusApplication {
        @Inject
        SomeDataClass someDataClass;

        @Inject
        @RestClient
        TestClient testClient;
        
        @Override
        public int run(String... args) throws Exception {
            
            System.out.println("DO POST");
            ClientUtilities.handleConnectionRefused(() -> testClient.makePost(someDataClass));
            System.out.println("Rest of program executes..");
            
            Quarkus.waitForExit();
            return 0;
        }

        
    
    }
}