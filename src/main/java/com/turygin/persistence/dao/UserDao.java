package com.turygin.persistence.dao;

import com.turygin.persistence.SessionFactoryProvider;
import com.turygin.persistence.entity.User;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;

import java.util.List;

/**
 * User data access object that queries user table in the database.
 */
public class UserDao {

    private static final Logger logger = LogManager.getLogger(UserDao.class);
    private static final SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();

    /**
     * Get user by id.
     * @param id unique user id
     * @return user object, or null if not found
     */
    public static User getById(long id) {
        logger.debug("Searching user by id: {}", id);
        Session session = sessionFactory.openSession();
        User user = session.get(User.class, id);
        session.close();
        return user;
    }

    /**
     * Updates the current user in the database.
     * @param user the user to save into the database
     */
    public static void update(User user) {
        logger.debug("Updating user: {}", user);
        sessionFactory.inTransaction(session -> {
            session.merge(user);
        });
    }

    /**
     * Inserts a new user into the database.
     * @param user the user to insert into the database
     * @return new used id
     */
    public static long insert(User user) {
        logger.debug("Inserting user: {}", user);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(user);
        session.getTransaction().commit();
        long userId = user.getId();
        session.close();
        return userId;
    }

    /**
     * Removes the user from the database.
     * @param user the user to remove
     */
    public static void delete(User user) {
        logger.debug("Deleting user: {}", user);
        sessionFactory.inTransaction(session -> {
            session.remove(user);
        });
    }

    /**
     * Get all users from the database.
     * @return a list of all users
     */
    public static List<User> getAll() {
        Session session = sessionFactory.openSession();
        List<User> users = session.createSelectionQuery("from User", User.class).getResultList();
        session.close();
        logger.debug("Found {} users.", users.size());
        return users;
    }

    /**
     * Get a list of all users that match the property=value condition.
     * @param property the name of the User object field
     * @param value the value to compare against
     * @return a list of user matching the search criteria
     */
    public static List<User> getByPropertyEquals(String property, Object value) {
        logger.debug("Searching users by {} equal to '{}'", property, value);
        Session session = sessionFactory.openSession();
        HibernateCriteriaBuilder b = session.getCriteriaBuilder();
        JpaCriteriaQuery<User> q = b.createQuery(User.class);
        Root<User> root = q.from(User.class);
        q.select(root).where(b.equal(root.get(property), value));

        List<User> users = session.createQuery(q).getResultList();
        session.close();
        logger.debug("Found {} users.", users.size());
        return users;
    }

    /**
     * Get a list of all users that match the property LIKE %value% condition.
     * @param property the name of the User object field
     * @param value the substring value
     * @return a list of user matching the search criteria
     */
    public static List<User> getByPropertySubstring(String property, String value) {
        logger.debug("Searching for users by {} that contains '{}'", property, value);
        Session session = sessionFactory.openSession();
        HibernateCriteriaBuilder b = session.getCriteriaBuilder();
        JpaCriteriaQuery<User> q = b.createQuery(User.class);
        Root<User> root = q.from(User.class);
        q.select(root).where(b.like(root.get(property), String.format("%%%s%%", value)));

        List<User> users = session.createQuery(q).getResultList();
        logger.debug("Found {} users.", users.size());
        session.close();
        return users;
    }
}
