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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.manveru.integrationaltests.BaseDigitTest;
import ru.manveru.integrationaltests.Helpers.RequestParams;

@Epic("Операции с числами")
@Feature("Подсчёт сумм цифр числа. Негативные проверки.")
public class DigitsCalculatorErrorTest extends BaseDigitTest{

    @Test
    @DisplayName("Отсутствие входного параметра")
    @Description("При отсутствии входного параметра запрос завершается с кодом 400")
    public void testMissingParameter() {
        RequestParams params = RequestParams.get("/sum")
                .withQueryParams(Map.of())
                .build();
        Response response = apiHelper.sendRequest(params);
        Allure.step("Проверка статуса ответа", () -> response.then().statusCode(400));
    }

    @ParameterizedTest(name = "[{index}] {1}")
    @DisplayName("Параметризированный тест сумм")
    @CsvSource({"not-a-number, Not a number test",
        "17.28, Float number test", 
        "-17, Negative number test"})
    public void testInvalidParameters(String param, String description) {
        sendRequestStep(param);
    }
    
    @Step("Отправка запроса")
    private void sendRequestStep(String requestParams){
        RequestParams params = RequestParams.get("/sum")
                .withQueryParams(Map.of("number", requestParams))
                .build();
        Response response = apiHelper.sendRequest(params);
        Allure.step("Проверка статуса ответа", () -> response.then().statusCode(400));
    }
}
