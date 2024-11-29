package com.turygin.persistence.dao;

import com.turygin.persistence.SessionFactoryProvider;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;

import java.util.List;

/**
 * Generic data access object that queries the database.
 * @param <T> The DTO object corresponding to a table in the database.
 */
public class Dao<T> {

    protected static final Logger LOG = LogManager.getLogger(Dao.class);

    /** Session factory for obtaining Hibernate sessions. */
    protected static final SessionFactory SESSION_FACTORY = SessionFactoryProvider.getSessionFactory();

    /** Stores entity class to facilitate conversion of entities to appropriate type. */
    protected final Class<T> ENTITY_CLASS;

    /**
     * Constructor that stores object's class for use with Hibernate.
     * This information is not available at runtime.
     * @param entityClass object' class
     */
    public Dao(Class<T> entityClass) {
        this.ENTITY_CLASS = entityClass;
    }

    /**
     * Get entity by id.
     * @param id unique entity id
     * @return entity object, or null if not found
     */
    public T getById(long id) {
        LOG.debug("Searching entity by id: {}", id);
        Session session = SESSION_FACTORY.openSession();
        T entity = session.get(ENTITY_CLASS, id);
        session.close();
        return entity;
    }

    /**
     * Updates the current entity in the database.
     * @param entity the entity to save into the database
     */
    public void update(T entity) {
        LOG.debug("Updating entity: {}", entity);
        SESSION_FACTORY.inTransaction(session -> {
            session.merge(entity);
        });
    }

    /**
     * Inserts a new entity into the database.
     * Entity's new id will be added to the object.
     * @param entity the entity to insert into the database
     */
    public void insert(T entity) {
        LOG.debug("Inserting entity: {}", entity);
        SESSION_FACTORY.inTransaction(session -> {
            session.persist(entity);
        });
    }

    /**
     * Removes the entity from the database.
     * @param entity the entity to remove
     */
    public void delete(T entity) {
        LOG.debug("Deleting entity: {}", entity);
        SESSION_FACTORY.inTransaction(session -> {
            session.remove(entity);
        });
    }

    /**
     * Get all entities from the database.
     * @return a list of all entities
     */
    public List<T> getAll() {
        Session session = SESSION_FACTORY.openSession();

        HibernateCriteriaBuilder b = session.getCriteriaBuilder();
        JpaCriteriaQuery<T> q = b.createQuery(ENTITY_CLASS);
        Root<T> root = q.from(ENTITY_CLASS);

        List<T> entities = session.createQuery(q).getResultList();
        session.close();
        LOG.debug("Found {} entities.", entities.size());
        return entities;
    }

    /**
     * Get a list of all entities that match the property=value condition.
     * @param property the name of the entity object field
     * @param value the value to compare against
     * @return a list of entities matching the search criteria
     */
    public List<T> getByPropertyEquals(String property, Object value) {
        LOG.debug("Searching entities by {} equal to '{}'", property, value);
        Session session = SESSION_FACTORY.openSession();
        HibernateCriteriaBuilder b = session.getCriteriaBuilder();
        JpaCriteriaQuery<T> q = b.createQuery(ENTITY_CLASS);
        Root<T> root = q.from(ENTITY_CLASS);
        q.select(root).where(b.equal(root.get(property), value));

        List<T> entities = session.createQuery(q).getResultList();
        session.close();
        LOG.debug("Found {} entities.", entities.size());
        return entities;
    }

    /**
     * Get a list of all entities that match the property LIKE %value% condition.
     * @param property the name of the enitity object field
     * @param value the substring value
     * @return a list of entities matching the search criteria
     */
    public List<T> getByPropertySubstring(String property, String value) {
        LOG.debug("Searching for entities by {} that contains '{}'", property, value);
        Session session = SESSION_FACTORY.openSession();
        HibernateCriteriaBuilder b = session.getCriteriaBuilder();
        JpaCriteriaQuery<T> q = b.createQuery(ENTITY_CLASS);
        Root<T> root = q.from(ENTITY_CLASS);
        q.select(root).where(b.like(b.lower(root.get(property)), String.format("%%%s%%", value.toLowerCase())));

        List<T> entities = session.createQuery(q).getResultList();
        LOG.debug("Found {} entities.", entities.size());
        session.close();
        return entities;
    }
}
