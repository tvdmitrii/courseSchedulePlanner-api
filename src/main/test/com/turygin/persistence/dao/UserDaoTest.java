package com.turygin.persistence.dao;

import com.turygin.persistence.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class UserDaoTest {

    private static final Logger LOG = LogManager.getLogger(UserDaoTest.class);
    private static final List<User> USERS = new ArrayList<>();
    private static final Dao<User> USER_DAO = new Dao<>(User.class);
    private static final int INITIAL_USER_COUNT = 5;

    @BeforeEach
    void resetDatabase() {
        if(!ResetDatabaseHelper.reset()) {
            LOG.error("Could not reset database!");
            throw new RuntimeException("Could not reset database!");
        }

        // Reset lists
        USERS.clear();

        // Populate users
        USERS.addAll(USER_DAO.getAll());
        assertEquals(INITIAL_USER_COUNT, USERS.size());

        // Sort by id to guarantee order for convenience
        USERS.sort(Comparator.comparingLong(User::getId));
    }
    
    @Test
    void getById() {
        User user1 = USERS.get(0);

        User user = USER_DAO.getById(user1.getId());

        assertNotNull(user);
        assertEquals(user1, user);
    }

    @Test
    void getById_InvalidId() {
        User user = USER_DAO.getById(INITIAL_USER_COUNT + 10);

        assertNull(user);
    }

    @Test
    void update() {
        String newLastName = "Thompson";
        User user2 = USERS.get(1);

        user2.setLastName(newLastName);
        USER_DAO.update(user2);
        User user = USER_DAO.getById(user2.getId());

        assertEquals(user2, user);
    }

    @Test
    void insert() {
        User newUser = new User("Another", "Person", "another.person@example.com", User.Type.USER);

        USER_DAO.insert(newUser);
        User user = USER_DAO.getById(newUser.getId());

        assertEquals(newUser, user);
    }

    @Test
    void insert_DuplicateEmail() {
        User user3 = USERS.get(2);

        User newUser = new User(user3.getFirstName(), user3.getLastName(), user3.getEmail(), user3.getRole());

        assertThrows(ConstraintViolationException.class, () -> USER_DAO.insert(newUser));
    }

    @Test
    void delete() {
        User user4 = USERS.get(INITIAL_USER_COUNT - 1);

        USER_DAO.delete(user4);
        User user = USER_DAO.getById(user4.getId());
        USERS.remove(user4);

        assertNull(user);
    }

    @Test
    void getAll() {
        List<User> usersFromDb = USER_DAO.getAll();

        assertNotNull(usersFromDb);
        assertEquals(usersFromDb.size(), USERS.size());
        usersFromDb.sort(Comparator.comparingLong(User::getId));

        for(int i = 0; i < USERS.size(); i++) {
            assertEquals(USERS.get(i), usersFromDb.get(i));
        }
    }

    @Test
    void getByPropertyEquals_String() {
        User user1 = USERS.get(0);
        List<User> foundUsers = USER_DAO.getByPropertyEquals("firstName", user1.getFirstName());

        assertNotNull(foundUsers);
        assertEquals(1, foundUsers.size());
        assertEquals(user1, foundUsers.get(0));
    }

    @Test
    void getByPropertyEquals_Long() {
        User user2 = USERS.get(1);
        List<User> foundUsers = USER_DAO.getByPropertyEquals("id", user2.getId());

        assertNotNull(foundUsers);
        assertEquals(1, foundUsers.size());
        assertEquals(user2, foundUsers.get(0));
    }

    @Test
    void getByPropertyEquals_Missing() {
        List<User> foundUsers = USER_DAO.getByPropertyEquals("id", 1000);

        assertEquals(0, foundUsers.size());
    }

    @Test
    void getByPropertySubstring() {
        List<User> foundUsers = USER_DAO.getByPropertySubstring("lastName", "in");

        assertNotNull(foundUsers);
        assertEquals(2, foundUsers.size());
    }
}