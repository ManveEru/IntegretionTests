package ru.manveru.integrationaltests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import ru.manveru.integrationaltests.Helpers.RestApiHelper;

public class BaseDigTest {
    public static RestApiHelper apiHelper;
    
    @BeforeAll
    public static void setup() {
        apiHelper = RestApiHelper.getInstance();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/digits";
    }
}
