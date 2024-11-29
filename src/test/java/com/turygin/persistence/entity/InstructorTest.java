package com.turygin.persistence.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Instructor entity tests. */
class InstructorTest {

    private Instructor instructor1;
    private Instructor instructor2;

    /**
     * Resets instructors before each test run.
     */
    @BeforeEach
    void resetInstructors() {
        instructor1 = new Instructor("James", "Connor");
        instructor2 = new Instructor("Kelly", "Smith");
    }

    /** Ensure full name is constructed correctly. */
    @Test
    void getFullName() {
        assertEquals("James Connor", instructor1.getFullName());
    }

    /** Ensure deep comparison returns true for an object and its copy. */
    @Test
    void testSameObject() {
        Instructor inst1_copy = new Instructor(instructor1.getFirstName(), instructor1.getLastName());

        assertEquals(instructor1, inst1_copy);
    }

    /** Ensure deep comparison returns false for different objects. */
    @Test
    void testDifferentObject() {
        assertNotEquals(instructor1, instructor2);
    }

    /** Ensure that hash is the same for equal objects. */
    @Test
    void testHashCodeSameObject() {
        Instructor inst1_copy = new Instructor(instructor1.getFirstName(), instructor1.getLastName());

        assertEquals(instructor1.hashCode(), inst1_copy.hashCode());
    }

    /** Ensure that hash is different for different objects. */
    @Test
    void testHashCodeDifferentObject() {
        assertNotEquals(instructor1.hashCode(), instructor2.hashCode());
    }
}