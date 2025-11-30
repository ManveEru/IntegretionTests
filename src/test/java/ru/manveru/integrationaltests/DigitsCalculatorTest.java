package ru.manveru.integrationaltests;

import ru.manveru.integrationaltests.model.DigitSumResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class DigitsCalculatorTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/digits";
    }

    @Test
    public void testDigitsSumWithPOJO() {
        DigitSumResponse response = given()
            .param("number", 12345)
        .when()
            .get("/sum")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .extract()
            .as(DigitSumResponse.class);

        // Проверяем через JUnit assertions
        assertEquals(15, response.getTotalSum(), "Total sum should be 15");
        assertEquals(9, response.getOddSum(), "Odd sum should be 9");
        assertEquals(5, response.getMaxDigit(), "Max digit should be 5");
    }

    @Test
    public void testDigitsSumWithMixedDigits() {
        DigitSumResponse response = given()
            .param("number", 246813579)
        .when()
            .get("/sum")
        .then()
            .statusCode(200)
            .extract()
            .as(DigitSumResponse.class);

        assertEquals(45, response.getTotalSum());  // 2+4+6+8+1+3+5+7+9 = 45
        assertEquals(25, response.getOddSum());    // 1+3+5+7+9 = 25
        assertEquals(9, response.getMaxDigit());
    }
}