package com.turygin.persistence.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Course entity tests. */
class CourseTest {

    private Course course1;
    private Course course2;

    /**
     * Reset test courses before each test.
     */
    @BeforeEach
    void resetCourses() {
        Department compSci = new Department("CS", "Computer Science");
        course1 = new Course("Introduction to Python",
                "Introduces students to Python.", 3, 101, compSci);
        course2 = new Course("Advanced Java", "Java EE.", 4, 203, compSci);
    }

    /** Ensure course code generation works. */
    @Test
    void getCode() {
        assertEquals("CS 101", course1.getCode());
    }

    /** Ensure negative course credits are not allowed. */
    @Test
    void creditsNegative() {
        int credits = -13;
        assertThrows(IllegalArgumentException.class, () -> course1.setCredits(credits));
    }

    /** Ensure 0 course credits are not allowed. */
    @Test
    void creditsZero() {
        int credits = 0;
        assertThrows(IllegalArgumentException.class, () -> course1.setCredits(credits));
    }

    /** Ensure maximum number of credits works. */
    @Test
    void creditsMaxByte() {
        int credits = 255;
        course1.setCredits(credits);
        assertEquals(credits, course1.getCredits());
    }

    /** Ensure that the size of credits cannot be more than 1 byte. */
    @Test
    void creditsTooLarge() {
        int credits = 256;
        assertThrows(IllegalArgumentException.class, () -> course1.setCredits(credits));
    }

    /** Ensure negative course numbers are not allowed. */
    @Test
    void numberNegative() {
        int number = -13;
        assertThrows(IllegalArgumentException.class, () -> course1.setNumber(number));
    }

    /** Ensure course number 0 is not allowed. */
    @Test
    void numberZero() {
        int number = 0;
        assertThrows(IllegalArgumentException.class, () -> course1.setNumber(number));
    }

    /** Ensure maximum course number of 65535 still works. */
    @Test
    void numberMaxShort() {
        int number = 65535;
        course1.setNumber(number);
        assertEquals(number, course1.getNumber());
    }

    /** Ensure course numbers larger than short are not allowed. */
    @Test
    void numberTooLarge() {
        int number = 65536;
        assertThrows(IllegalArgumentException.class, () -> course1.setNumber(number));
    }

    /** Ensure deep object comparison returns true for a course and its copy. */
    @Test
    void testSameObject() {
        Course course_copy =
                new Course(course1.getTitle(), course1.getDescription(), course1.getCredits(), course1.getNumber());

        assertEquals(course1, course_copy);
    }

    /** Ensure deep object comparison returns false for different objects. */
    @Test
    void testDifferentObject() {
        assertNotEquals(course1, course2);
    }

    /** Ensure that hash is the same for equal objects. */
    @Test
    void testHashCodeSameObject() {
        Course course_copy = new Course(course1.getTitle(), course1.getDescription(), course1.getCredits(), course1.getNumber());

        assertEquals(course1.hashCode(), course_copy.hashCode());
    }

    /** Ensure that hash is different for different objects. */
    @Test
    void testHashCodeDifferentObject() {
        assertNotEquals(course1.hashCode(), course2.hashCode());
    }
}