package ru.manveru.integrationaltests.DigitCalcTests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;

public class DigitsCalculatorErrorTest extends DigitCalculatorTestBase{

    @Test
    public void testMissingParameter() {
        given()
        .when()
            .get("/sum")
        .then()
            .statusCode(400);  // Bad Request - отсутствует параметр
    }

    @Test
    public void testInvalidParameterString() {
        sendBadRequest("not-a-number");
    }
    
    @Test
    public void testInvalidParameterFloat() {
        sendBadRequest("17.28");
    }
    
    @Test
    public void testInvalidParameterLessThenZero() {
        sendBadRequest("-17");
    }
}
