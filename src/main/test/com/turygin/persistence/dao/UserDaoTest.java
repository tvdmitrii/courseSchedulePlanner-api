package com.turygin.persistence.dao;

import com.turygin.persistence.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class UserDaoTest {

    private static final Logger logger = LogManager.getLogger(UserDaoTest.class);
    private static final List<User> users = new ArrayList<>();
    private final Dao<User> userDao = new Dao<>(User.class);

    @BeforeAll
    static void resetDatabase() {
        if(!ResetDatabaseHelper.reset()) {
            logger.error("Could not reset database!");
            throw new RuntimeException("Could not reset database!");
        }

        // Populate users
        users.add(new User("Phyllis", "Martin", "phyllis.martin@example.com", User.Type.USER));
        users.add(new User("Lucas", "Hawkins", "lucas.hawkins@example.com", User.Type.USER));
        users.add(new User("Charlene", "Williamson",
                "charlene.williamson@example.com", User.Type.USER));
        users.add(new User("Gene", "Hughes", "gene.hughes@example.com", User.Type.ADMIN));

       for(int i = 0; i < users.size(); i++) {
           users.get(i).setId(i + 1);
       }
    }


    @Test
    void getById() {
        User user1 = users.get(0);

        User user = userDao.getById(user1.getId());

        assertNotNull(user);
        assertEquals(user1, user);
    }

    @Test
    void getById_InvalidId() {
        User user = userDao.getById(1000);

        assertNull(user);
    }

    @Test
    void update() {
        String newLastName = "Thompson";
        User user2 = users.get(1);

        user2 = userDao.getById(user2.getId()); // get detached instance
        user2.setLastName(newLastName);
        userDao.update(user2);
        User user = userDao.getById(user2.getId());

        assertEquals(user2, user);
    }

    @Test
    void insert() {
        User newUser = new User("Another", "Person", "another.person@example.com", User.Type.USER);

        userDao.insert(newUser);
        User user = userDao.getById(newUser.getId());

        assertEquals(newUser, user);
        assertEquals(newUser, user);
    }

    @Test
    void insert_DuplicateEmail() {
        User user3 = users.get(2);

        User newUser = new User(user3.getFirstName(), user3.getLastName(), user3.getEmail(), user3.getRole());

        assertThrows(ConstraintViolationException.class, () -> userDao.insert(newUser));
    }

    @Test
    void delete() {
        User user4 = users.get(3);

        userDao.delete(user4);
        User user = userDao.getById(user4.getId());
        users.remove(user4);

        assertNull(user);
    }

    @Test
    void getAll() {
        List<User> usersFromDb = userDao.getAll();

        assertNotNull(usersFromDb);
        assertEquals(usersFromDb.size(), users.size());
        for(int i = 0; i < users.size(); i++) {
            assertEquals(users.get(i), usersFromDb.get(i));
        }
    }

    @Test
    void getByPropertyEquals_String() {
        User user1 = users.get(0);
        List<User> foundUsers = userDao.getByPropertyEquals("firstName", user1.getFirstName());

        assertNotNull(foundUsers);
        assertEquals(foundUsers.size(), 1);
        assertEquals(foundUsers.get(0), user1);
    }

    @Test
    void getByPropertyEquals_Long() {
        User user2 = users.get(1);
        List<User> foundUsers = userDao.getByPropertyEquals("id", user2.getId());

        assertNotNull(foundUsers);
        assertEquals(foundUsers.size(), 1);
        assertEquals(foundUsers.get(0), user2);
    }

    @Test
    void getByPropertyEquals_Missing() {
        List<User> foundUsers = userDao.getByPropertyEquals("id", 1000);

        assertEquals(foundUsers.size(), 0);
    }

    @Test
    void getByPropertySubstring() {
        List<User> foundUsers = userDao.getByPropertySubstring("lastName", "son");

        assertNotNull(foundUsers);
        assertEquals(foundUsers.size(), 3);
    }
}