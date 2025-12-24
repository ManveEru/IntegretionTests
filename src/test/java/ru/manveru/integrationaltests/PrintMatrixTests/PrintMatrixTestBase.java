package ru.manveru.integrationaltests.PrintMatrixTests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import ru.manveru.integrationaltests.Helpers.RestApiHelper;

public class PrintMatrixTestBase {
    public static RestApiHelper apiHelper;
    
    @BeforeAll
    public static void setup() {
        apiHelper = new RestApiHelper();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/digits";
    }
    
    
    
}
