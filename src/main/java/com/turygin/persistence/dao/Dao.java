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

    private static final Logger logger = LogManager.getLogger(Dao.class);
    private static final SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();

    private final Class<T> entityClass;

    /**
     * Constructor that stores object's class for use with Hibernate.
     * This information is not available at runtime.
     * @param entityClass object' class
     */
    public Dao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Get entity by id.
     * @param id unique entity id
     * @return entity object, or null if not found
     */
    public T getById(long id) {
        logger.debug("Searching entity by id: {}", id);
        Session session = sessionFactory.openSession();
        T user = session.get(entityClass, id);
        session.close();
        return user;
    }

    /**
     * Updates the current entity in the database.
     * @param entity the entity to save into the database
     */
    public void update(T entity) {
        logger.debug("Updating entity: {}", entity);
        sessionFactory.inTransaction(session -> {
            session.merge(entity);
        });
    }

    /**
     * Inserts a new entity into the database.
     * Entity's new id will be added to the object.
     * @param entity the entity to insert into the database
     */
    public void insert(T entity) {
        logger.debug("Inserting entity: {}", entity);
        sessionFactory.inTransaction(session -> {
            session.persist(entity);
        });
    }

    /**
     * Removes the entity from the database.
     * @param entity the entity to remove
     */
    public void delete(T entity) {
        logger.debug("Deleting entity: {}", entity);
        sessionFactory.inTransaction(session -> {
            session.remove(entity);
        });
    }

    /**
     * Get all entities from the database.
     * @return a list of all entities
     */
    public List<T> getAll() {
        Session session = sessionFactory.openSession();

        HibernateCriteriaBuilder b = session.getCriteriaBuilder();
        JpaCriteriaQuery<T> q = b.createQuery(entityClass);
        Root<T> root = q.from(entityClass);

        List<T> entities = session.createQuery(q).getResultList();
        session.close();
        logger.debug("Found {} entities.", entities.size());
        return entities;
    }

    /**
     * Get a list of all entities that match the property=value condition.
     * @param property the name of the entity object field
     * @param value the value to compare against
     * @return a list of entities matching the search criteria
     */
    public List<T> getByPropertyEquals(String property, Object value) {
        logger.debug("Searching users by {} equal to '{}'", property, value);
        Session session = sessionFactory.openSession();
        HibernateCriteriaBuilder b = session.getCriteriaBuilder();
        JpaCriteriaQuery<T> q = b.createQuery(entityClass);
        Root<T> root = q.from(entityClass);
        q.select(root).where(b.equal(root.get(property), value));

        List<T> entities = session.createQuery(q).getResultList();
        session.close();
        logger.debug("Found {} entities.", entities.size());
        return entities;
    }

    /**
     * Get a list of all entities that match the property LIKE %value% condition.
     * @param property the name of the enitity object field
     * @param value the substring value
     * @return a list of entities matching the search criteria
     */
    public List<T> getByPropertySubstring(String property, String value) {
        logger.debug("Searching for users by {} that contains '{}'", property, value);
        Session session = sessionFactory.openSession();
        HibernateCriteriaBuilder b = session.getCriteriaBuilder();
        JpaCriteriaQuery<T> q = b.createQuery(entityClass);
        Root<T> root = q.from(entityClass);
        q.select(root).where(b.like(root.get(property), String.format("%%%s%%", value)));

        List<T> entities = session.createQuery(q).getResultList();
        logger.debug("Found {} entities.", entities.size());
        session.close();
        return entities;
    }
}
