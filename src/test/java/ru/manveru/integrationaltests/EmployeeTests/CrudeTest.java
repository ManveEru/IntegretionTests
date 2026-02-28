package ru.manveru.integrationaltests.EmployeeTests;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import java.sql.SQLException;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.manveru.integrationaltests.BaseDbTest;
import static ru.manveru.integrationaltests.BaseTest.apiHelper;
import ru.manveru.integrationaltests.DTO.Employee;
import ru.manveru.integrationaltests.DTO.tables.pojos.employeesPojo;
import ru.manveru.integrationaltests.Helpers.RequestParams;

@Epic("Кадровый учёт")
@Feature("Сотрудники")
@DisplayName("Работа с таблицей Employees")
public class CrudeTest extends BaseDbTest{
    
    @Test
    @DisplayName("Выборка по id")
    @Description("Запрос данных о сотруднике по его id")
    @Tag("Regress")
    public void testGetById() throws SQLException{
        //Employee inDb = employeeRepo.findById(employeeRepo.ceate(new Employee("Test", "Testsonn", "JDBC", 1000)));
        employeesPojo inDb = employeeRepo.create(new employeesPojo(null, "Test", "Testsonn", "JDBC", 1000));
        RequestParams params = RequestParams.get("/" + inDb.getId()).build();
        
        Response response = sendRequestStep(params, 200);
        employeesPojo fromRequest = response
                .then()
                .extract()
                .as(employeesPojo.class);

        Allure.step("Записи в БД и в ответе идентичны", () -> assertThat(inDb).isEqualTo(fromRequest));
        
        employeeRepo.deleteById(inDb.getId());
        //employeeRepo.delete(inDb.getId());
    }
    
    @Test
    @DisplayName("Общая выборка")
    @Description("Запрос всех записей")
    @Tag("Regress")
    public void testGet() throws SQLException{
        List<employeesPojo> inDb = employeeRepo.findAll();
        
        RequestParams params = RequestParams.get("").build();
        Response response = sendRequestStep(params, 200);
        List<employeesPojo> fromRequest = response
                .then()
                .extract()
                .as(new TypeRef<List<employeesPojo>>(){});

        Allure.step("Сравнение выборки по БД и из запроса", () -> assertThat(inDb).containsExactlyInAnyOrderElementsOf(fromRequest));
    }
    
    @Test
    @DisplayName("Создание записи")
    @Description("Запрос создания данных, в ответе ожидается созданный объект")
    @Tag("Regress")
    public void testCreate() throws SQLException{
        List<employeesPojo> inDbBefore = employeeRepo.findAll();
        Employee newEmployee = new Employee("Frumpel", "Devol", "Trade", 1000000);
        RequestParams params = RequestParams.post("").withBody(newEmployee).build();
        
        Response response = sendRequestStep(params, 201);
        employeesPojo fromRequest = response
                .then()
                .extract()
                .as(employeesPojo.class);
        List<employeesPojo> inDbAfter = employeeRepo.findAll();
        employeesPojo inDb = employeeRepo.findById(fromRequest.getId()).orElseThrow(() -> new AssertionError("Запись не найдена в БД после создания"));
        
        assertAll(
            () -> Allure.step("Количество в БД", () -> assertThat(inDbAfter.size()).isGreaterThan(inDbBefore.size())),
            () -> Allure.step("Наличие новой записи в БД", () -> assertThat(inDb).isNotNull()),
            () -> Allure.step("Записи в БД и в ответе идентичны", () -> assertThat(inDb).isEqualTo(fromRequest))
        );
        employeeRepo.deleteById(inDb.getId());
    }
    
    @Test
    @DisplayName("Изменение записи")
    @Description("Запрос изменения данных, в ответе ожидается изменённый объект")
    @Tag("Regress")
    public void testModify() throws SQLException{
        //Employee oldEmployee = employeeRepo.findById(employeeRepo.ceate(new Employee("Test", "Testsonn", "JDBC", 1000)));
        employeesPojo testEmployee = employeeRepo.create(new employeesPojo(null, "Test", "Testsonn", "JDBC", 1000));
        testEmployee.setName("New Name");
        RequestParams params = RequestParams.put("/" + testEmployee.getId()).withBody(testEmployee).build();
        
        Response response = sendRequestStep(params, 200);
        employeesPojo fromRequest = response
                .then()
                .extract()
                .as(employeesPojo.class);
        employeesPojo inDb = employeeRepo.findById(testEmployee.getId()).orElseThrow(() -> new AssertionError("Запись не найдена в БД после создания"));
        
        assertAll(() -> Allure.step("Записи в БД и в ответе идентичны", () -> assertThat(inDb).isEqualTo(fromRequest)),
            () -> Allure.step("Запись в БД изменена", () -> assertThat(inDb).isEqualTo(testEmployee))
        );
        employeeRepo.deleteById(testEmployee.getId());
    }
    
    @Test
    @DisplayName("Удаление записи")
    @Description("Запрос удаления данных о сотруднике по его id")
    @Tag("Regress")
    public void testDelete() throws SQLException{
        //Employee inDb = employeeRepo.findById(employeeRepo.ceate(new Employee("Test", "Testsonn", "JDBC", 1000)));
        employeesPojo inDb = employeeRepo.create(new employeesPojo(null, "Test", "Testsonn", "JDBC", 1000));
        RequestParams params = RequestParams.delete("/" + inDb.getId()).build();
        
        sendRequestStep(params, 204);
        
        employeesPojo inDbAfter = employeeRepo.findById(inDb.getId()).orElse(null);
        Allure.step("Запись удалена из БД", () -> assertThat(inDbAfter).isNull());
    }
    
    @Step("Отправка запроса")
    private Response sendRequestStep(RequestParams params, int statusCode){
        Response response = apiHelper.sendRequest(params);
        Allure.step("Проверка статуса ответа", () -> response.then().statusCode(statusCode));
        return response;
    }
}
