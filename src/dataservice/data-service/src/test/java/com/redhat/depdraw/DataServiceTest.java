package com.redhat.depdraw;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.redhat.depdraw.dto.DiagramResourceDTO;
import com.redhat.depdraw.dto.LineDTO;
import com.redhat.depdraw.model.*;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ResponseBody;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class DataServiceTest {

    @Test
    public void testDataServiceHealthEndpoint() {
        given()
                .when().get("/health")
                .then()
                .statusCode(200);
    }

    @Test
    public void testDeleteDiagram() {
        Diagram d = new Diagram();
        d.setName("testDiagram");

        //Create Diagram
        final String uuid = given().body(d)
                .contentType(ContentType.JSON)
                .when().post("/diagrams").getBody().as(Diagram.class).getUuid();

        //Get Diagram by Id
        given().body(d)
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}", uuid)
                .then()
                .statusCode(200)
                .body("name", is("testDiagram"))
                .body("resources", emptyCollectionOf(Set.class))
                .body("lines", emptyCollectionOf(Set.class))
                .body("uuid", equalTo(uuid));

        //Delete Diagram
        given().body(d)
                .contentType(ContentType.JSON)
                .when().delete("/diagrams/{diagramId}", uuid)
                .then()
                .statusCode(200);

        //Get Diagram
        given().body(d)
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}", uuid)
                .then()
                .statusCode(200)
                .body(emptyString());
    }

    @Test
    public void testGetMultipleDiagrams() {
        Diagram d = new Diagram();
        d.setName("testDiagram");

        //Create Diagram
        final String uuid1 = given().body(d)
                .contentType(ContentType.JSON)
                .when().post("/diagrams").getBody().as(Diagram.class).getUuid();

        //Create another Diagram
        final String uuid2 = given().body(d)
                .contentType(ContentType.JSON)
                .when().post("/diagrams").getBody().as(Diagram.class).getUuid();

        //Get all Diagrams
        List<Diagram> diagrams = given().body(d)
                .contentType(ContentType.JSON)
                .when().get("/diagrams")
                .then()
                .statusCode(200).extract().as(new TypeRef<>() {});
        Assertions.assertEquals(2, diagrams.size());

        //Delete Diagram
        given().body(d)
                .contentType(ContentType.JSON)
                .when().delete("/diagrams/{diagramId}", uuid1)
                .then()
                .statusCode(200)
                .body(emptyString());

        //Delete second Diagram
        given().body(d)
                .contentType(ContentType.JSON)
                .when().delete("/diagrams/{diagramId}", uuid2)
                .then()
                .statusCode(200)
                .body(emptyString());

    }

    @Test
    public void testDeleteDiagramWithMultipleDiagramResources() throws JsonProcessingException {
        ResourceCatalog rc1 = new ResourceCatalog("387585aa-8382-11ed-a1eb-0242ac120002", "Service", "f54012e8-8311-11ed-a1eb-0242ac120002");
        ResourceCatalog rc2 = new ResourceCatalog("0ab01ee0-8211-11ed-a1eb-0242ac120002", "POD", "3cfea982-7ec6-11ed-a1eb-0242ac120002");
        Diagram d = new Diagram();
        d.setName("testDiagram");

        DiagramResourceDTO dr = new DiagramResourceDTO();
        dr.setName("testDiagramResource");
        dr.setResourceCatalogID("387585aa-8382-11ed-a1eb-0242ac120002");
        dr.setPosX(1);
        dr.setPosY(1);
        dr.setType("type1");

        DiagramResourceDTO dr2 = new DiagramResourceDTO();
        dr2.setName("testDiagramResource2");
        dr2.setResourceCatalogID("0ab01ee0-8211-11ed-a1eb-0242ac120002");
        dr2.setPosX(1);
        dr2.setPosY(1);
        dr2.setType("type2");

        //Create Diagram
        final String uuid = given().body(d)
                .contentType(ContentType.JSON)
                .when().post("/diagrams").getBody().as(Diagram.class).getUuid();

        //Get Diagram by Id
        given().body(d)
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}", uuid)
                .then()
                .statusCode(200)
                .body("name", is("testDiagram"))
                .body("resources", emptyCollectionOf(Set.class))
                .body("lines", emptyCollectionOf(Set.class))
                .body("uuid", equalTo(uuid));

        //Create DiagramResource
        final String drUuid = given().body(dr)
                .contentType(ContentType.JSON)
                .when().post("/diagrams/{diagramId}/resources", uuid).getBody().as(DiagramResource.class).getUuid();

        //Get DiagramResource by Id
        DiagramResource diagramResource = given().body(dr)
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}/resources/{diagramResourceId}", uuid, drUuid)
                .then()
                .statusCode(200).extract().as(DiagramResource.class);

        Assertions.assertEquals("testDiagramResource", diagramResource.getName());
        Assertions.assertEquals(rc1, diagramResource.getResourceCatalog());
        Assertions.assertEquals(drUuid, diagramResource.getUuid());

        //Create another DiagramResource
        final String drUuid2 = given().body(dr2)
                .contentType(ContentType.JSON)
                .when().post("/diagrams/{diagramId}/resources", uuid).getBody().as(DiagramResource.class).getUuid();

        //Get second DiagramResource by Id
        DiagramResource diagramResource2 = given().body(dr2)
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}/resources/{diagramResourceId}", uuid, drUuid2)
                .then()
                .statusCode(200).extract().as(DiagramResource.class);

        Assertions.assertEquals("testDiagramResource2", diagramResource2.getName());
        Assertions.assertEquals(rc2, diagramResource2.getResourceCatalog());
        Assertions.assertEquals(drUuid2, diagramResource2.getUuid());

        //Get Diagram by Id
        given().body(d)
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}", uuid)
                .then()
                .statusCode(200)
                .body("name", is("testDiagram"))
                .body("resources", iterableWithSize(2))
                .body("lines", emptyCollectionOf(Set.class))
                .body("uuid", equalTo(uuid));

        //Get all DiagramResources
        given()
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}/resources", uuid)
                .then()
                .statusCode(200)
                .body("", iterableWithSize(2));

        //Delete Diagram
        given().body(d)
                .contentType(ContentType.JSON)
                .when().delete("/diagrams/{diagramId}", uuid)
                .then()
                .statusCode(200);

        //Get Diagram by Id
        given().body(d)
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}", uuid)
                .then()
                .statusCode(200)
                .body(emptyString());

        //Get DiagramResource by Id
        given().body(dr)
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}/resources/{diagramResourceId}", uuid, drUuid)
                .then()
                .statusCode(200)
                .body(emptyString());

        //Get second DiagramResource by Id
        given().body(dr2)
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}/resources/{diagramResourceId}", uuid, drUuid2)
                .then()
                .statusCode(200)
                .body(emptyString());
    }

     @Test
     public void testDeleteDiagramWithMultipleLines() throws JsonProcessingException {
         ResourceCatalog rc1 = new ResourceCatalog("387585aa-8382-11ed-a1eb-0242ac120002", "Service", "f54012e8-8311-11ed-a1eb-0242ac120002");
         ResourceCatalog rc2 = new ResourceCatalog("0ab01ee0-8211-11ed-a1eb-0242ac120002", "POD", "3cfea982-7ec6-11ed-a1eb-0242ac120002");
         Diagram d = new Diagram();
         d.setName("testDiagram");

         DiagramResourceDTO dr = new DiagramResourceDTO();
         dr.setName("testDiagramResource");
         dr.setResourceCatalogID("387585aa-8382-11ed-a1eb-0242ac120002");
         dr.setPosX(1);
         dr.setPosY(1);
         dr.setType("type1");

         DiagramResourceDTO dr2 = new DiagramResourceDTO();
         dr2.setName("testDiagramResource2");
         dr2.setResourceCatalogID("0ab01ee0-8211-11ed-a1eb-0242ac120002");
         dr2.setPosX(1);
         dr2.setPosY(1);
         dr2.setType("type1");

         //Create Diagram
         Diagram diagram = given().body(d)
                 .contentType(ContentType.JSON)
                 .when().post("/diagrams").getBody().as(Diagram.class);
         final String uuid = diagram.getUuid();

         //Get Diagram by Id
         given().body(d)
                 .contentType(ContentType.JSON)
                 .when().get("/diagrams/{diagramId}", uuid)
                 .then()
                 .statusCode(200)
                 .body("name", is("testDiagram"))
                 .body("resources", emptyCollectionOf(Set.class))
                 .body("lines", emptyCollectionOf(Set.class))
                 .body("uuid", equalTo(uuid));

         //Create DiagramResource
         DiagramResource diagramResource = given().body(dr)
                 .contentType(ContentType.JSON)
                 .when().post("/diagrams/{diagramId}/resources", uuid).getBody().as(DiagramResource.class);

         final String drUuid = diagramResource.getUuid();
         Assertions.assertEquals("testDiagramResource", diagramResource.getName());
         Assertions.assertEquals(rc1, diagramResource.getResourceCatalog());
         Assertions.assertEquals(drUuid, diagramResource.getUuid());

         //Get DiagramResource by Id
         diagramResource = given().body(dr)
                 .contentType(ContentType.JSON)
                 .when().get("/diagrams/{diagramId}/resources/{diagramResourceId}", uuid, drUuid)
                 .then()
                 .statusCode(200).extract().as(DiagramResource.class);

         Assertions.assertEquals("testDiagramResource", diagramResource.getName());
         Assertions.assertEquals(rc1, diagramResource.getResourceCatalog());
         Assertions.assertEquals(drUuid, diagramResource.getUuid());

         //Create another DiagramResource
         DiagramResource diagramResource2 = given().body(dr2)
                 .contentType(ContentType.JSON)
                 .when().post("/diagrams/{diagramId}/resources", uuid).getBody().as(DiagramResource.class);
         final String drUuid2 = diagramResource2.getUuid();

         Assertions.assertEquals("testDiagramResource2", diagramResource2.getName());
         Assertions.assertEquals(rc2, diagramResource2.getResourceCatalog());
         Assertions.assertEquals(drUuid2, diagramResource2.getUuid());

         //Get second DiagramResource by Id
         diagramResource2 = given().body(dr2)
                 .contentType(ContentType.JSON)
                 .when().get("/diagrams/{diagramId}/resources/{diagramResourceId}", uuid, drUuid2)
                 .then()
                 .statusCode(200).extract().as(DiagramResource.class);

         Assertions.assertEquals("testDiagramResource2", diagramResource2.getName());
         Assertions.assertEquals(rc2, diagramResource2.getResourceCatalog());
         Assertions.assertEquals(drUuid2, diagramResource2.getUuid());

         //Get Diagram by Id
         given().body(d)
                 .contentType(ContentType.JSON)
                 .when().get("/diagrams/{diagramId}", uuid)
                 .then()
                 .statusCode(200)
                 .body("name", is("testDiagram"))
                 .body("resources", iterableWithSize(2))
                 .body("lines", emptyCollectionOf(Set.class))
                 .body("uuid", equalTo(uuid));

         //Get all DiagramResources
         given()
                 .contentType(ContentType.JSON)
                 .when().get("/diagrams/{diagramId}/resources", uuid)
                 .then()
                 .statusCode(200)
                 .body("", iterableWithSize(2));

         //Get lineCatalog
         String lcuuid = "ef18d3dc-8cf8-11ed-a1eb-0242ac120002";
         LineCatalog lineCatalog = given()
                 .contentType(ContentType.JSON)
                 .when().get("/linecatalogs/{lineCatalogId}", lcuuid).as(LineCatalog.class);
         Assertions.assertEquals(lcuuid, lineCatalog.getUuid());
         Assertions.assertEquals("Inherit Labels", lineCatalog.getName());
         Assertions.assertEquals(1, lineCatalog.getRules().size());

         LineDTO line = new LineDTO();
         line.setDestination(diagramResource.getUuid());
         line.setSource(diagramResource2.getUuid());
         line.setLineCatalogID(lcuuid);

         //Create another Line
         final String lineUuid = given().body(line)
                 .contentType(ContentType.JSON)
                 .when().post("/diagrams/{diagramId}/lines", uuid).getBody().as(Line.class).getUuid();

         //Get Line by Id
         Line line1 = given().body(line)
                 .contentType(ContentType.JSON)
                 .when().get("/diagrams/{diagramId}/lines/{lineId}", uuid, lineUuid)
                 .then()
                 .statusCode(200).extract().as(Line.class);

         Assertions.assertEquals(lineCatalog, line1.getLineCatalog());
         Assertions.assertEquals(diagramResource2, line1.getSource());
         Assertions.assertEquals(diagramResource, line1.getDestination());
         Assertions.assertEquals(lineUuid, line1.getUuid());

         //Get all Lines
         given()
                 .contentType(ContentType.JSON)
                 .when().get("/diagrams/{diagramId}/lines", uuid)
                 .then()
                 .statusCode(200)
                 .body("", iterableWithSize(1));

         //Get Diagram by Id
         given().body(d)
                 .contentType(ContentType.JSON)
                 .when().get("/diagrams/{diagramId}", uuid)
                 .then()
                 .statusCode(200)
                 .body("name", is("testDiagram"))
                 .body("resources", iterableWithSize(2))
                 .body("lines", iterableWithSize(1))
                 .body("uuid", equalTo(uuid));

         //Delete Diagram
         given().body(d)
                 .contentType(ContentType.JSON)
                 .when().delete("/diagrams/{diagramId}", uuid)
                 .then()
                 .statusCode(200);

         //Get Diagram by Id
         given().body(d)
                 .contentType(ContentType.JSON)
                 .when().get("/diagrams/{diagramId}", uuid)
                 .then()
                 .statusCode(200)
                 .body(emptyString());

         //Get DiagramResource by Id
         given().body(dr)
                 .contentType(ContentType.JSON)
                 .when().get("/diagrams/{diagramId}/resources/{diagramResourceId}", uuid, drUuid)
                 .then()
                 .statusCode(200)
                 .body(emptyString());

         //Get second DiagramResource by Id
         given().body(dr2)
                 .contentType(ContentType.JSON)
                 .when().get("/diagrams/{diagramId}/resources/{diagramResourceId}", uuid, drUuid2)
                 .then()
                 .statusCode(200)
                 .body(emptyString());

         //Get Line by Id
         given().body(line)
                 .contentType(ContentType.JSON)
                 .when().get("/diagrams/{diagramId}/lines/{lineId}", uuid, lineUuid)
                 .then()
                 .statusCode(200)
                 .body(emptyString());
     }

    @Test
    public void testDeleteDiagramResources() {
        ResourceCatalog rc = new ResourceCatalog("387585aa-8382-11ed-a1eb-0242ac120002", "Service", "f54012e8-8311-11ed-a1eb-0242ac120002");
        Diagram d = new Diagram();
        d.setName("testDiagram");

        DiagramResourceDTO dr = new DiagramResourceDTO();
        dr.setName("testDiagramResource");
        dr.setResourceCatalogID("387585aa-8382-11ed-a1eb-0242ac120002");
        dr.setPosX(1);
        dr.setPosY(1);
        dr.setType("type1");

        //Create Diagram
        final String uuid = given().body(d)
                .contentType(ContentType.JSON)
                .when().post("/diagrams").getBody().as(Diagram.class).getUuid();

        //Get Diagram by Id
        given().body(d)
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}", uuid)
                .then()
                .statusCode(200)
                .body("name", is("testDiagram"))
                .body("resources", emptyCollectionOf(Set.class))
                .body("lines", emptyCollectionOf(Set.class))
                .body("uuid", equalTo(uuid));

        //Create DiagramResource
        final String drUuid = given().body(dr)
                .contentType(ContentType.JSON)
                .when().post("/diagrams/{diagramId}/resources", uuid).getBody().as(DiagramResource.class).getUuid();

        //Get DiagramResource by Id
        DiagramResource diagramResource = given().body(dr)
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}/resources/{diagramResourceId}", uuid, drUuid)
                .then()
                .statusCode(200).extract().as(DiagramResource.class);

        Assertions.assertEquals("testDiagramResource", diagramResource.getName());
        Assertions.assertEquals(rc, diagramResource.getResourceCatalog());
        Assertions.assertEquals(drUuid, diagramResource.getUuid());

        //Get Diagram by Id
        given().body(d)
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}", uuid)
                .then()
                .statusCode(200)
                .body("name", is("testDiagram"))
                .body("resources", iterableWithSize(1))
                .body("lines", emptyCollectionOf(Set.class))
                .body("uuid", equalTo(uuid));

        //Delete DiagramResource
        given().body(dr)
                .contentType(ContentType.JSON)
                .when().delete("/diagrams/{diagramId}/resources/{diagramResourceId}", uuid, drUuid)
                .then()
                .statusCode(200);

        //Get Diagram by Id
        given().body(d)
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}", uuid)
                .then()
                .statusCode(200)
                .body("name", is("testDiagram"))
                .body("resources", emptyCollectionOf(Set.class))
                .body("lines", emptyCollectionOf(Set.class))
                .body("uuid", equalTo(uuid));

        //Get DiagramResource by Id
        given().body(dr)
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}/resources/{diagramResourceId}", uuid, drUuid)
                .then()
                .statusCode(200)
                .body(emptyString());

        //Delete Diagram
        given().body(d)
                .contentType(ContentType.JSON)
                .when().delete("/diagrams/{diagramId}", uuid)
                .then()
                .statusCode(200);

        //Get Diagram by Id
        given().body(d)
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}", uuid)
                .then()
                .statusCode(200)
                .body(emptyString());
    }

    @Test
    public void testUpdateDefinition() {
        Diagram d = new Diagram();
        d.setName("testDiagram");

        DiagramResourceDTO dr = new DiagramResourceDTO();
        dr.setName("testDiagramResource");
        dr.setResourceCatalogID("387585aa-8382-11ed-a1eb-0242ac120002");
        dr.setPosX(1);
        dr.setPosY(1);
        dr.setType("type1");

        //Create Diagram
        final String uuid = given().body(d)
                .contentType(ContentType.JSON)
                .when().post("/diagrams").getBody().as(Diagram.class).getUuid();

        //Get Diagram by Id
        given().body(d)
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}", uuid)
                .then()
                .statusCode(200)
                .body("name", is("testDiagram"))
                .body("resources", emptyCollectionOf(Set.class))
                .body("lines", emptyCollectionOf(Set.class))
                .body("uuid", equalTo(uuid));

        //Create DiagramResource
        final String drUuid = given().body(dr)
                .contentType(ContentType.JSON)
                .when().post("/diagrams/{diagramId}/resources", uuid).getBody().as(DiagramResource.class).getUuid();

        //Get DiagramResource by id
        ResponseBody body = given().body(d)
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}/resources", uuid)
                .getBody();
        String drId = body.as(new TypeRef<List<DiagramResource>>() {}).get(0).getUuid();

        //Update Definition
        given().body("{\n" +
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
                        "}")
                .contentType(ContentType.JSON)
                .when().post("/diagrams/{diagramId}/resources/{diagramResourceId}/definition", uuid, drId)
                .then()
                .statusCode(200);

        //Get Definition
        given()
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}/resources/{diagramResourceId}/definition", uuid, drId)
                .then()
                .statusCode(200)
                .body(equalTo("{\n" +
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
                        "}"));

        //Update Definition
        given().body("{\n" +
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
                        "}")
                .contentType(ContentType.JSON)
                .when().post("/diagrams/{diagramId}/resources/{diagramResourceId}/definition", uuid, drId)
                .then()
                .statusCode(200);

        //Get Definition
        given()
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}/resources/{diagramResourceId}/definition", uuid, drId)
                .then()
                .statusCode(200)
                .body(equalTo("{\n" +
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
                        "}"));

        //Delete Diagram
        given().body(d)
                .contentType(ContentType.JSON)
                .when().delete("/diagrams/{diagramId}", uuid)
                .then()
                .statusCode(200);

        //Get Diagram
        given().body(d)
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}", uuid)
                .then()
                .statusCode(200)
                .body(emptyString());
    }
}