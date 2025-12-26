package ru.manveru.integrationaltests.DigitCalcTests;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;

import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import ru.manveru.integrationaltests.BaseDigTest;

@Epic("Операции с числами")
@Feature("Подсчёт сумм цифр числа. Негативные проверки.")
public class DigitsCalculatorErrorTest extends BaseDigTest{

    @Test
    @DisplayName("Отсутствие входного параметра")
    @Description("При отсутствии входного параметра запрос завершается с кодом 400")
    public void testMissingParameter() {
        logger.info("START");
        Response response = apiHelper.sendGetRequest("/sum", Map.of());
        Allure.step("Проверка статуса ответа", () -> response.then().statusCode(400));
    }

    @Test
    @DisplayName("Входной параметр не число")
    @Description("Если входной параметр не число, то запрос завершается с кодом 400")
    public void testInvalidParameterString() {
        logger.info("START");
        sendRequestStep("not-a-number");
    }
    
    @Test
    @DisplayName("Входной параметр - вещественное число")
    @Description("Если входной параметр вещественное число, то запрос завершается с кодом 400")
    public void testInvalidParameterFloat() {
        logger.info("START");
        sendRequestStep("17.28");
    }
    
    @Test
    @DisplayName("Входной параметр - отрицательное число")
    @Description("Если входной параметр отрицательное число, то запрос завершается с кодом 400")
    public void testInvalidParameterLessThenZero() {
        logger.info("START");
        sendRequestStep("-17");
    }
    
    @Step("Отправка запроса")
    private void sendRequestStep(String requestParams){
        
        Response response = apiHelper.sendGetRequest("/sum", Map.of("number", requestParams));
        Allure.step("Проверка статуса ответа", () -> response.then().statusCode(400));
    }
}
