package ru.manveru.integrationaltests.EmployeeTests;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.manveru.integrationaltests.BaseDbTest;
import static ru.manveru.integrationaltests.BaseTest.apiHelper;
import ru.manveru.integrationaltests.DTO.Employee;

@Epic("Кадровый учёт")
@Feature("Сотрудники")
public class CrudeTest extends BaseDbTest{
    
    @Test
    @DisplayName("Выборка по id")
    @Description("Запрос данных о сотруднике по его id")
    @Tag("Regress")
    public void testGetById() throws SQLException{
        Employee inDb = employeeRepo.findById(employeeRepo.ceate(new Employee("Test", "Testsonn", "JDBC", 1000)));
        
        Employee response = sendRequestStep(String.valueOf(inDb.getId()));

        responseAsserts(inDb,response);
        
        employeeRepo.delete(inDb.getId());
    }
    
    @Step("Отправка запроса")
    private Employee sendRequestStep(String id){
        
        String endpoint = id != null ? "/" + id : "";
        Response response = apiHelper.sendGetRequest(endpoint, null);
        Allure.step("Проверка статуса ответа", () -> response.then().statusCode(200));
        return response
                .then()
                .extract()
                .as(Employee.class);
    }
    
    @Step("Проверка ответа на запрос")
    private void responseAsserts(Employee inDb, Employee response) {
        assertAll(
        () -> Allure.step("Имя", () -> assertEquals(inDb.getName(), response.getName())),
        () -> Allure.step("Фамилия", () -> assertEquals(inDb.getSurname(), response.getSurname())),
        () -> Allure.step("Отдел", () -> assertEquals(inDb.getDepartment(), response.getDepartment())),
        () -> Allure.step("ЗП", () -> assertEquals(inDb.getSalary(), response.getSalary()))
        );
    }
}
