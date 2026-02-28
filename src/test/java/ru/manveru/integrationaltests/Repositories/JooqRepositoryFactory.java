package ru.manveru.integrationaltests.Repositories;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JooqRepositoryFactory implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(JooqRepositoryFactory.class);
    private static JooqRepositoryFactory instance;
    private final HikariDataSource dataSource;
    private final Configuration configuration;
    private final Map<Class<?>, Object> repositoryCache = new HashMap<>();
    
    private JooqRepositoryFactory(String host, String user, String pass) {
        HikariConfig config = new HikariConfig();
        // Настройка пула соединений
        config.setJdbcUrl(host);
        config.setUsername(user);
        config.setPassword(pass);
        config.setDriverClassName("org.postgresql.Driver");
        
        // Дополнительные настройки по умолчанию
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        
        this.dataSource = new HikariDataSource(config);
        
        this.configuration = new DefaultConfiguration()
            .set(dataSource)
            .set(SQLDialect.POSTGRES);
    }
    
    public static JooqRepositoryFactory getInstance(String host, String user, String pass) {
        if (instance == null) {
            instance = new JooqRepositoryFactory(host, user, pass);
            logger.debug("************Factory created****************");
        }
        return instance;
    }
    
    @FunctionalInterface
    private interface RepositoryCreator<T> {
        T create(Configuration config);
    }
    
    @SuppressWarnings("unchecked")
    private <T> T getOrCreate(Class<T> type, RepositoryCreator<T> creator) {
        return (T) repositoryCache.computeIfAbsent(type, k -> creator.create(configuration));
    }
    
    public EmployeeJooqRepository getEmployeeRepository() {
        return getOrCreate(EmployeeJooqRepository.class, EmployeeJooqRepository::new);
    }
    
    @Override
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
