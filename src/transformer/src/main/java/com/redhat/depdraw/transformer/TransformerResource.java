package com.redhat.depdraw.transformer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.com.depdraw.rest.client.DataServiceClient;
import com.redhat.depdraw.model.DiagramResource;
import com.redhat.depdraw.model.Line;
import com.redhat.depdraw.model.LineCatalog;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import static com.redhat.depdraw.model.LineCatalog.*;

@Path("/")
public class TransformerResource {

    @Inject
    @RestClient
    DataServiceClient dataServiceClient;

    @Inject
    ObjectMapper objectMapper;

    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    public Response health() {
        return Response.ok().build();
    }

    @POST
    @Path("/transform/{diagramId}/lines")
    @Produces(MediaType.APPLICATION_JSON)
    public Response transformDiagramLines(@PathParam("diagramId") String diagramId) {
        List<Line> lines = dataServiceClient.getLinesByDigramId(diagramId);
        for (Line l : lines) {
            DiagramResource drSource = l.getSource();
            DiagramResource drDestination = l.getDestination();

            LineCatalog lineCatalog = dataServiceClient.getLineCatalogById(l.getLineCatalog().getUuid());
            String srcDefinition = dataServiceClient.getDefinition(diagramId, drSource.getUuid());
            String destinationId = drDestination.getUuid();
            String destDefinition = dataServiceClient.getDefinition(diagramId, destinationId);
            try {
                JsonNode srcJsonNode = objectMapper.readTree(srcDefinition);
                JsonNode destJsonNode = objectMapper.readTree(destDefinition);
                JsonNode srcMetadataJsonNode = srcJsonNode.path("metadata");
                JsonNode destMetadataJsonNode = destJsonNode.path("metadata");

                //TODO validate the updateDefinition call was successful
                for (String rule : lineCatalog.getRules()) {
                    switch (rule) {
                        case INHERIT_LABELS:
                            updateLabels(srcMetadataJsonNode, destMetadataJsonNode);
                            dataServiceClient.updateDefinition(diagramId, destinationId, destJsonNode.toString());
                            break;
                        case INHERIT_ANNOTATIONS:
                            updateAnnotations(srcMetadataJsonNode, destMetadataJsonNode);
                            dataServiceClient.updateDefinition(diagramId, destinationId, destJsonNode.toString());
                            break;
                        case INHERIT_METADATA:
                            updateLabels(srcMetadataJsonNode, destMetadataJsonNode);
                            updateAnnotations(srcMetadataJsonNode, destMetadataJsonNode);
                            dataServiceClient.updateDefinition(diagramId, destinationId, destJsonNode.toString());
                            break;
                        case SELECT_RESOURCE:
                            //TODO Copy src matchLabels content to dest metadata labels
                            updateSelect(srcJsonNode.path("spec").path("selector").path("matchLabels"), destMetadataJsonNode);
                            dataServiceClient.updateDefinition(diagramId, destinationId, destJsonNode.toString());
                            break;
                    }
                }
            } catch (JsonProcessingException e) {
                //TODO handle error in the correct way
                throw new RuntimeException(e);
            }
        }

        return Response.ok().build();
    }

    private void updateLabels(JsonNode srcMetadataJsonNode, JsonNode destMetadataJsonNode) {
        JsonNode srcLabelsJsonNode = srcMetadataJsonNode.get("labels");
        JsonNode destLabelsJsonNode = destMetadataJsonNode.get("labels");
        if(srcLabelsJsonNode != null){
            if(destLabelsJsonNode != null){
                ((ObjectNode) destLabelsJsonNode).setAll((ObjectNode) srcLabelsJsonNode);
            } else {
                ((ObjectNode) destMetadataJsonNode).set("labels", srcLabelsJsonNode);
            }
        }
    }

    private void updateAnnotations(JsonNode srcMetadataJsonNode, JsonNode destMetadataJsonNode) {
        JsonNode srcaAnotationsJsonNode = srcMetadataJsonNode.get("annotations");
        JsonNode destAnnotationsJsonNode = destMetadataJsonNode.get("annotations");
        if(srcaAnotationsJsonNode != null){
            if(destAnnotationsJsonNode != null){
                ((ObjectNode) destAnnotationsJsonNode).setAll((ObjectNode)srcaAnotationsJsonNode);
            } else {
                ((ObjectNode) destMetadataJsonNode).set("annotations", srcaAnotationsJsonNode);
            }
        }
    }

    private void updateSelect(JsonNode srcMatchLabelsJsonNode, JsonNode destMetadataJsonNode) {
        JsonNode destLabelsJsonNode = destMetadataJsonNode.get("labels");
        if(srcMatchLabelsJsonNode != null){
            if(destLabelsJsonNode != null){
                ((ArrayNode) destLabelsJsonNode).addAll((ArrayNode) srcMatchLabelsJsonNode);
            } else {
                ((ObjectNode) destMetadataJsonNode).set("labels", srcMatchLabelsJsonNode);
            }
        }
    }
}