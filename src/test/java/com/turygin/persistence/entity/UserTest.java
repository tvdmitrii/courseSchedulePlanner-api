package com.turygin.persistence.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class UserTest {

    private User user1;
    private User user2;

    @BeforeEach
    void populateUsers() {
        user1 = new User("John", "Smith", "john.smith@example.com", User.Type.USER);
        user2 = new User("Kyle", "Thompson", "kyle.thompson@example.com", User.Type.ADMIN);
    }

    @Test
    void getFullName() {
        assertEquals("John Smith", user1.getFullName());
    }

    @Test
    void testEqualsSameUser() {
        User sameUser = new User(user1.getFirstName(), user1.getLastName(), user1.getEmail(), user1.getRole());

        assertEquals(user1, sameUser);
    }

    @Test
    void testEqualsDifferentUser() {
        assertNotEquals(user1, user2);
    }

    @Test
    void testHashCodeSameUser() {
        User sameUser = new User(user1.getFirstName(), user1.getLastName(), user1.getEmail(), user1.getRole());

        assertEquals(user1.hashCode(), sameUser.hashCode());
    }

    @Test
    void testHashCodeDifferentUser() {
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }
}