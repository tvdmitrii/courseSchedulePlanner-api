package com.turygin.persistence.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class UserTest {

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User("John", "Smith", "john.smith@example.com", User.Type.USER);
        user2 = new User("Kyle", "Thompson", "kyle.thompson@example.com", User.Type.ADMIN);
    }

    @Test
    void getFullName() {
        assertEquals("John Smith", user1.getFullName());
    }

    @Test
    void testEqualsSameUser() {
        user1.setId(22);

        User sameUser = new User(user1.getFirstName(), user1.getLastName(), user1.getEmail(), user1.getRole());
        sameUser.setId(user1.getId());

        assertEquals(user1, sameUser);
        assertEquals(user1.getId(), sameUser.getId());
        assertEquals(user1.getFirstName(), sameUser.getFirstName());
        assertEquals(user1.getLastName(), sameUser.getLastName());
        assertEquals(user1.getEmail(), sameUser.getEmail());
        assertEquals(user1.getRole(), sameUser.getRole());
    }

    @Test
    void testEqualsDifferentUser() {
        user1.setId(22);
        user2.setId(11);

        assertNotEquals(user1, user2);
        assertNotEquals(user1.getId(), user2.getId());
        assertNotEquals(user1.getFirstName(), user2.getFirstName());
        assertNotEquals(user1.getLastName(), user2.getLastName());
        assertNotEquals(user1.getEmail(), user2.getEmail());
        assertNotEquals(user1.getRole(), user2.getRole());
    }

    @Test
    void testHashCodeSameUser() {
        user1.setId(22);

        User sameUser = new User(user1.getFirstName(), user1.getLastName(), user1.getEmail(), user1.getRole());
        sameUser.setId(user1.getId());

        assertEquals(user1.hashCode(), sameUser.hashCode());
    }

    @Test
    void testHashCodeDifferentUser() {
        user1.setId(22);
        user2.setId(11);

        assertNotEquals(user1, user2);
        assertNotEquals(user1.getId(), user2.getId());
        assertNotEquals(user1.getFirstName(), user2.getFirstName());
        assertNotEquals(user1.getLastName(), user2.getLastName());
        assertNotEquals(user1.getEmail(), user2.getEmail());
        assertNotEquals(user1.getRole(), user2.getRole());
    }
}