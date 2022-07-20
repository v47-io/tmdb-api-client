package io.v47.tmdb.http.quarkus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.kotlin.ExtensionsKt;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.internal.mapping.Jackson2Mapper;
import io.v47.tmdb.http.tck.TckResult;
import io.v47.tmdb.http.tck.TckResultDeserializer;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class TckResourceTest {
    @Test
    public void runTckTest() {
        ObjectMapper objectMapper = new ObjectMapper();
        ExtensionsKt.registerKotlinModule(objectMapper);
        objectMapper.registerModule(new SimpleModule().addDeserializer(TckResult.class, new TckResultDeserializer()));

        TckResult result = given().when()
                                  .get("/run-tck-test")
                                  .then()
                                  .statusCode(HttpStatus.SC_OK)
                                  .extract()
                                  .body()
                                  .as(TckResult.class, new Jackson2Mapper((cls, charset) -> objectMapper));

        if (result instanceof TckResult.Failure) {
            TckResult.Failure failureResult = (TckResult.Failure) result;

            failureResult.getFailedTests().forEach(failedTest -> {
                Assertions.assertEquals(failedTest.getExpectedValue(),
                                        failedTest.getActualValue(),
                                        "Test " + failedTest.getName() + " failed");
            });
        }
    }
}
