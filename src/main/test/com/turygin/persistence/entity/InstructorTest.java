package com.turygin.persistence.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InstructorTest {

    private Instructor instructor1;
    private Instructor instructor2;

    @BeforeEach
    void populateUsers() {
        instructor1 = new Instructor("James", "Connor");
        instructor1.setId(1);
        instructor2 = new Instructor("Kelly", "Smith");
        instructor2.setId(2);
    }

    @Test
    void getFullName() {
        assertEquals("James Connor", instructor1.getFullName());
    }

    @Test
    void testSameObject() {
        Instructor inst1_copy = new Instructor(instructor1.getFirstName(), instructor1.getLastName());
        inst1_copy.setId(instructor1.getId());

        assertEquals(instructor1, inst1_copy);
    }

    @Test
    void testDifferentObject() {
        assertNotEquals(instructor1, instructor2);
    }

    @Test
    void testHashCodeSameObject() {
        Instructor inst1_copy = new Instructor(instructor1.getFirstName(), instructor1.getLastName());
        inst1_copy.setId(instructor1.getId());

        assertEquals(instructor1.hashCode(), inst1_copy.hashCode());
    }

    @Test
    void testHashCodeDifferentObject() {
        assertNotEquals(instructor1.hashCode(), instructor2.hashCode());
    }
}