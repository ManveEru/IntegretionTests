package ru.manveru.integrationaltests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import ru.manveru.integrationaltests.Repositories.EmployeeJdbcRepository;

public class BaseDbTest extends BaseTest{
    public static EmployeeJdbcRepository employeeRepo;
    
    @BeforeAll
    public static void setup() {
        RestAssured.basePath = "/api/employees";
        employeeRepo = EmployeeJdbcRepository.getInstance();
    }
}
