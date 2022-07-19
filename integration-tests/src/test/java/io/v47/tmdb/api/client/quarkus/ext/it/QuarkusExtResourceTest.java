package io.v47.tmdb.api.client.quarkus.ext.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class QuarkusExtResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/quarkus-ext")
                .then()
                .statusCode(200)
                .body(is("Hello quarkus-ext"));
    }
}
