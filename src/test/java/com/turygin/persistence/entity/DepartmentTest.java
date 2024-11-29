package com.turygin.persistence.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Department entity tests. */
class DepartmentTest {

    private Department dept1;
    private Department dept2;

    /**
     * Resets departments before each test run.
     */
    @BeforeEach
    void resetDepartments() {
        dept1 = new Department("CS", "Computer Science");
        dept2 = new Department("ENG", "Engineering");
    }

    /** Ensure department code is converted to upper case. */
    @Test
    void setUpperCaseCode() {
        dept1.setCode("comp sci");
        assertEquals(dept1.getCode(), "COMP SCI");
    }

    /** Ensure deep object comparison returns true for a department and its copy. */
    @Test
    void testSameObject() {
        Department dept1_copy = new Department(dept1.getCode(), dept1.getName());

        assertEquals(dept1, dept1_copy);
    }

    /** Ensure deep comparison returns false for different departments. */
    @Test
    void testDifferentObject() {
        assertNotEquals(dept1, dept2);
    }

    /** Ensure that hash is the same for equal objects. */
    @Test
    void testHashCodeSameObject() {
        Department dept1_copy = new Department(dept1.getCode(), dept1.getName());

        assertEquals(dept1.hashCode(), dept1_copy.hashCode());
    }

    /** Ensure that hash is different for different objects. */
    @Test
    void testHashCodeDifferentObject() {
        assertNotEquals(dept1.hashCode(), dept2.hashCode());
    }
}