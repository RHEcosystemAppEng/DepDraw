package com.redhat.depdraw.k8sclient;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.ws.rs.*;
import java.io.*;
import io.fabric8.kubernetes.client.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;


@Path("/")
public class K8sClient {
    @Inject
    KubernetesClient kubernetesClient;

    @GET
    @Path("/get-resource")
    public Response GetResources(@QueryParam("args") String args,@QueryParam("name") String name,@QueryParam("kind") String kind,@QueryParam("project") String project) {

        String command = "oc";
        String[] ARGS = new String[8];
        ARGS[0] = command;
        ARGS[1] = "get";
        ARGS[2] = kind;
        ARGS[3] = name;
        ARGS[4] = "-n";
        ARGS[5] = project;
        ARGS[6] = "-o";
        ARGS[7] = "json";

        try {
            String output = executeCommand(ARGS);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonObject jsonObject = JsonObject.mapFrom(objectMapper.readTree(output));
//            String prettyStatus = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject).replace("\\\n", '');

            return Response.ok(output).build();
        } catch (IOException | InterruptedException ex) {
            return Response.serverError().entity("Error executing command: " + ex.getMessage()).build();
        }
    }



    @POST
    @Path("/update-resource")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateResource(InputStream requestBody) {
        String command = "oc";
        String[] ARGS = new String[] {command, "apply", "-f", "-"};

        try {
            // Read the request body into a String
            BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String resourceDefinition = stringBuilder.toString();

            // Execute the oc create command with the resource definition passed as input
            ProcessBuilder builder = new ProcessBuilder(ARGS);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            OutputStream outputStream = process.getOutputStream();
            outputStream.write(resourceDefinition.getBytes());
            outputStream.flush();
            outputStream.close();

            // Read the output of the command and return it as a response
            InputStream inputStream = process.getInputStream();
            String output = new String(inputStream.readAllBytes(), Charset.defaultCharset());
            inputStream.close();

            System.out.println(output);
            return Response.ok(output).build();
        } catch (IOException ex) {
            return Response.serverError().entity("Error executing command: " + ex.getMessage()).build();
        }
    }


    @GET
    @Path("/get-status")
    public Response GetStatus(@QueryParam("args") String args,@QueryParam("name") String name,@QueryParam("kind") String kind,@QueryParam("project") String project) {

        String command = "oc";
        String[] ARGS = new String[8];
        ARGS[0] = command;
        ARGS[1] = "get";
        ARGS[2] = kind;
        ARGS[3] = name;
        ARGS[4] = "-n";
        ARGS[5] = project;
        ARGS[6] = "-o";
        ARGS[7] = "json";
        try {
            String output = executeCommand(ARGS);

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
//            output = output.replaceAll("\\\\n", "\n"); // Replace "\n" with actual new line
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