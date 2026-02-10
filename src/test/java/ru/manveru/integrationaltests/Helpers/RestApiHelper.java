package ru.manveru.integrationaltests.Helpers;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.manveru.integrationaltests.Extentions.LoggingExtension;

public class RestApiHelper {
    private static RestApiHelper instance;
    private static final Logger logger = LoggerFactory.getLogger(LoggingExtension.class);
    
    private RestApiHelper(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/digits";
        logger.info("**********Helper created.***************");
    };
    
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
        logger.debug("Sent request to {} with params {}", endpoint, requestParams);
        return request.log().uri().log().params().when().get(endpoint);
    }
}
