package com.turygin.persistence.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    private Course course1;
    private Course course2;

    @BeforeEach
    void populateUsers() {
        Department compSci = new Department("CS", "Computer Science");
        course1 = new Course("Introduction to Python", "Introduces students to Python.", 3, 101, compSci);
        course2 = new Course("Advanced Java", "Java EE.", 4, 203, compSci);
    }

    @Test
    void getCode() {
        assertEquals("CS 101", course1.getCode());
    }

    @Test
    void creditsNegative() {
        int credits = -13;
        assertThrows(IllegalArgumentException.class, () -> course1.setCredits(credits));
    }

    @Test
    void creditsZero() {
        int credits = 0;
        assertThrows(IllegalArgumentException.class, () -> course1.setCredits(credits));
    }

    @Test
    void creditsMaxByte() {
        int credits = 255;
        course1.setCredits(credits);
        assertEquals(credits, course1.getCredits());
    }

    @Test
    void creditsTooLarge() {
        int credits = 256;
        assertThrows(IllegalArgumentException.class, () -> course1.setCredits(credits));
    }

    @Test
    void numberNegative() {
        int number = -13;
        assertThrows(IllegalArgumentException.class, () -> course1.setNumber(number));
    }

    @Test
    void numberZero() {
        int number = 0;
        assertThrows(IllegalArgumentException.class, () -> course1.setNumber(number));
    }

    @Test
    void numberMaxShort() {
        int number = 65535;
        course1.setNumber(number);
        assertEquals(number, course1.getNumber());
    }

    @Test
    void numberTooLarge() {
        int number = 65536;
        assertThrows(IllegalArgumentException.class, () -> course1.setNumber(number));
    }

    @Test
    void testSameObject() {
        Course course_copy = new Course(course1.getTitle(), course1.getDescription(), course1.getCredits(), course1.getNumber());

        assertEquals(course1, course_copy);
    }

    @Test
    void testDifferentObject() {
        assertNotEquals(course1, course2);
    }

    @Test
    void testHashCodeSameObject() {
        Course course_copy = new Course(course1.getTitle(), course1.getDescription(), course1.getCredits(), course1.getNumber());

        assertEquals(course1.hashCode(), course_copy.hashCode());
    }

    @Test
    void testHashCodeDifferentObject() {
        assertNotEquals(course1.hashCode(), course2.hashCode());
    }
}