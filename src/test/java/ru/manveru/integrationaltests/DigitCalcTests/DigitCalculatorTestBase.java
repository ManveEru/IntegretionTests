package ru.manveru.integrationaltests.DigitCalcTests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import ru.manveru.integrationaltests.Helpers.RestApiHelper;

public class DigitCalculatorTestBase {
    public static RestApiHelper apiHelper;
    
    @BeforeAll
    public static void setup() {
        apiHelper = new RestApiHelper();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/digits";
    }
    
}
