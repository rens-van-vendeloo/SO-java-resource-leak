package org.acme.rest.client;

import org.acme.data.SomeDataClass;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/endpoint")
@RegisterRestClient
public interface TestClient {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    Response makePost(SomeDataClass someData);
}
