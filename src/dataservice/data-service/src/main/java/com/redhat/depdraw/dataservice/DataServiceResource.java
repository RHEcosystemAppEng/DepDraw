package com.redhat.depdraw.dataservice;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.redhat.depdraw.model.Diagram;
import com.redhat.depdraw.model.DiagramResource;
import com.redhat.depdraw.model.Line;
import com.redhat.depdraw.model.LineCatalog;
import com.redhat.depdraw.model.ResourceCatalog;
import com.redhat.depdraw.dataservice.service.DiagramResourceService;
import com.redhat.depdraw.dataservice.service.DiagramService;
import com.redhat.depdraw.dataservice.service.K8SResourceSchemaService;
import com.redhat.depdraw.dataservice.service.LineCatalogService;
import com.redhat.depdraw.dataservice.service.LineService;
import com.redhat.depdraw.dataservice.service.ResourceCatalogService;


@Path("/")
public class DataServiceResource {

    @Inject
    DiagramService diagramService;

    @Inject
    LineService lineService;

    @Inject
    LineCatalogService lineCatalogService;

    @Inject
    DiagramResourceService diagramResourceService;

    @Inject
    ResourceCatalogService resourceCatalogService;

    @Inject
    K8SResourceSchemaService k8sResourceSchemaService;


    @POST
    @Path("/diagrams")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createDiagram(Diagram diagram) {
        final Diagram createdDiagram = diagramService.createDiagram(diagram);

        return Response.ok(createdDiagram).build();
    }

    @GET
    @Path("/diagrams/{diagramId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiagramById(@PathParam("diagramId") String diagramId) {
          final Diagram diagram = diagramService.getDiagramById(diagramId);

        return Response.ok(diagram).build();
    }

    @DELETE
    @Path("/diagrams/{diagramId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDiagramById(@PathParam("diagramId") String diagramId) {
        diagramService.deleteDiagramById(diagramId);

        return Response.ok().build();
    }

    @GET
    @Path("/diagrams/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiagrams() {
        List<Diagram> diagrams = diagramService.getDiagrams();

        return Response.ok(diagrams).build();
    }

    @POST
    @Path("/diagrams/{diagramId}/lines")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createLine(@PathParam("diagramId") String diagramId, Line line) {
        final Line createdLine = lineService.createLine(diagramId, line);

        return Response.ok(createdLine).build();
    }

    @DELETE
    @Path("/diagrams/{diagramId}/lines/{lineId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteLineById(@PathParam("diagramId") String diagramId, @PathParam("lineId") String lineId) {
        lineService.deleteLineById(diagramId, lineId);

        return Response.ok().build();
    }

    @GET
    @Path("/diagrams/{diagramId}/lines/{lineId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLineById(@PathParam("diagramId") String diagramId, @PathParam("lineId") String lineId) {
        final Line line = lineService.getLineById(diagramId, lineId);

        return Response.ok(line).build();
    }

    @GET
    @Path("/diagrams/{diagramId}/lines")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLines(@PathParam("diagramId") String diagramId) {
        List<Line> lines = lineService.getLines(diagramId);

        return Response.ok(lines).build();
    }

    @GET
    @Path("/linecatalogs/{lineCatalogId}/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLineCatalogById(@PathParam("lineCatalogId") String lineCatalogId) {
        final LineCatalog lineCatalog = lineCatalogService.getLineCatalogById(lineCatalogId);

        return Response.ok(lineCatalog).build();
    }

    @GET
    @Path("/linecatalogs/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLineCatalogs() {
        final List<LineCatalog> lineCatalogs = lineCatalogService.getLineCatalogs();

        return Response.ok(lineCatalogs).build();
    }

    @POST
    @Path("/diagrams/{diagramId}/resources")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createDiagramResource(@PathParam("diagramId") String diagramId, DiagramResource diagramResource) {
        final DiagramResource createdDiagramResource = diagramResourceService.createDiagramResource(diagramId, diagramResource);

        return Response.ok(createdDiagramResource).build();
    }

    @GET
    @Path("/diagrams/{diagramId}/resources/{diagramResourceId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiagramResourceById(@PathParam("diagramId") String diagramId, @PathParam("diagramResourceId") String diagramResourceId) {
        final DiagramResource diagramResource = diagramResourceService.getDiagramResourceById(diagramId, diagramResourceId);

        return Response.ok(diagramResource).build();
    }

    @DELETE
    @Path("/diagrams/{diagramId}/resources/{diagramResourceId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDiagramResourceById(@PathParam("diagramId") String diagramId, @PathParam("diagramResourceId") String diagramResourceId) {
        diagramResourceService.deleteDiagramResourceById(diagramId, diagramResourceId);

        return Response.ok().build();
    }

    @GET
    @Path("/diagrams/{diagramId}/resources/{diagramResourceId}/schema")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiagramResourceSchemaById(@PathParam("diagramId") String diagramId,
            @PathParam("diagramResourceId") String diagramResourceId) {
        final DiagramResource diagramResource = diagramResourceService.getDiagramResourceById(diagramId,
                diagramResourceId);
        final ResourceCatalog resourceCatalog = resourceCatalogService
                .getResourceCatalogById(diagramResource.getResourceCatalog().getUuid());
        final String k8sResourceSchema = k8sResourceSchemaService
                .getK8sResourceSchemaById(resourceCatalog.getK8sResourceSchemaRef());

        return Response.ok(k8sResourceSchema).build();
    }

    @POST
    @Path("/diagrams/{diagramId}/resources/{diagramResourceId}/definition")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDefinition(@PathParam("diagramId") String diagramId,
            @PathParam("diagramResourceId") String diagramResourceId, String body) {
                diagramResourceService.updateDiagramResourceDefinition(diagramId, diagramResourceId, body);
        return Response.ok().build();
    }

    @GET
    @Path("/diagrams/{diagramId}/resources/{diagramResourceId}/definition")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDefinition(@PathParam("diagramId") String diagramId,
            @PathParam("diagramResourceId") String diagramResourceId) {
        return Response.ok(diagramResourceService.getDiagramResourceDefinition(diagramId, diagramResourceId)).build();
    }

    @GET
    @Path("/diagrams/{diagramId}/resources")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiagramResources(@PathParam("diagramId") String diagramId) {
        List<DiagramResource> diagramsResources = diagramResourceService.getDiagramResources(diagramId);

        return Response.ok(diagramsResources).build();
    }

    @GET
    @Path("/resourcecatalogs/{resourceCatalogId}/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResourceCatalogById(@PathParam("resourceCatalogId") String resourceCatalogId) {
        final ResourceCatalog resourceCatalog = resourceCatalogService.getResourceCatalogById(resourceCatalogId);

        return Response.ok(resourceCatalog).build();
    }

    @GET
    @Path("/k8sresourceschemas/{k8sResourceschemaId}/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getK8sResourceschemaById(@PathParam("k8sResourceschemaId") String k8sResourceschemaId) {
        final String k8sResourceSchema = k8sResourceSchemaService.getK8sResourceSchemaById(k8sResourceschemaId);

        return Response.ok(k8sResourceSchema).build();
    }

    @GET
    @Path("/health")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response health() {
        return Response.ok().build();
    }
}