package com.redhat.com.depdraw.rest.client;

import com.redhat.depdraw.model.DiagramResource;
import com.redhat.depdraw.model.Line;
import com.redhat.depdraw.model.LineCatalog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;


@Path("/")
@ApplicationScoped
@RegisterRestClient
public interface DataServiceClient {

    @GET
    @Path("/diagrams/{diagramId}/lines")
    List<Line> getLinesByDigramId(@PathParam("diagramId") String diagramId);

    @GET
    @Path("/diagrams/{diagramId}/resources/{diagramResourceId}")
    DiagramResource getDiagramResourceById(@PathParam("diagramId") String diagramId, @PathParam("diagramResourceId") String diagramResourceId);

    @GET
    @Path("/diagrams/{diagramId}/resources/{diagramResourceId}/definition")
    String getDefinition(@PathParam("diagramId") String diagramId, @PathParam("diagramResourceId") String diagramResourceId);

    @POST
    @Path("/diagrams/{diagramId}/resources/{diagramResourceId}/definition")
    Response updateDefinition(@PathParam("diagramId") String diagramId, @PathParam("diagramResourceId") String diagramResourceId, String body);

    @GET
    @Path("/linecatalogs/{lineCatalogId}/")
    LineCatalog getLineCatalogById(@PathParam("lineCatalogId") String lineCatalogId);

    @GET
    @Path("/diagrams/{diagramId}/resources")
    @Produces(MediaType.APPLICATION_JSON)
    List<DiagramResource> getDiagramResources(@PathParam("diagramId") String diagramId);
}
