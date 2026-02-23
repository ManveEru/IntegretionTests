package ru.manveru.integrationaltests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import ru.manveru.integrationaltests.DTO.Employee;
import ru.manveru.integrationaltests.Repositories.EmployeeJdbcRepository;
import ru.manveru.integrationaltests.Repositories.GenericJdbcRepository;

public class BaseDbTest extends BaseTest{
    //public static EmployeeJdbcRepository employeeRepo = EmployeeJdbcRepository.getInstance(connectionPool);
    public GenericJdbcRepository<Employee> employeeRepo = new GenericJdbcRepository(Employee.class, "employees", connectionPool);
    
    @BeforeAll
    public static void setup() {
        RestAssured.basePath = "/api/employees";
    }
}
