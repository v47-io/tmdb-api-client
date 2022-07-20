package io.v47.tmdb.http.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class MovieInfoResourceTest {
    @Test
    public void testGetMovieInfo() {
        MovieInfoResource.MovieInfo movieInfo = given().when()
                                                       .get("/movie/284053")
                                                       .then()
                                                       .statusCode(HttpStatus.SC_OK)
                                                       .extract()
                                                       .body()
                                                       .as(MovieInfoResource.MovieInfo.class);

        Assertions.assertEquals(new MovieInfoResource.MovieInfo("tt3501632", "Thor: Ragnarok", 2017), movieInfo);
    }
}
