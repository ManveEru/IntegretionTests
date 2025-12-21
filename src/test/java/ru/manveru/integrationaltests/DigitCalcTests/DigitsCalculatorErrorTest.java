package ru.manveru.integrationaltests.DigitCalcTests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import org.junit.jupiter.api.DisplayName;

@Epic("Операции с числами")
@Feature("Подсчёт сумм цифр числа. Негативные проверки.")
public class DigitsCalculatorErrorTest extends DigitCalculatorTestBase{

    @Test
    @DisplayName("Отсутствие входного параметра")
    @Description("При отсутствии входного параметра запрос завершается с кодом 400")
    public void testMissingParameter() {
        given()
        .when()
            .get("/sum")
        .then()
            .statusCode(400);  // Bad Request - отсутствует параметр
    }

    @Test
    @DisplayName("Входной параметр не число")
    @Description("Если входной параметр не число, то запрос завершается с кодом 400")
    public void testInvalidParameterString() {
        sendBadRequest("not-a-number");
    }
    
    @Test
    @DisplayName("Входной параметр - вещественное число")
    @Description("Если входной параметр вещественное число, то запрос завершается с кодом 400")
    public void testInvalidParameterFloat() {
        sendBadRequest("17.28");
    }
    
    @Test
    @DisplayName("Входной параметр - отрицательное число")
    @Description("Если входной параметр отрицательное число, то запрос завершается с кодом 400")
    public void testInvalidParameterLessThenZero() {
        sendBadRequest("-17");
    }
}
