package ru.manveru.integrationaltests.Repositories;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import ru.manveru.integrationaltests.annotations.Column;
import ru.manveru.integrationaltests.annotations.Table;

public class AnnotatedJdbcRepository<T> extends GenericJdbcRepository<T>{
    
    public AnnotatedJdbcRepository(Class<T> entityClass, HikariConnectionPool connectionPool) {
        
        super(entityClass, getTableName(entityClass), connectionPool);
    }
    
    private static <T> String getTableName(Class<T> entityClass){
        Table table = entityClass.getAnnotation(Table.class);
        return table != null ? table.name() : entityClass.getSimpleName().toLowerCase();
    }
    
    @Override
    protected List<Field> getFieldsWithoutId(){
        List<Field> fields = new ArrayList<>();
        for (Field field : entityClass.getDeclaredFields()){
            if (field.isAnnotationPresent(Column.class) &&
                    !field.getName().equalsIgnoreCase("id")){
                field.setAccessible(true);
                fields.add(field);
            }
        }
        return fields;
    }
}
