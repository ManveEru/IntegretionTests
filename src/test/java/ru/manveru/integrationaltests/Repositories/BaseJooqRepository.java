package ru.manveru.integrationaltests.Repositories;

import java.util.List;
import java.util.Optional;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.UpdatableRecord;
import org.jooq.impl.DAOImpl;
import org.jooq.impl.DSL;

/**
 * Базовый generic репозиторий, использующий сгенерированный DAO для общих
 * операций
 *
 * @param <R> Тип записи (Record)
 * @param <P> Тип POJO
 * @param <T> Тип ID
 */
public abstract class BaseJooqRepository<R extends UpdatableRecord<R>, P, T> {

    protected final DAOImpl<R, P, T> dao;
    protected final DSLContext dsl;
    protected final Table<R> table;

    public BaseJooqRepository(Configuration configuration,
            DAOImpl<R, P, T> dao,
            Table<R> table) {
        this.dao = dao;
        this.dsl = DSL.using(configuration);
        this.table = table;
    }

    // ========== Базовые CRUD операции через DAO ==========
    public P create(P entity) {
        R record = dsl.newRecord(table, entity);
        record.store(); // ID заполняется в record

        // Обновляем оригинальный entity или возвращаем новый
        return record.into(dao.getType());
    }

    public void createMany(P... entities) {
        dao.insert(entities);
    }

    public void createMany(List<P> entities) {
        dao.insert(entities);
    }

    public Optional<P> findById(T id) {
        return Optional.ofNullable(dao.findById(id));
    }

    public List<P> findAll() {
        return dao.findAll();
    }

    public void update(P entity) {
        dao.update(entity);
    }

    public void updateMany(P... entities) {
        dao.update(entities);
    }

    public void updateMany(List<P> entities) {
        dao.update(entities);
    }

    public void delete(P entity) {
        dao.delete(entity);
    }

    public void deleteById(T id) {
        dao.deleteById(id);
    }

    public void deleteMany(P... entities) {
        dao.delete(entities);
    }

    public void deleteMany(List<P> entities) {
        dao.delete(entities);
    }

    public boolean existsById(T id) {
        return dao.existsById(id);
    }

    public long count() {
        return dao.count();
    }
}
