package ru.manveru.integrationaltests.Helpers;

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
    
    private RestApiHelper(){logger.info("\n**********Helper created.***************\n");};
    
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
        logger.debug("Sent request on " + endpoint);
        return request.log().uri().when().get(endpoint);
    }
}
