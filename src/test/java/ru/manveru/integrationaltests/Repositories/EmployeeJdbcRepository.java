package ru.manveru.integrationaltests.Repositories;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.manveru.integrationaltests.DTO.Employee;
import ru.manveru.integrationaltests.Extentions.LoggingExtension;

public class EmployeeJdbcRepository extends AbstractJdbcRepository<Employee>{

    private static final Logger logger = LoggerFactory.getLogger(LoggingExtension.class);
    private static EmployeeJdbcRepository instance;
    private final HikariConnectionPool cp;
    private final AtomicInteger connectionRequests = new AtomicInteger(0);
    
    public EmployeeJdbcRepository(HikariConnectionPool cp) {
        this.cp = cp;
    }
    
    public static EmployeeJdbcRepository getInstance(HikariConnectionPool cp){
        if (instance == null) {
            logger.info("************Repository created**********");
            instance = new EmployeeJdbcRepository(cp);
        }
        return instance;
    }
    
    @Override
    protected Connection getConnection() throws SQLException {
        int requestCount = connectionRequests.incrementAndGet();
        logger.debug("Before connection #{}. Pool stats - Active: {}, Idle: {}, Total: {}", 
            requestCount,
            cp.getHikariPoolMXBean().getActiveConnections(),
            cp.getHikariPoolMXBean().getIdleConnections(),
            cp.getHikariPoolMXBean().getTotalConnections()
        );
        long startTime = System.currentTimeMillis();
        Connection conn = cp.getConnection();
        long duration = System.currentTimeMillis() - startTime;
        logger.debug("After making connection #{} obtained in {}ms. Pool stats - Active: {}, Idle: {}, Total: {}", 
            requestCount,
            duration,
            cp.getHikariPoolMXBean().getActiveConnections(),
            cp.getHikariPoolMXBean().getIdleConnections(),
            cp.getHikariPoolMXBean().getTotalConnections()
        );
        return conn;
    }
    
    public List<Employee> findBySurname(String surname) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE surname LIKE ?";
        
        try(Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + surname + "%");
            logger.debug("SELECT record by surname " + surname);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
                employees.add(mapResultSetToEntity(rs));
        }
        return employees;
    }

    @Override
    protected String getTableName() {
        return "employees";
    }

    @Override
    protected String getIdColumn() {
        return "id";
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO employees (name, surname, department, salary) VALUES (?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE emloyees SET name = ?, surname = ?, department = ?, salary = ? WHERE id = ?";
    }

    @Override
    protected void setIdParameter(PreparedStatement stmt, Employee entity) throws SQLException {
        stmt.setInt(5, entity.getId());
    }

    @Override
    protected Employee mapResultSetToEntity(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setId(rs.getInt("id"));
        employee.setName(rs.getString("name"));
        employee.setSurname(rs.getString("surname"));
        employee.setDepartment(rs.getString("department"));
        employee.setSalary(rs.getInt("salary"));
        return employee;
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, Employee entity) throws SQLException {
        stmt.setString(1, entity.getName());
        stmt.setString(2, entity.getSurname());
        stmt.setString(3, entity.getDepartment());
        stmt.setInt(4, entity.getSalary());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, Employee entity) throws SQLException {
        stmt.setString(1, entity.getName());
        stmt.setString(2, entity.getSurname());
        stmt.setString(3, entity.getDepartment());
        stmt.setInt(4, entity.getSalary());
    }
}
