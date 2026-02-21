package ru.manveru.integrationaltests.Repositories;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class GenericJdbcRepository<T> {
    
    private final Class<T> entityClass;
    private final String tableName;
    private final HikariConnectionPool connectionPool;
    private final Map<String, Field> fieldMap = new HashMap<>();

    public GenericJdbcRepository(Class<T> entityClass, String tableName, HikariConnectionPool connectionPool) {
        this.entityClass = entityClass;
        this.tableName = tableName;
        this.connectionPool = connectionPool;
        initFieldMap();
    }

    private void initFieldMap() {
        Field[] fields = entityClass.getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            fieldMap.put(field.getName().toLowerCase(), field);
        }
    }
    
    //Create
    public int ceate(T entity) throws SQLException{
        List<Field> fields = getFieldsWithoutId();
        String columns = getColumnsString(fields);
        String placeholders = getPlaceholdersString(fields.size());
        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columns, placeholders);
        
        try(Connection conn = connectionPool.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            setParameters(stmt, entity, fields, 1);
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0)
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()){
                        Field idField = getIdField();
                        idField.set(entity, generatedKeys.getInt(1));
                        return generatedKeys.getInt(1);
                    }
                }
            return -1;  
        } catch (IllegalAccessException e){
            throw new SQLException("Failed to access entity fields", e);
        }
    }
    
    //Find by id
    public T findById (int id) throws SQLException {
        String sql = String.format("SELECT * FROM %s WHERE id = ?", tableName);
        
        try (Connection conn = connectionPool.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            return rs.next() ? mapResultSetToEntity(rs) : null;
        }
    }

    private List<Field> getFieldsWithoutId() {
        List<Field> fields = new ArrayList<>();
        for(Field field : entityClass.getDeclaredFields()){
            if(!field.getName().equalsIgnoreCase("id")){
                field.setAccessible(true);
                fields.add(field);
            }
        }
        return fields;
    }

    private String getColumnsString(List<Field> fields) {
        return fields.stream()
                .map(f -> f.getName().toLowerCase())
                .collect(Collectors.joining(", "));
    }

    private String getPlaceholdersString(int count) {
        return String.join(", ", Collections.nCopies(count, "?"));
    }

    private void setParameters(PreparedStatement stmt, T entity, List<Field> fields, int startIndex)
            throws SQLException, IllegalAccessException {
        for(int i = 0; i < fields.size(); i++){
            Field field = fields.get(i);
            Object value = field.get(entity);
            stmt.setObject(startIndex + i, value);
        }
    }

    private Field getIdField() {
        try {
            Field idField = entityClass.getDeclaredField("id");
            idField.setAccessible(true);
            return idField;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Entity must have 'id' field", e);
        }
    }

    private T mapResultSetToEntity(ResultSet rs) throws SQLException {
        try{
            T entity = entityClass.getDeclaredConstructor().newInstance();
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
             for(int i = 1; i <= columnCount; i++){
                 String columnName = metaData.getColumnLabel(i).toLowerCase();
                 Field field = fieldMap.get(columnName);
                 if (field != null){
                     Object value = rs.getObject(i);
                     if (value != null)
                         field.set(entity, convertValue(field.getType(), value));
                 }
             }
             return entity;
        } catch (Exception e){
            throw new SQLException("Failed to map ResultSet to entity", e);
        }
    }

    private Object convertValue(Class<?> targetType, Object value) {
        if (targetType == Integer.class || targetType == int.class) {
            return ((Number) value).intValue();
        } else if (targetType == Long.class || targetType == long.class) {
            return ((Number) value).longValue();
        } else if (targetType == String.class) {
            return value.toString();
        }
        return value;
    }
}
