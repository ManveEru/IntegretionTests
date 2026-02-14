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
import org.junit.jupiter.api.extension.ExtendWith;
import ru.manveru.integrationaltests.BaseTest;
import ru.manveru.integrationaltests.Extentions.LoggingExtension;

@Epic("Операции с числами")
@Feature("Подсчёт сумм цифр числа. Негативные проверки.")
@ExtendWith(LoggingExtension.class)
public class DigitsCalculatorErrorTest extends BaseTest{

    @Test
    @DisplayName("Отсутствие входного параметра")
    @Description("При отсутствии входного параметра запрос завершается с кодом 400")
    public void testMissingParameter() {
        Response response = apiHelper.sendGetRequest("/sum", Map.of());
        Allure.step("Проверка статуса ответа", () -> response.then().statusCode(400));
    }

    @Test
    @DisplayName("Входной параметр не число")
    @Description("Если входной параметр не число, то запрос завершается с кодом 400")
    public void testInvalidParameterString() {
        sendRequestStep("not-a-number");
    }
    
    @Test
    @DisplayName("Входной параметр - вещественное число")
    @Description("Если входной параметр вещественное число, то запрос завершается с кодом 400")
    public void testInvalidParameterFloat() {
        sendRequestStep("17.28");
    }
    
    @Test
    @DisplayName("Входной параметр - отрицательное число")
    @Description("Если входной параметр отрицательное число, то запрос завершается с кодом 400")
    public void testInvalidParameterLessThenZero() {
        sendRequestStep("-17");
    }
    
    @Step("Отправка запроса")
    private void sendRequestStep(String requestParams){
        
        Response response = apiHelper.sendGetRequest("/sum", Map.of("number", requestParams));
        Allure.step("Проверка статуса ответа", () -> response.then().statusCode(400));
    }
}
