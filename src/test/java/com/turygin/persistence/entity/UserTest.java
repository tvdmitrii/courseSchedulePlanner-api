package com.turygin.persistence.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/** User entity tests. */
class UserTest {

    private User user1;
    private User user2;

    /**
     * Resets users before each test run.
     */
    @BeforeEach
    void resetUsers() {
        user1 = new User(UUID.randomUUID(), User.Type.USER);
        user2 = new User(UUID.randomUUID(), User.Type.ADMIN);
    }

    /** Ensure deep comparison returns true for an object and its copy. */
    @Test
    void testEqualsSameUser() {
        User sameUser = new User(user1.getUuid(), user1.getRole());

        assertEquals(user1, sameUser);
    }

    /** Ensure deep comparison returns false for different objects. */
    @Test
    void testEqualsDifferentUser() {
        assertNotEquals(user1, user2);
    }

    /** Ensure that hash is the same for equal objects. */
    @Test
    void testHashCodeSameUser() {
        User sameUser = new User(user1.getUuid(), user1.getRole());

        assertEquals(user1.hashCode(), sameUser.hashCode());
    }

    /** Ensure that hash is different for different objects. */
    @Test
    void testHashCodeDifferentUser() {
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }
}