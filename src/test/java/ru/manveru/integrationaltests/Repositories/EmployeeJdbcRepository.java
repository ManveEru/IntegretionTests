package ru.manveru.integrationaltests.Repositories;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.manveru.integrationaltests.DTO.Employee;
import ru.manveru.integrationaltests.Extentions.LoggingExtension;

public class EmployeeJdbcRepository {
//    private static final String URL = "jdbc:postgresql://localhost:5432/my_db";
//    private static final String USER = "postgres";
//    private static final String PASSWORD = "WenCoda";
    private static final Logger logger = LoggerFactory.getLogger(LoggingExtension.class);
    private static EmployeeJdbcRepository instance;
    private static final HikariDataSource dataSource;
    
    public static EmployeeJdbcRepository getInstance(){
        if (instance == null)
            instance = new EmployeeJdbcRepository();
        return instance;
    }
    
    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/my_db");
        config.setUsername("postgres");
        config.setPassword("WenCoda");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        
        dataSource = new HikariDataSource(config);
    }
    
    private Connection getConnection() throws SQLException {
        logger.debug("Connect to DB");
        return dataSource.getConnection();
    }
    
    public int ceate(Employee employee) throws SQLException{
        String sql = "INSERT INTO employees (name, surname, department, salary) VALUES (?, ?, ?, ?)";
        
        try(Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getSurname());
            stmt.setString(3, employee.getDepartment());
            stmt.setInt(4, employee.getSalary());
            
            logger.debug("INSERT record");
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0)
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next())
                        return generatedKeys.getInt(1);
                }
            return -1;  
        }
    }
    
    public Employee findById (int id) throws SQLException {
        String sql = "SELECT * FROM employees WHERE id = ?";
        Employee employee = null;
        
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            logger.debug("SELECT record by id " + id);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
                employee = mapResultSetToEmployee(rs);
        }
        return employee;
    }
    
    public List<Employee> findAll() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        
        logger.debug("SELECT all records");
        try(Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next())
                employees.add(mapResultSetToEmployee(rs));
        }
        return employees;
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
                employees.add(mapResultSetToEmployee(rs));
        }
        return employees;
    }
    
    public boolean update(Employee emploee) throws SQLException {
        String sql = "UPDATE emloyees SET name = ?, surname = ?, department = ?, salary = ? WHERE id = ?";
        
        try(Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, emploee.getName());
            stmt.setString(2, emploee.getSurname());
            stmt.setString(3, emploee.getDepartment());
            stmt.setInt(4, emploee.getSalary());
            stmt.setInt(5, emploee.getId());
            logger.debug("UPDATE record");
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";
        
        try(Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            logger.debug("DELETE record");
            return stmt.executeUpdate() > 0;
        }
    }
    
    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException{
        Employee employee = new Employee();
        employee.setId(rs.getInt("id"));
        employee.setName(rs.getString("name"));
        employee.setSurname(rs.getString("surname"));
        employee.setDepartment(rs.getString("department"));
        employee.setSalary(rs.getInt("salary"));
        return employee;
    }
}
