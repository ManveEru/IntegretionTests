package ru.manveru.integrationaltests.Repositories;

import java.util.List;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.SQL;
import org.jooq.impl.DSL;
import ru.manveru.integrationaltests.DTO.tables.daos.employeesDao;
import ru.manveru.integrationaltests.DTO.tables.employees;
import ru.manveru.integrationaltests.DTO.tables.pojos.employeesPojo;
import ru.manveru.integrationaltests.DTO.tables.records.employeesRecord;

public class EmployeeJooqRepository extends BaseJooqRepository<employeesRecord, employeesPojo, Integer> {
 public EmployeeJooqRepository(Configuration configuration) {
        super(configuration, new employeesDao(configuration), employees.EMPLOYEES);
    }
 
 public List<employeesPojo> search(String name, String surname, String department, 
                                     Integer minSalary, Integer maxSalary) {
        Condition condition = DSL.trueCondition();
        
        if (name != null && !name.isEmpty()) {
            condition = condition.and(employees.EMPLOYEES.NAME.containsIgnoreCase(name));
        }
        if (surname != null && !surname.isEmpty()) {
            condition = condition.and((SQL) employees.EMPLOYEES.SURNAME.containsIgnoreCase(surname));
        }
        if (department != null && !department.isEmpty()) {
            condition = condition.and(employees.EMPLOYEES.DEPARTMENT.eq(department));
        }
        if (minSalary != null) {
            condition = condition.and((SQL) employees.EMPLOYEES.SALARY.ge(minSalary));
        }
        if (maxSalary != null) {
            condition = condition.and(employees.EMPLOYEES.SALARY.le(maxSalary));
        }
        
        return dsl.selectFrom(employees.EMPLOYEES)
            .where(condition)
            .orderBy(employees.EMPLOYEES.SURNAME, employees.EMPLOYEES.NAME)
            .fetchInto(employeesPojo.class);
    }
}
