package ru.manveru.integrationaltests.PrintMatrixTests;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.common.mapper.TypeRef;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;

public class PrintMatrixTestBase {
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/digits";
    }
    
    protected List<String> sendRequest (Map<String, Integer> requestParams) {
        RequestSpecification request = given();
        if (requestParams != null && !requestParams.isEmpty())
            for (Map.Entry<String, Integer> param : requestParams.entrySet()){
                request.param(param.getKey(), param.getValue());
            }
        return request
                .when()
                    .get("/matrix")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(new TypeRef<List<String>>(){});
    }
    
}
