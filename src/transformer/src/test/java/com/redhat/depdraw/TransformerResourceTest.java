package com.redhat.depdraw;

import com.redhat.depdraw.model.DiagramResource;
import com.redhat.depdraw.model.Line;
import com.redhat.depdraw.model.LineCatalog;
import com.redhat.com.depdraw.rest.client.DataServiceClient;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@QuarkusTest
public class TransformerResourceTest {

    @InjectMock
    @RestClient
    DataServiceClient dataServiceClient;

    @Test
    public void testTransformerHealthEndpoint() {
        given()
          .when().get("/health")
          .then()
             .statusCode(200);
    }

    @Test
    public void testTransformDiagramLinesWithLabels() {
        DiagramResource dr = new DiagramResource();
        dr.setUuid("1");

        DiagramResource dr2 = new DiagramResource();
        dr2.setUuid("2");
        LineCatalog lineCatalog = new LineCatalog("uuid", "lineCatalog1", Set.of(LineCatalog.INHERIT_LABELS));
        List<Line> lines = List.of(new Line("uuid", lineCatalog, dr, dr2));
        Mockito.when(dataServiceClient.getLinesByDigramId(anyString())).thenReturn(lines);
        Mockito.when(dataServiceClient.getLineCatalogById(anyString())).thenReturn(lineCatalog);
        Mockito.when(dataServiceClient.getDefinition(anyString(), eq("1"))).thenReturn("{\n" +
                "  \"apiVersion\": \"v1\",\n" +
                        "  \"kind\": \"Pod\",\n" +
                        "  \"metadata\": {\n" +
                        "    \"name\": \"xmlxml\",\n" +
                        "    \"labels\": {\n" +
                        "       \"environment\" : \"production\",\n" +
                        "       \"app\": \"nginx\"\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"spec\": {\n" +
                        "    \"containers\": [\n" +
                        "      {\n" +
                        "        \"name\": \"nginx\",\n" +
                        "        \"image\": \"nginx:1.14.2\",\n" +
                        "        \"ports\": [\n" +
                        "          {\n" +
                        "            \"containerPort\": 11\n" +
                        "          }\n" +
                        "        ]\n" +
                        "      }\n" +
                        "    ]\n" +
                        "  }\n" +
                        "}");
        Mockito.when(dataServiceClient.getDefinition(anyString(), eq("2"))).thenReturn("{\n" +
                "  \"apiVersion\": \"v1\",\n" +
                "  \"kind\": \"Pod\",\n" +
                "  \"metadata\": {\n" +
                "    \"name\": \"destName\"\n" +
                "  },\n" +
                "  \"spec\": {\n" +
                "    \"containers\": [\n" +
                "      {\n" +
                "        \"name\": \"nginx\",\n" +
                "        \"image\": \"nginx:1.14.2\",\n" +
                "        \"ports\": [\n" +
                "          {\n" +
                "            \"containerPort\": 11\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}");

        given()
                .when().post("/transform/1111-1111/lines")
                .then()
                .statusCode(200);
    }

    @Test
    public void testTransformDiagramLinesWithDestExistingLabels() {
        DiagramResource dr = new DiagramResource();
        dr.setUuid("1");

        DiagramResource dr2 = new DiagramResource();
        dr2.setUuid("2");

        LineCatalog lineCatalog = new LineCatalog("uuid", "lineCatalog1", Set.of(LineCatalog.INHERIT_LABELS));
        List<Line> lines = List.of(new Line("uuid", lineCatalog, dr, dr2));
        Mockito.when(dataServiceClient.getLinesByDigramId(anyString())).thenReturn(lines);
        Mockito.when(dataServiceClient.getLineCatalogById(anyString())).thenReturn(lineCatalog);
        Mockito.when(dataServiceClient.getDefinition(anyString(), eq("1"))).thenReturn("{\n" +
                "  \"apiVersion\": \"v1\",\n" +
                "  \"kind\": \"Pod\",\n" +
                "  \"metadata\": {\n" +
                "    \"name\": \"xmlxml\",\n" +
                "    \"labels\": {\n" +
                "       \"environment\" : \"production\",\n" +
                "       \"app\": \"nginx\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"spec\": {\n" +
                "    \"containers\": [\n" +
                "      {\n" +
                "        \"name\": \"nginx\",\n" +
                "        \"image\": \"nginx:1.14.2\",\n" +
                "        \"ports\": [\n" +
                "          {\n" +
                "            \"containerPort\": 11\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}");
        Mockito.when(dataServiceClient.getDefinition(anyString(), eq("2"))).thenReturn("{\n" +
                "  \"apiVersion\": \"v1\",\n" +
                "  \"kind\": \"Pod\",\n" +
                "  \"metadata\": {\n" +
                "    \"name\": \"xmlxml\",\n" +
                "    \"labels\": {\n" +
                "       \"environment\" : \"production\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"spec\": {\n" +
                "    \"containers\": [\n" +
                "      {\n" +
                "        \"name\": \"nginx\",\n" +
                "        \"image\": \"nginx:1.14.2\",\n" +
                "        \"ports\": [\n" +
                "          {\n" +
                "            \"containerPort\": 11\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}");

        given()
                .when().post("/transform/1111-1111/lines")
                .then()
                .statusCode(200);
    }

    @Test
    public void testTransformDiagramLinesWithAnnotations() {
        DiagramResource dr = new DiagramResource();
        dr.setUuid("1");

        DiagramResource dr2 = new DiagramResource();
        dr2.setUuid("2");

        LineCatalog lineCatalog = new LineCatalog("uuid", "lineCatalog1", Set.of(LineCatalog.INHERIT_ANNOTATIONS));
        List<Line> lines = List.of(new Line("uuid", lineCatalog, dr, dr2));
        Mockito.when(dataServiceClient.getLinesByDigramId(anyString())).thenReturn(lines);
        Mockito.when(dataServiceClient.getLineCatalogById(anyString())).thenReturn(lineCatalog);
        Mockito.when(dataServiceClient.getDefinition(anyString(), eq("1"))).thenReturn("{\n" +
                "  \"apiVersion\": \"v1\",\n" +
                "  \"kind\": \"Pod\",\n" +
                "  \"metadata\": {\n" +
                "    \"name\": \"xmlxml\",\n" +
                "    \"annotations\": {\n" +
                "       \"environment\" : \"production\",\n" +
                "       \"app\": \"nginx\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"spec\": {\n" +
                "    \"containers\": [\n" +
                "      {\n" +
                "        \"name\": \"nginx\",\n" +
                "        \"image\": \"nginx:1.14.2\",\n" +
                "        \"ports\": [\n" +
                "          {\n" +
                "            \"containerPort\": 11\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}");
        Mockito.when(dataServiceClient.getDefinition(anyString(), eq("2"))).thenReturn("{\n" +
                "  \"apiVersion\": \"v1\",\n" +
                "  \"kind\": \"Pod\",\n" +
                "  \"metadata\": {\n" +
                "    \"name\": \"xmlxml\"\n" +
                "  },\n" +
                "  \"spec\": {\n" +
                "    \"containers\": [\n" +
                "      {\n" +
                "        \"name\": \"nginx\",\n" +
                "        \"image\": \"nginx:1.14.2\",\n" +
                "        \"ports\": [\n" +
                "          {\n" +
                "            \"containerPort\": 11\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}");

        given()
                .when().post("/transform/1111-1111/lines")
                .then()
                .statusCode(200);
    }

    @Test
    public void testTransformDiagramLinesWithDestExistingAnnotations() {
        DiagramResource dr = new DiagramResource();
        dr.setUuid("1");

        DiagramResource dr2 = new DiagramResource();
        dr2.setUuid("2");
        LineCatalog lineCatalog = new LineCatalog("uuid", "lineCatalog1", Set.of(LineCatalog.INHERIT_ANNOTATIONS));
        List<Line> lines = List.of(new Line("uuid", lineCatalog, dr, dr2));
        Mockito.when(dataServiceClient.getLinesByDigramId(anyString())).thenReturn(lines);
        Mockito.when(dataServiceClient.getLineCatalogById(anyString())).thenReturn(lineCatalog);
        Mockito.when(dataServiceClient.getDefinition(anyString(), eq("1"))).thenReturn("{\n" +
                "  \"apiVersion\": \"v1\",\n" +
                "  \"kind\": \"Pod\",\n" +
                "  \"metadata\": {\n" +
                "    \"name\": \"xmlxml\",\n" +
                "    \"annotations\": {\n" +
                "       \"environment\" : \"production\",\n" +
                "       \"app\": \"nginx\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"spec\": {\n" +
                "    \"containers\": [\n" +
                "      {\n" +
                "        \"name\": \"nginx\",\n" +
                "        \"image\": \"nginx:1.14.2\",\n" +
                "        \"ports\": [\n" +
                "          {\n" +
                "            \"containerPort\": 11\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}");
        Mockito.when(dataServiceClient.getDefinition(anyString(), eq("2"))).thenReturn("{\n" +
                "  \"apiVersion\": \"v1\",\n" +
                "  \"kind\": \"Pod\",\n" +
                "  \"metadata\": {\n" +
                "    \"name\": \"xmlxml\",\n" +
                "    \"annotations\": {\n" +
                "       \"environment\" : \"production\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"spec\": {\n" +
                "    \"containers\": [\n" +
                "      {\n" +
                "        \"name\": \"nginx\",\n" +
                "        \"image\": \"nginx:1.14.2\",\n" +
                "        \"ports\": [\n" +
                "          {\n" +
                "            \"containerPort\": 11\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}");

        given()
                .when().post("/transform/1111-1111/lines")
                .then()
                .statusCode(200);
    }

    @Test
    public void testTransformDiagramLinesWithMetadata() {
        DiagramResource dr = new DiagramResource();
        dr.setUuid("1");

        DiagramResource dr2 = new DiagramResource();
        dr2.setUuid("2");

        LineCatalog lineCatalog = new LineCatalog("uuid", "lineCatalog1", Set.of(LineCatalog.INHERIT_METADATA));
        List<Line> lines = List.of(new Line("uuid", lineCatalog, dr, dr2));
        Mockito.when(dataServiceClient.getLinesByDigramId(anyString())).thenReturn(lines);
        Mockito.when(dataServiceClient.getLineCatalogById(anyString())).thenReturn(lineCatalog);
        Mockito.when(dataServiceClient.getDefinition(anyString(), eq("1"))).thenReturn("{\n" +
                "  \"apiVersion\": \"v1\",\n" +
                "  \"kind\": \"Pod\",\n" +
                "  \"metadata\": {\n" +
                "    \"name\": \"xmlxml\",\n" +
                "    \"labels\": {\n" +
                "       \"environment\" : \"production\",\n" +
                "       \"app\": \"nginx\"\n" +
                "    },\n" +
                "    \"annotations\": {\n" +
                "       \"environment\" : \"annotation-test\",\n" +
                "       \"app\": \"annotation-test\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"spec\": {\n" +
                "    \"containers\": [\n" +
                "      {\n" +
                "        \"name\": \"nginx\",\n" +
                "        \"image\": \"nginx:1.14.2\",\n" +
                "        \"ports\": [\n" +
                "          {\n" +
                "            \"containerPort\": 11\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}");
        Mockito.when(dataServiceClient.getDefinition(anyString(), eq("2"))).thenReturn("{\n" +
                "  \"apiVersion\": \"v1\",\n" +
                "  \"kind\": \"Pod\",\n" +
                "  \"metadata\": {\n" +
                "    \"name\": \"destName\"\n" +
                "  },\n" +
                "  \"spec\": {\n" +
                "    \"containers\": [\n" +
                "      {\n" +
                "        \"name\": \"nginx\",\n" +
                "        \"image\": \"nginx:1.14.2\",\n" +
                "        \"ports\": [\n" +
                "          {\n" +
                "            \"containerPort\": 11\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}");

        given()
                .when().post("/transform/1111-1111/lines")
                .then()
                .statusCode(200);
    }

    @Test
    public void testTransformDiagramLinesWithDestExistingMetadata() {
        DiagramResource dr = new DiagramResource();
        dr.setUuid("1");

        DiagramResource dr2 = new DiagramResource();
        dr2.setUuid("2");

        LineCatalog lineCatalog = new LineCatalog("uuid", "lineCatalog1", Set.of(LineCatalog.INHERIT_METADATA));
        List<Line> lines = List.of(new Line("uuid", lineCatalog, dr, dr2));
        Mockito.when(dataServiceClient.getLinesByDigramId(anyString())).thenReturn(lines);
        Mockito.when(dataServiceClient.getLineCatalogById(anyString())).thenReturn(lineCatalog);
        Mockito.when(dataServiceClient.getDefinition(anyString(), eq("1"))).thenReturn("{\n" +
                "  \"apiVersion\": \"v1\",\n" +
                "  \"kind\": \"Pod\",\n" +
                "  \"metadata\": {\n" +
                "    \"name\": \"xmlxml\",\n" +
                "    \"labels\": {\n" +
                "       \"environment\" : \"production\",\n" +
                "       \"app\": \"nginx\"\n" +
                "    },\n" +
                "    \"annotations\": {\n" +
                "       \"environment\" : \"annotation-test\",\n" +
                "       \"app\": \"annotation-test\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"spec\": {\n" +
                "    \"containers\": [\n" +
                "      {\n" +
                "        \"name\": \"nginx\",\n" +
                "        \"image\": \"nginx:1.14.2\",\n" +
                "        \"ports\": [\n" +
                "          {\n" +
                "            \"containerPort\": 11\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}");
        Mockito.when(dataServiceClient.getDefinition(anyString(), eq("2"))).thenReturn("{\n" +
                "  \"apiVersion\": \"v1\",\n" +
                "  \"kind\": \"Pod\",\n" +
                "  \"metadata\": {\n" +
                "    \"name\": \"xmlxml\",\n" +
                "    \"labels\": {\n" +
                "       \"app\": \"nginx\"\n" +
                "    },\n" +
                "    \"annotations\": {\n" +
                "       \"app\": \"annotation-test\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"spec\": {\n" +
                "    \"containers\": [\n" +
                "      {\n" +
                "        \"name\": \"nginx\",\n" +
                "        \"image\": \"nginx:1.14.2\",\n" +
                "        \"ports\": [\n" +
                "          {\n" +
                "            \"containerPort\": 11\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}");

        given()
                .when().post("/transform/1111-1111/lines")
                .then()
                .statusCode(200);
    }

    @Test
    public void testTransformDiagramLinesWithSelector() {
        DiagramResource dr = new DiagramResource();
        dr.setUuid("1");

        DiagramResource dr2 = new DiagramResource();
        dr2.setUuid("2");

        LineCatalog lineCatalog = new LineCatalog("uuid", "lineCatalog1", Set.of(LineCatalog.SELECT_RESOURCE));
        List<Line> lines = List.of(new Line("uuid", lineCatalog, dr, dr2));
        Mockito.when(dataServiceClient.getLinesByDigramId(anyString())).thenReturn(lines);
        Mockito.when(dataServiceClient.getLineCatalogById(anyString())).thenReturn(lineCatalog);
        Mockito.when(dataServiceClient.getDefinition(anyString(), eq("1"))).thenReturn("{\n" +
                "  \"apiVersion\": \"v1\",\n" +
                "  \"kind\": \"Pod\",\n" +
                "  \"metadata\": {\n" +
                "    \"name\": \"destName\"\n" +
                "  },\n" +
                "  \"spec\": {\n" +
                "    \"selector\": {\n" +
                "      \"matchLabels\": {\n" +
                "        \"app\": \"metadata-pod\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"containers\": [\n" +
                "      {\n" +
                "        \"name\": \"nginx\",\n" +
                "        \"image\": \"nginx:1.14.2\",\n" +
                "        \"ports\": [\n" +
                "          {\n" +
                "            \"containerPort\": 11\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}");
        Mockito.when(dataServiceClient.getDefinition(anyString(), eq("2"))).thenReturn("{\n" +
                "  \"apiVersion\": \"v1\",\n" +
                "  \"kind\": \"Pod\",\n" +
                "  \"metadata\": {\n" +
                "    \"name\": \"xmlxml\"\n" +
                "  },\n" +
                "  \"spec\": {\n" +
                "    \"containers\": [\n" +
                "      {\n" +
                "        \"name\": \"nginx\",\n" +
                "        \"image\": \"nginx:1.14.2\",\n" +
                "        \"ports\": [\n" +
                "          {\n" +
                "            \"containerPort\": 11\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}");

        given()
                .when().post("/transform/1111-1111/lines")
                .then()
                .statusCode(200);
    }

}