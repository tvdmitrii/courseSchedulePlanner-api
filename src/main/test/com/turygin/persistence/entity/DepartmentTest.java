package com.turygin.persistence.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentTest {

    private Department dept1;
    private Department dept2;

    @BeforeEach
    void populateUsers() {
        dept1 = new Department("CS", "Computer Science");
        dept2 = new Department("ENG", "Engineering");
    }

    @Test
    void constructorUpperCaseCode() {
        dept1 = new Department("comp sci", "Computer Science");
        assertEquals(dept1.getCode(), "COMP SCI");
    }

    @Test
    void setUpperCaseCode() {
        dept1.setCode("comp sci");
        assertEquals(dept1.getCode(), "COMP SCI");
    }

    @Test
    void testSameObject() {
        Department dept1_copy = new Department(dept1.getCode(), dept1.getName());

        assertEquals(dept1, dept1_copy);
    }

    @Test
    void testDifferentObject() {
        assertNotEquals(dept1, dept2);
    }

    @Test
    void testHashCodeSameObject() {
        Department dept1_copy = new Department(dept1.getCode(), dept1.getName());

        assertEquals(dept1.hashCode(), dept1_copy.hashCode());
    }

    @Test
    void testHashCodeDifferentObject() {
        assertNotEquals(dept1.hashCode(), dept2.hashCode());
    }
}