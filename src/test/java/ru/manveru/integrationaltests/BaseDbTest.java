package ru.manveru.integrationaltests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import ru.manveru.integrationaltests.DTO.Employee;
import ru.manveru.integrationaltests.Repositories.AnnotatedJdbcRepository;
import ru.manveru.integrationaltests.Repositories.EmployeeJooqRepository;

public class BaseDbTest extends BaseTest{
    //public static EmployeeJdbcRepository employeeRepo = EmployeeJdbcRepository.getInstance(connectionPool);
    //public GenericJdbcRepository<Employee> employeeRepo = new GenericJdbcRepository(Employee.class, "employees", connectionPool);
    //public AnnotatedJdbcRepository<Employee> employeeRepo = factory.getRepository(Employee.class);//new AnnotatedJdbcRepository(Employee.class, connectionPool);
    public EmployeeJooqRepository employeeRepo = factory.getEmployeeRepository();
    @BeforeAll
    public static void setup() {
        RestAssured.basePath = "/api/employees";
    }
}
