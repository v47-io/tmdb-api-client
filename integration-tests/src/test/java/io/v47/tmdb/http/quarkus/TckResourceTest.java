/*
 * The Clear BSD License
 *
 * Copyright (c) 2022, the tmdb-api-client authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted (subject to the limitations in the disclaimer
 * below) provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *
 *      * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 *      * Neither the name of the copyright holder nor the names of its
 *      contributors may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
 * THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package io.v47.tmdb.http.quarkus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.kotlin.ExtensionsKt;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.internal.mapping.Jackson2Mapper;
import io.v47.tmdb.http.quarkus.jackson.TckResultDeserializer;
import io.v47.tmdb.http.tck.TckResult;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class TckResourceTest {
    private static Jackson2Mapper objectMapper;

    @BeforeAll
    public static void initObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        ExtensionsKt.registerKotlinModule(objectMapper);
        objectMapper.registerModule(new SimpleModule().addDeserializer(TckResult.class, new TckResultDeserializer()));

        TckResourceTest.objectMapper = new Jackson2Mapper(((cls, charset) -> objectMapper));
    }

    @Test
    public void runTckTest() {
        TckResult result = given().when()
                                  .get("/run-tck-test")
                                  .then()
                                  .statusCode(HttpStatus.SC_OK)
                                  .extract()
                                  .body()
                                  .as(TckResult.class, objectMapper);

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
