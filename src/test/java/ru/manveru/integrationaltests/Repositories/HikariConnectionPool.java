package ru.manveru.integrationaltests.Repositories;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import java.sql.Connection;
import java.sql.SQLException;

public class HikariConnectionPool {
    private static HikariDataSource dataSource;
    static {
        HikariConfig config = new HikariConfig();
        
        // Base settings
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/my_db");
        config.setUsername("postgres");
        config.setPassword("WenCoda");
        config.setDriverClassName("org.postgresql.Driver");
        
        // Pool settings
        config.setMaximumPoolSize(10);     // max connections in pool
        config.setMinimumIdle(2);           // min idl connections
        config.setConnectionTimeout(30000); // connection timeout (ms)
        config.setIdleTimeout(600000);      // idl connection timeout (ms)
        config.setMaxLifetime(1800000);     // connection max lifetime (ms)
        
        dataSource = new HikariDataSource(config);
    }
    
    /**
     * Get connection from pool
     * @return connection to DB
     * @throws SQLException 
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    public HikariPoolMXBean getHikariPoolMXBean() {
        return dataSource.getHikariPoolMXBean();
    }
}
