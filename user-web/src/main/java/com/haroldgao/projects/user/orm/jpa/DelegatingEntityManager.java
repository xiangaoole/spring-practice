package com.haroldgao.projects.user.orm.jpa;

import com.haroldgao.projects.user.context.ComponentContext;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 委派实现（静态 AOP 实现）
 * 保持容器内单例
 * 目前实现 DelegatingEntityManager : EntityManager = 1:N
 */
public class DelegatingEntityManager implements EntityManager {

    private String persistenceUnitName;

    private String propertiesLocation;

    private ThreadLocal<EntityManager> entityManagerThreadLocal;

    /* These two setter will be invoked by JNDI */
    public void setPersistenceUnitName(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    public void setPropertiesLocation(String propertiesLocation) {
        this.propertiesLocation = propertiesLocation;
    }

    @PostConstruct
    public void init() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName,
                loadProperties(propertiesLocation));
        // Each thread get its own EntityManager instance
        entityManagerThreadLocal = ThreadLocal.withInitial(() -> entityManagerFactory.createEntityManager());
    }

    private Map loadProperties(String propertiesLocation) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL propertiesFileURL = classLoader.getResource(propertiesLocation);
        Properties properties = new Properties();
        try {
            properties.load(propertiesFileURL.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ComponentContext componentContext = ComponentContext.getInstance();
        for (String propertyName : properties.stringPropertyNames()) {
            String propertyValue = properties.getProperty(propertyName);
            if (propertyValue.startsWith("@")) {
                String componentName = propertyValue.substring(1);
                Object component = componentContext.getComponent(componentName);
                properties.put(propertyName, component);
            }
        }

        return properties;
    }

    @Override
    public void persist(Object entity) {
        entityManagerThreadLocal.get().persist(entity);
    }

    @Override
    public <T> T merge(T entity) {
        return entityManagerThreadLocal.get().merge(entity);
    }

    @Override
    public void remove(Object entity) {
        entityManagerThreadLocal.get().remove(entity);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey) {
        return entityManagerThreadLocal.get().find(entityClass, primaryKey);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
        return entityManagerThreadLocal.get().find(entityClass, primaryKey, properties);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
        return entityManagerThreadLocal.get().find(entityClass, primaryKey, lockMode);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
        return entityManagerThreadLocal.get().find(entityClass, primaryKey, lockMode, properties);
    }

    @Override
    public <T> T getReference(Class<T> entityClass, Object primaryKey) {
        return entityManagerThreadLocal.get().getReference(entityClass, primaryKey);
    }

    @Override
    public void flush() {
        entityManagerThreadLocal.get().flush();
    }

    @Override
    public void setFlushMode(FlushModeType flushMode) {
        entityManagerThreadLocal.get().setFlushMode(flushMode);
    }

    @Override
    public FlushModeType getFlushMode() {
        return entityManagerThreadLocal.get().getFlushMode();
    }

    @Override
    public void lock(Object entity, LockModeType lockMode) {
        entityManagerThreadLocal.get().lock(entity, lockMode);
    }

    @Override
    public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        entityManagerThreadLocal.get().lock(entity, lockMode, properties);
    }

    @Override
    public void refresh(Object entity) {
        entityManagerThreadLocal.get().refresh(entity);
    }

    @Override
    public void refresh(Object entity, Map<String, Object> properties) {
        entityManagerThreadLocal.get().refresh(entity, properties);
    }

    @Override
    public void refresh(Object entity, LockModeType lockMode) {
        entityManagerThreadLocal.get().refresh(entity, lockMode);
    }

    @Override
    public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        entityManagerThreadLocal.get().refresh(entity, lockMode, properties);
    }

    @Override
    public void clear() {
        entityManagerThreadLocal.get().clear();
    }

    @Override
    public void detach(Object entity) {
        entityManagerThreadLocal.get().detach(entity);
    }

    @Override
    public boolean contains(Object entity) {
        return entityManagerThreadLocal.get().contains(entity);
    }

    @Override
    public LockModeType getLockMode(Object entity) {
        return entityManagerThreadLocal.get().getLockMode(entity);
    }

    @Override
    public void setProperty(String propertyName, Object value) {
        entityManagerThreadLocal.get().setProperty(propertyName, value);
    }

    @Override
    public Map<String, Object> getProperties() {
        return entityManagerThreadLocal.get().getProperties();
    }

    @Override
    public Query createQuery(String qlString) {
        return entityManagerThreadLocal.get().createQuery(qlString);
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        return entityManagerThreadLocal.get().createQuery(criteriaQuery);
    }

    @Override
    public Query createQuery(CriteriaUpdate updateQuery) {
        return entityManagerThreadLocal.get().createQuery(updateQuery);
    }

    @Override
    public Query createQuery(CriteriaDelete deleteQuery) {
        return entityManagerThreadLocal.get().createQuery(deleteQuery);
    }

    @Override
    public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
        return entityManagerThreadLocal.get().createQuery(qlString, resultClass);
    }

    @Override
    public Query createNamedQuery(String name) {
        return entityManagerThreadLocal.get().createNamedQuery(name);
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
        return entityManagerThreadLocal.get().createNamedQuery(name, resultClass);
    }

    @Override
    public Query createNativeQuery(String sqlString) {
        return entityManagerThreadLocal.get().createNativeQuery(sqlString);
    }

    @Override
    public Query createNativeQuery(String sqlString, Class resultClass) {
        return entityManagerThreadLocal.get().createNativeQuery(sqlString, resultClass);
    }

    @Override
    public Query createNativeQuery(String sqlString, String resultSetMapping) {
        return entityManagerThreadLocal.get().createNativeQuery(sqlString, resultSetMapping);
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
        return entityManagerThreadLocal.get().createNamedStoredProcedureQuery(name);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
        return entityManagerThreadLocal.get().createStoredProcedureQuery(procedureName);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
        return entityManagerThreadLocal.get().createStoredProcedureQuery(procedureName, resultClasses);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
        return entityManagerThreadLocal.get().createStoredProcedureQuery(procedureName, resultSetMappings);
    }

    @Override
    public void joinTransaction() {
        entityManagerThreadLocal.get().joinTransaction();
    }

    @Override
    public boolean isJoinedToTransaction() {
        return entityManagerThreadLocal.get().isJoinedToTransaction();
    }

    @Override
    public <T> T unwrap(Class<T> cls) {
        return entityManagerThreadLocal.get().unwrap(cls);
    }

    @Override
    public Object getDelegate() {
        return entityManagerThreadLocal.get().getDelegate();
    }

    @Override
    public void close() {
        entityManagerThreadLocal.get().close();
    }

    @Override
    public boolean isOpen() {
        return entityManagerThreadLocal.get().isOpen();
    }

    @Override
    public EntityTransaction getTransaction() {
        return entityManagerThreadLocal.get().getTransaction();
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerThreadLocal.get().getEntityManagerFactory();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return entityManagerThreadLocal.get().getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return entityManagerThreadLocal.get().getMetamodel();
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
        return entityManagerThreadLocal.get().createEntityGraph(rootType);
    }

    @Override
    public EntityGraph<?> createEntityGraph(String graphName) {
        return entityManagerThreadLocal.get().createEntityGraph(graphName);
    }

    @Override
    public EntityGraph<?> getEntityGraph(String graphName) {
        return entityManagerThreadLocal.get().getEntityGraph(graphName);
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
        return entityManagerThreadLocal.get().getEntityGraphs(entityClass);
    }
}
