package ru.manveru.integrationaltests.DigitCalcTests;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.Map;
import ru.manveru.integrationaltests.model.DigitSumResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

@Epic("Операции с числами")
@Feature("Подсчёт сумм цифр числа")
public class DigitsCalculatorTest extends DigitCalculatorTestBase{

    @Test
    @DisplayName("Основной вариант запроса")
    @Description("Подсчёт суммы цифр числа, суммы нечётных цифр числа, поиск максимальной цифры числа")
    @Tag("Regress")
    public void testDigitsSum() {
        DigitSumResponse response = sendRequestStep("12345");

        responseAsserts(15, 9, 5, response);
    }

    @Test
    @DisplayName("Обработка 0")
    @Description("Корректная работа алгоритма для 0")
    @Tag("Regress")
    public void testDigitsSumOfZero() {
        DigitSumResponse response = sendRequestStep("0");

         responseAsserts(0, 0, 0, response);
    }
    
    @Test
    @DisplayName("Обработка одно циферного числа")
    @Description("Корректная работа алгоритма для числа из одной цифры")
    public void testOddDigitsSum() {
        DigitSumResponse response = sendRequestStep("7");

        responseAsserts(7, 7, 7, response);
    }
    
    @Test
    @DisplayName("Одноциферное чётное число")
    @Description("Для числа из одной чётной цифры сумма нечётных цифр = 0")
    public void testEvenDigitsSum() {
        DigitSumResponse response = sendRequestStep("8");

        responseAsserts(8, 0, 8, response);
    }
    
    @Step("Отправка запроса")
    private DigitSumResponse sendRequestStep(String requestParams){
        
        Response response = apiHelper.sendGetRequest("/sum", Map.of("number", requestParams));
        Allure.step("Проверка статуса ответа", () -> response.then().statusCode(200));
        return response
                .then()
                .extract()
                .as(DigitSumResponse.class);
    }
    
    @Step("Проверка ответа на запрос")
    private void responseAsserts(int sum, int oddSum, int maxDigit, DigitSumResponse response) {
        assertAll(
        () -> Allure.step("Сумма цифр числа", () -> assertEquals(sum, response.getTotalSum())),
        () -> Allure.step("Сумма нечётных цифр числа", () -> assertEquals(oddSum, response.getOddSum())),
        () -> Allure.step("Наибольшая цифра числа", () -> assertEquals(maxDigit, response.getMaxDigit()))
        );
    }
}