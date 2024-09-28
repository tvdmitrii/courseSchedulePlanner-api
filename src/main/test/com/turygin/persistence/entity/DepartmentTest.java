package com.turygin.persistence.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentTest {

    private Department dept1;
    private Department dept2;

    @BeforeEach
    void populateUsers() {
        dept1 = new Department("Computer Science");
        dept1.setId(1);
        dept2 = new Department("Engineering");
        dept2.setId(2);
    }

    @Test
    void testSameObject() {
        Department dept1_copy = new Department(dept1.getName());
        dept1_copy.setId(dept1.getId());

        assertEquals(dept1, dept1_copy);
    }

    @Test
    void testDifferentObject() {
        assertNotEquals(dept1, dept2);
    }

    @Test
    void testHashCodeSameObject() {
        Department dept1_copy = new Department(dept1.getName());
        dept1_copy.setId(dept1.getId());

        assertEquals(dept1.hashCode(), dept1_copy.hashCode());
    }

    @Test
    void testHashCodeDifferentObject() {
        assertNotEquals(dept1.hashCode(), dept2.hashCode());
    }
}