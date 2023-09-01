package com.redhat.depdraw;

import java.util.List;
import java.util.Set;

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
                .body("resourcesID", emptyCollectionOf(Set.class))
                .body("linesID", emptyCollectionOf(Set.class))
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
        given().body(d)
                .contentType(ContentType.JSON)
                .when().get("/diagrams")
                .then()
                .statusCode(200)
                .body("", iterableWithSize(2));

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
    public void testDeleteDiagramWithMultipleDiagramResources() {
        ResourceCatalog rc1 = new ResourceCatalog("387585aa-8382-11ed-a1eb-0242ac120002", "ResourceCatalog1", "");
        ResourceCatalog rc2 = new ResourceCatalog("0ab01ee0-8211-11ed-a1eb-0242ac120002", "ResourceCatalog2", "");
        Diagram d = new Diagram();
        d.setName("testDiagram");

        DiagramResource dr = new DiagramResource();
        dr.setName("testDiagramResource");
        dr.setResourceCatalog(rc1);

        DiagramResource dr2 = new DiagramResource();
        dr2.setName("testDiagramResource2");
        dr2.setResourceCatalog(rc2);
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
                .body("resourcesID", emptyCollectionOf(Set.class))
                .body("linesID", emptyCollectionOf(Set.class))
                .body("uuid", equalTo(uuid));

        //Create DiagramResource
        final String drUuid = given().body(dr)
                .contentType(ContentType.JSON)
                .when().post("/diagrams/{diagramId}/resources", uuid).getBody().as(DiagramResource.class).getUuid();


        //Get DiagramResource by Id
        given().body(dr)
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}/resources/{diagramResourceId}", uuid, drUuid)
                .then()
                .statusCode(200)
                .body("name", is("testDiagramResource"))
                .body("resourceCatalogID", is("387585aa-8382-11ed-a1eb-0242ac120002"))
                .body("diagramID", equalTo(uuid))
                .body("uuid", equalTo(drUuid));


        //Create another DiagramResource
        final String drUuid2 = given().body(dr2)
                .contentType(ContentType.JSON)
                .when().post("/diagrams/{diagramId}/resources", uuid).getBody().as(DiagramResource.class).getUuid();

        //Get second DiagramResource by Id
        given().body(dr2)
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}/resources/{diagramResourceId}", uuid, drUuid2)
                .then()
                .statusCode(200)
                .body("name", is("testDiagramResource2"))
                .body("resourceCatalogID", is("0ab01ee0-8211-11ed-a1eb-0242ac120002"))
                .body("diagramID", equalTo(uuid))
                .body("uuid", equalTo(drUuid2));

        //Get Diagram by Id
        given().body(d)
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}", uuid)
                .then()
                .statusCode(200)
                .body("name", is("testDiagram"))
                .body("resourcesID", iterableWithSize(2))
                .body("linesID", emptyCollectionOf(Set.class))
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
     public void testDeleteDiagramWithMultipleLines() {
         ResourceCatalog rc1 = new ResourceCatalog("387585aa-8382-11ed-a1eb-0242ac120002", "ResourceCatalog1", "");
         ResourceCatalog rc2 = new ResourceCatalog("0ab01ee0-8211-11ed-a1eb-0242ac120002", "ResourceCatalog2", "");
         Diagram d = new Diagram();
         d.setName("testDiagram");

         DiagramResource dr = new DiagramResource();
         dr.setName("testDiagramResource");
         dr.setResourceCatalog(rc1);
         DiagramResource dr2 = new DiagramResource();
         dr2.setName("testDiagramResource2");
         dr2.setResourceCatalog(rc2);
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
                 .body("resourcesID", emptyCollectionOf(Set.class))
                 .body("linesID", emptyCollectionOf(Set.class))
                 .body("uuid", equalTo(uuid));

         //Create DiagramResource
         final String drUuid = given().body(dr)
                 .contentType(ContentType.JSON)
                 .when().post("/diagrams/{diagramId}/resources", uuid).getBody().as(DiagramResource.class).getUuid();

         //Get DiagramResource by Id
         given().body(dr)
                 .contentType(ContentType.JSON)
                 .when().get("/diagrams/{diagramId}/resources/{diagramResourceId}", uuid, drUuid)
                 .then()
                 .statusCode(200)
                 .body("name", is("testDiagramResource"))
                 .body("resourceCatalogID", is("387585aa-8382-11ed-a1eb-0242ac120002"))
                 .body("diagramID", equalTo(uuid))
                 .body("uuid", equalTo(drUuid));

         //Create another DiagramResource
         final String drUuid2 = given().body(dr2)
                 .contentType(ContentType.JSON)
                 .when().post("/diagrams/{diagramId}/resources", uuid).getBody().as(DiagramResource.class).getUuid();

         //Get second DiagramResource by Id
         given().body(dr2)
                 .contentType(ContentType.JSON)
                 .when().get("/diagrams/{diagramId}/resources/{diagramResourceId}", uuid, drUuid2)
                 .then()
                 .statusCode(200)
                 .body("name", is("testDiagramResource2"))
                 .body("resourceCatalogID", is("0ab01ee0-8211-11ed-a1eb-0242ac120002"))
                 .body("diagramID", equalTo(uuid))
                 .body("uuid", equalTo(drUuid2));

         //Get Diagram by Id
         given().body(d)
                 .contentType(ContentType.JSON)
                 .when().get("/diagrams/{diagramId}", uuid)
                 .then()
                 .statusCode(200)
                 .body("name", is("testDiagram"))
                 .body("resourcesID", iterableWithSize(2))
                 .body("linesID", emptyCollectionOf(Set.class))
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
         Assertions.assertEquals("uuid", lineCatalog.getUuid());
         Assertions.assertEquals("Inherit Labels", lineCatalog.getName());
         Assertions.assertEquals(1, lineCatalog.getRules().size());

         Line line = new Line();
         line.setDestination(drUuid);
         line.setSource(drUuid2);
         line.setLineCatalog(lineCatalog);

         //Create another DiagramResource
         final String lineUuid = given().body(line)
                 .contentType(ContentType.JSON)
                 .when().post("/diagrams/{diagramId}/lines", uuid).getBody().as(Line.class).getUuid();

         //Get Line by Id
         given().body(line)
                 .contentType(ContentType.JSON)
                 .when().get("/diagrams/{diagramId}/lines/{lineId}", uuid, lineUuid)
                 .then()
                 .statusCode(200)
                 .body("diagramID", equalTo(uuid))
                 .body("lineCatalogID", equalTo(lcuuid))
                 .body("source", equalTo(drUuid2))
                 .body("destination", equalTo(drUuid))
                 .body("uuid", equalTo(lineUuid));

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
                 .body("resourcesID", iterableWithSize(2))
                 .body("linesID", iterableWithSize(1))
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
        ResourceCatalog rc = new ResourceCatalog("387585aa-8382-11ed-a1eb-0242ac120002", "ResourceCatalog1", "");
        Diagram d = new Diagram();
        d.setName("testDiagram");

        DiagramResource dr = new DiagramResource();
        dr.setName("testDiagramResource");
        dr.setResourceCatalog(rc);
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
                .body("resourcesID", emptyCollectionOf(Set.class))
                .body("linesID", emptyCollectionOf(Set.class))
                .body("uuid", equalTo(uuid));

        //Create DiagramResource
        final String drUuid = given().body(dr)
                .contentType(ContentType.JSON)
                .when().post("/diagrams/{diagramId}/resources", uuid).getBody().as(DiagramResource.class).getUuid();

        //Get DiagramResource by Id
        given().body(dr)
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}/resources/{diagramResourceId}", uuid, drUuid)
                .then()
                .statusCode(200)
                .body("name", is("testDiagramResource"))
                .body("resourceCatalogID", is("387585aa-8382-11ed-a1eb-0242ac120002"))
                .body("diagramID", equalTo(uuid))
                .body("uuid", equalTo(drUuid));

        //Get Diagram by Id
        given().body(d)
                .contentType(ContentType.JSON)
                .when().get("/diagrams/{diagramId}", uuid)
                .then()
                .statusCode(200)
                .body("name", is("testDiagram"))
                .body("resourcesID", iterableWithSize(1))
                .body("linesID", emptyCollectionOf(Set.class))
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
                .body("resourcesID", emptyCollectionOf(Set.class))
                .body("linesID", emptyCollectionOf(Set.class))
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
        ResourceCatalog rc = new ResourceCatalog("387585aa-8382-11ed-a1eb-0242ac120002", "ResourceCatalog1", "");
        Diagram d = new Diagram();
        d.setName("testDiagram");

        DiagramResource dr = new DiagramResource();
        dr.setName("testDiagramResource");
        dr.setResourceCatalog(rc);

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
                .body("resourcesID", emptyCollectionOf(Set.class))
                .body("linesID", emptyCollectionOf(Set.class))
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