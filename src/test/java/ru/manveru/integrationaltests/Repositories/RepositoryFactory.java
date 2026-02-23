package ru.manveru.integrationaltests.Repositories;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepositoryFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(RepositoryFactory.class);
    private static RepositoryFactory instance;
    private final HikariConnectionPool connectionPool;
    private final Map<Class<?>, Object> repositories = new HashMap<>();
    
    private RepositoryFactory(){
        this.connectionPool = new HikariConnectionPool();
    }
    
    public static RepositoryFactory getInstance() {
        if (instance == null) {
            instance = new RepositoryFactory();
            logger.debug("************Factory created****************");
        }
        return instance;
    }
    
    @SuppressWarnings("unchecked")
    public <T> AnnotatedJdbcRepository<T> getRepository(Class<T> entityClass) {
        //Repository allrady exist
        if (repositories.containsKey(entityClass)) {
            return (AnnotatedJdbcRepository<T>) repositories.get(entityClass);
        }
        
        // Create new repository
        AnnotatedJdbcRepository<T> repository = 
            new AnnotatedJdbcRepository<>(entityClass, connectionPool);
        repositories.put(entityClass, repository);
        return repository;
    }
}
