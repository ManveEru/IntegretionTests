package ru.manveru.integrationaltests.DigitCalcTests;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.BeforeAll;
import ru.manveru.integrationaltests.model.DigitSumResponse;

public class DigitCalculatorTestBase {
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/digits";
    }
    
    protected DigitSumResponse sendRequest(String param){
    return given()
            .param("number", param)
        .when()
            .get("/sum")
        .then()
            .statusCode(200)
            .extract()
            .as(DigitSumResponse.class);
    }
    
    protected void sendBadRequest(String param){
    given()
        .param("number", param)
    .when()
        .get("/sum")
    .then()
       .statusCode(400);
    }
    
}
