package ru.manveru.integrationaltests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;

public class DigitsCalculatorErrorTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/digits";
    }

    @Test
    public void testMissingParameter() {
        given()
        .when()
            .get("/sum")
        .then()
            .statusCode(400);  // Bad Request - отсутствует параметр
    }

    @Test
    public void testInvalidParameter() {
        given()
            .param("number", "not-a-number")
        .when()
            .get("/sum")
        .then()
            .statusCode(400);  // Bad Request - неверный формат
    }
}
