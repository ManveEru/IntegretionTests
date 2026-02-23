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
        logger.info("**********Helper created.***************");
    };
    
    public static RestApiHelper getInstance(){
        if (instance == null)
            instance = new RestApiHelper();
        return instance;
    }
    
    /**
     * Send REST API request
     * @param requestParams requests parameters (body, endpoint, query params, type)
     * @return jbject with response
     */
    public Response sendRequest (RequestParams requestParams) {
        RequestSpecification request = given();
        
        addQueryParams(request, requestParams.getQueryParams());
        if (requestParams.getBody() != null && supportsBody(requestParams.getType())) {
            request.body(requestParams.getBody());
            request.contentType("application/json");
        }
        logRequest(requestParams);
        switch (requestParams.getType()) {
            case GET -> {
                return request.log().uri().log().params().when()
                        .get(requestParams.getEndpoint());
            }
            case POST -> {
                return request.log().uri().log().body().when()
                        .post(requestParams.getEndpoint());
            }
            case PUT -> {
                return request.log().uri().log().body().when()
                        .put(requestParams.getEndpoint());
            }
            case PATCH -> {
                return request.log().uri().log().body().when()
                        .patch(requestParams.getEndpoint());
            }
            case DELETE -> {
                return request.log().uri().log().all().when()
                        .delete(requestParams.getEndpoint());
            }
            default -> throw new IllegalArgumentException("Unsupported request type: " + requestParams.getType());
        }
    }
    
    // Add path-parameters to request
    private void addQueryParams(RequestSpecification request, Map<String, String> queryParams) {
        if (queryParams != null && !queryParams.isEmpty()) {
            for (Map.Entry<String, String> param : queryParams.entrySet()) {
                request.param(param.getKey(), param.getValue());
            }
        }
    }
    
    // Check support body by request
    private boolean supportsBody(RequestType type) {
        return type == RequestType.POST || 
               type == RequestType.PUT || 
               type == RequestType.PATCH;
    }
    
    private void logRequest(RequestParams requestParams) {
        logger.debug("Sending {} request to {}", 
                    requestParams.getType(), 
                    requestParams.getEndpoint());
        
        if (requestParams.getQueryParams() != null && !requestParams.getQueryParams().isEmpty()) {
            logger.debug("Query params: {}", requestParams.getQueryParams());
        }
        
        if (requestParams.getBody() != null && supportsBody(requestParams.getType())) {
            logger.debug("Request body: {}", requestParams.getBody());
        }
    }
}
