package org.acme.opentracing.services;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class IngestorTest {

    @Test
    void testIngestEndpoint() {
        given()
                .when().get("/ingestor")
                .then()
                .statusCode(200)
                .body(containsString("organisatie"));
    }

}
