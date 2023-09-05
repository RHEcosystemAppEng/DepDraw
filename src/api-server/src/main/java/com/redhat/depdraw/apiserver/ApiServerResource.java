package com.redhat.depdraw.apiserver;

import com.redhat.com.depdraw.rest.client.DataServiceClient;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/")
public class ApiServerResource {

    @Inject
    @RestClient
    DataServiceClient dataServiceClient;

    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    public Response health() {
        return Response.ok().build();
    }
}