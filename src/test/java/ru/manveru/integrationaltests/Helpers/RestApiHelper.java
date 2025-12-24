package ru.manveru.integrationaltests.Helpers;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;
import ru.manveru.integrationaltests.model.DigitSumResponse;

public class RestApiHelper {
    public Response sendGetRequest (String endpoint, Map<String, String> requestParams) {
        RequestSpecification request = given();
        if (requestParams != null && !requestParams.isEmpty())
            for (Map.Entry<String, String> param : requestParams.entrySet()){
                request.param(param.getKey(), param.getValue());
            }
        return request.when().get(endpoint);
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
