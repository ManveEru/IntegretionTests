package ru.manveru.integrationaltests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseDigitTest extends BaseTest{
    
    @BeforeAll
    public static void setupDigitTests(){
        RestAssured.basePath = "/digits";
    }
    
}
