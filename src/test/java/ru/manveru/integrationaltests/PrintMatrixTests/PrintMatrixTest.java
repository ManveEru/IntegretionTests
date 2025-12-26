package ru.manveru.integrationaltests.PrintMatrixTests;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.manveru.integrationaltests.BaseDigTest;

@Epic("Операции с числами")
@Feature("Вывод чисел построчно")
public class PrintMatrixTest extends BaseDigTest{
    
    @ParameterizedTest(name = "[{index}] {2}")
    @DisplayName("Параметризированный тест")
    @MethodSource("testDataProvider")
    @Description("Проверка обработки различного количества параметров с выводом в одну строку")
    @Tag("parametrized")
    @Tag("Regress")
    public void testParametrizedParams(
                Map<String, String> requestParams,
                String expectedString,
                String testDescription) {
        logger.info("START (" + testDescription + ")");
        //Data
        //prepareed in testDataProvider
        
        //Actions
        logger.debug("Send request");
        List<String> response = sendRequestStep(requestParams);

        //Assertions
        logger.debug("Make assertions");
        Allure.step("Проверка ответа запроса", () -> assertEquals(expectedString, response.get(0), testDescription));
    }
  
    @Test
    @DisplayName("Количество символов в строке")
    @Description("Проверка обработки одного параметра: количество цифр в строке")
    @Tag("Regress")
    public void testPerLineParams() {
        logger.info("START");
        //Data
        logger.debug("Make data for request");
        Map<String, String> requestParams = Map.of("per_line", "20");
        
        //Actions
        logger.debug("Send request");
        List<String> response = sendRequestStep(requestParams);
            
        //Assertions
        logger.debug("Make assertions");
        Allure.step("Проверка ответа запроса", () -> assertAll(
            () -> Allure.step("В первой строке цифры от 1 до 20", () -> assertEquals("1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20", response.get(0))),
            () -> Allure.step("Во второй строке цифры от 21 до 30", () -> assertEquals("21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40", response.get(1))),
            () -> Allure.step("В третьей строке цифры от 41 до 60", () -> assertEquals("41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59 60", response.get(2))),
            () -> Allure.step("В четвёртой строке цифры от 61 до 80", () -> assertEquals("61 62 63 64 65 66 67 68 69 70 71 72 73 74 75 76 77 78 79 80", response.get(3))),
            () -> Allure.step("В пятой строке цифры от 81 до 100", () -> assertEquals("81 82 83 84 85 86 87 88 89 90 91 92 93 94 95 96 97 98 99 100", response.get(4)))
        ));
    }
    
    @Test
    @DisplayName("Параметры по умолчанию")
    @Description("Проверка обработки запроса без параметров")
    @Tag("Smoke")
    
    public void testNoParams() {
        logger.info("START");
        //Data
        logger.debug("Make data for request");
        Map<String, String> requestParams = Map.of();
        
        //Actions
        logger.debug("Send request");
        List<String> response = sendRequestStep(requestParams);
            
        //Assertions
        logger.debug("Make assertions");
        Allure.step("Проверка ответа запроса", () -> assertAll(
            () -> Allure.step("В первой строке цифры от 1 до 10", () -> assertEquals("1 2 3 4 5 6 7 8 9 10", response.get(0))),
            () -> Allure.step("Во второй строке цифры от 11 до 20", () -> assertEquals("11 12 13 14 15 16 17 18 19 20", response.get(1))),
            () -> Allure.step("В третьей строке цифры от 21 до 30", () -> assertEquals("21 22 23 24 25 26 27 28 29 30", response.get(2))),
            () -> Allure.step("В четвёртой строке цифры от 31 до 40", () -> assertEquals("31 32 33 34 35 36 37 38 39 40", response.get(3))),
            () -> Allure.step("В пятой строке цифры от 41 до 50", () -> assertEquals("41 42 43 44 45 46 47 48 49 50", response.get(4))),
            () -> Allure.step("В шестой строке цифры от 51 до 60", () -> assertEquals("51 52 53 54 55 56 57 58 59 60", response.get(5))),
            () -> Allure.step("В седьмой строке цифры от 61 до 70", () -> assertEquals("61 62 63 64 65 66 67 68 69 70", response.get(6))),
            () -> Allure.step("В восьмой строке цифры от 71 до 80", () -> assertEquals("71 72 73 74 75 76 77 78 79 80", response.get(7))),
            () -> Allure.step("В девятой строке цифры от 81 до 90", () -> assertEquals("81 82 83 84 85 86 87 88 89 90", response.get(8))),
            () -> Allure.step("В десятой строке цифры от 91 до 100", () -> assertEquals("91 92 93 94 95 96 97 98 99 100", response.get(9)))
                ));
    }
    
    @Test
    @DisplayName("Дополнение нулями")
    @Description("Проверка дополнения последней строки нулями, вывод в две строки")
    @Tag("Smoke")
    public void testTwoLinesZeroBrace() {
        logger.info("START");
        //Data
        logger.debug("Make data for request");
        Map<String, String> requestParams = Map.of(
                "start", "2",
                "end", "6",
                "per_line", "3");
        
        //Actions
        logger.debug("Send request");
        List<String> response = sendRequestStep(requestParams);

        //Assertions
        logger.debug("Make assertions");
        Allure.step("Проверка ответа запроса", () -> assertAll(
        () -> Allure.step("Вторая строка дополнена нулями", () -> assertEquals("5 6 0", response.get(1))),
        () -> Allure.step("В ответе 2 строки", () -> assertEquals(2, response.size()))
        ));
    }
    
    @Step("Отправка запроса")
    private List<String> sendRequestStep(Map<String, String> requestParams){
        Response response = apiHelper.sendGetRequest("/matrix", requestParams);
        Allure.step("Проверка статуса ответа", () -> response.then().statusCode(200));
        return response
                .then()
                .extract()
                .as(new TypeRef<List<String>>(){});
    }
    
    @DisplayName("Провайдер данных для параметризированных тестов")
    static Stream<Arguments> testDataProvider() {
        return Stream.of(
            arguments(
                Map.of("start", "96", "per_line", "5"),
                "96 97 98 99 100",
                "Print digits from start=96 to end=Default with 5 digits per_line"),
            arguments(
                Map.of("start", "2", "end", "11"),
                "2 3 4 5 6 7 8 9 10 11",
                "Print digits from start=2 to end=11 with Default digits per_line"),
            arguments(
                Map.of("end", "5", "per_line", "5"),
                "1 2 3 4 5",
                "Print digits from start=Default to end=5 with 5 digits per_line"),
            arguments(
                Map.of("start", "91"),
                "91 92 93 94 95 96 97 98 99 100",
                "Print digits from start=91 to end=Default with Default digits per_line"),
            arguments(
                Map.of("end", "10"),
                "1 2 3 4 5 6 7 8 9 10",
                "Print digits from start=Default to end=10 with Default digits per_line"),
            arguments(
                Map.of("start", "2", "end", "6", "per_line", "5"),
                "2 3 4 5 6",
                "Print digits from start=2 to end=6 with 5 digits per_line"));
    }
}
