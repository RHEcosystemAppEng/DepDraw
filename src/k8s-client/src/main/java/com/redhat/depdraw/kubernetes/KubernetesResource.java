package com.redhat.depdraw.kubernetes;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.ws.rs.*;
import java.io.*;

import java.io.IOException;
import java.io.InputStream;

@Path("/")
public class KubernetesResource {

    @Inject
    private KubernetesClient client;

    @GET
    @Path("/get-resource")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResources(@QueryParam("name") String name,@QueryParam("kind") String kind,@QueryParam("namespace") String namespace) {

        String[] args = {"oc", "get", kind, name, "-n", namespace, "-o", "json"};

        try {
            String output = executeCommand(args);

            return Response.ok(output).build();
        } catch (IOException | InterruptedException ex) {
            return Response.serverError().entity("Error executing command: " + ex.getMessage()).build();
        }
    }

    @POST
    @Path("/update-resource")
    @Consumes("text/yaml")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateResource(@QueryParam("kind") String kind, @QueryParam("apiVersion") String apiVersion, InputStream requestBody) {
        Resource<GenericKubernetesResource> k8Resource = client.genericKubernetesResources(apiVersion, kind)
                .load(requestBody);

        GenericKubernetesResource resource = client.genericKubernetesResources(apiVersion, kind)
                .createOrReplace(k8Resource.item());

            System.out.println(resource);
            return Response.ok(resource).build();
    }

    @GET
    @Path("/get-status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatus(@QueryParam("name") String name, @QueryParam("kind") String kind, @QueryParam("namespace") String namespace) {

        String[] args = {"oc", "get", kind, name, "-n", namespace, "-o", "json"};

        try {
            String output = executeCommand(args);

            // Use ObjectMapper to parse the JSON string into a JsonNode object
            ObjectMapper objectMapper = new ObjectMapper();
            JsonObject jsonObject = JsonObject.mapFrom(objectMapper.readTree(output));
            // Extract the "status" field from the JsonNode object
            return Response.ok(jsonObject.getJsonObject("status")).build();
        } catch (IOException | InterruptedException ex) {
            return Response.serverError().entity("Error executing command: " + ex.getMessage()).build();
        }
    }

    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    public Response health() {
        return Response.ok().build();
    }

    private String executeCommand(String[] command) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true);

        Process process = builder.start();
        try (InputStream inputStream = process.getInputStream()) {
            String output = readInputStream(inputStream);
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("Command exited with non-zero exit code: " + exitCode);
            }

            return output;
        }
    }

    private String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }

        return sb.toString();
    }
}