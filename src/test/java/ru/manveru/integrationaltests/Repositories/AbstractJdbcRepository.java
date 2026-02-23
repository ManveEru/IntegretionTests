package ru.manveru.integrationaltests.Repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractJdbcRepository<T> {
    
    protected abstract Connection getConnection() throws SQLException;
    //Table name for SQL statment
    protected abstract String getTableName();
    //Name of column that contain id for SQL statment
    protected abstract String getIdColumn();
    protected abstract String getInsertSql();
    protected abstract String getUpdateSql();
    protected abstract void setIdParameter(PreparedStatement stmt, T entity) throws SQLException;
    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;
    protected abstract void setInsertParameters(PreparedStatement stmt, T entity) throws SQLException;
    protected abstract void setUpdateParameters(PreparedStatement stmt, T entity) throws SQLException;
    
    //Create
    public int ceate(T entity) throws SQLException{
        String sql = getInsertSql();
        
        try(Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            setInsertParameters(stmt, entity);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0)
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next())
                        return generatedKeys.getInt(1);
                }
            return -1;  
        }
    }
    
    //Find by id
    public T findById (int id) throws SQLException {
        String sql = String.format("SELECT * FROM %s WHERE %s = ?", getTableName(), getIdColumn());
        T entity = null;
        
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
                entity = mapResultSetToEntity(rs);
        }
        return entity;
    }
    
    //Find all
    public List<T> findAll() throws SQLException {
        List<T> entity = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s", getTableName());
        
        try(Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next())
                entity.add(mapResultSetToEntity(rs));
        }
        return entity;
    }
    
    //Update
    public boolean update(T entity) throws SQLException {
        String sql = getUpdateSql();
        
        try(Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            setUpdateParameters(stmt, entity);
            setIdParameter(stmt, entity);
            return stmt.executeUpdate() > 0;
        }
    }
    
    //Delete
    public boolean delete(int id) throws SQLException {
        String sql = String.format("DELETE FROM %s WHERE %s = ?", getTableName(), getIdColumn());
        
        try(Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
