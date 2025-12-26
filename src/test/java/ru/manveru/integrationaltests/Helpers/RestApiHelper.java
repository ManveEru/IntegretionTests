package ru.manveru.integrationaltests.Helpers;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;

public class RestApiHelper {
    private static RestApiHelper instance;
    
    private RestApiHelper(){System.out.println("\n**********Helper created.***************\n");};
    
    public static RestApiHelper getInstance(){
        if (instance == null)
            instance = new RestApiHelper();
        return instance;
    }
    
    public Response sendGetRequest (String endpoint, Map<String, String> requestParams) {
        RequestSpecification request = given();
        if (requestParams != null && !requestParams.isEmpty())
            for (Map.Entry<String, String> param : requestParams.entrySet()){
                request.param(param.getKey(), param.getValue());
            }
        return request.log().uri().when().get(endpoint);
    }
}
