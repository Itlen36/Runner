package com.aldebaran.runner;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class RunnerMainTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/runner")
          .then()
             .statusCode(200)
             .body(is("hello"));
    }

}