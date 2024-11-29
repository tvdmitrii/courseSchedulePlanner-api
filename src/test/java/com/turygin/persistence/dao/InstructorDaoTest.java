package com.turygin.persistence.dao;

import com.turygin.persistence.entity.Instructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Instructor DAO tests. */
class InstructorDaoTest {

    private static final Logger LOG = LogManager.getLogger(InstructorDaoTest.class);

    /** List of instructors. */
    private static final List<Instructor> INSTRUCTORS = new ArrayList<>();

    /** DAO for working with instructors in the database. */
    private static final Dao<Instructor> INSTRUCTOR_DAO = new Dao<>(Instructor.class);

    /** Initial instructor count. */
    private static final int INITIAL_INSTRUCTOR_COUNT = 4;

    /** Reset database before each run. */
    @BeforeEach
    void resetDatabase() {
        if(!ResetDatabaseHelper.reset()) {
            LOG.error("Could not reset database!");
            throw new RuntimeException("Could not reset database!");
        }

        // Reset lists
        INSTRUCTORS.clear();

        // Populate instructors
        INSTRUCTORS.addAll(INSTRUCTOR_DAO.getAll());
        assertEquals(INITIAL_INSTRUCTOR_COUNT, INSTRUCTORS.size());

        // Sort by id to guarantee order for convenience
        INSTRUCTORS.sort(Comparator.comparingLong(Instructor::getId));
    }

    /** Ensure instructor can be loaded by id. */
    @Test
    void getById() {
        Instructor instructor1 = INSTRUCTORS.get(0);

        Instructor instructor = INSTRUCTOR_DAO.getById(instructor1.getId());

        assertEquals(instructor1, instructor);
    }

    /** Ensure getting instructor by invalid id returns null. */
    @Test
    void getById_InvalidId() {
        Instructor instructor = INSTRUCTOR_DAO.getById(INSTRUCTORS.size() + 10);

        assertNull(instructor);
    }

    /** Ensure instructor can be updated. */
    @Test
    void update() {
        String newFirstName = "Collin";
        String newLastName = "Copper";
        Instructor instructor2 = INSTRUCTORS.get(1);

        instructor2.setFirstName(newFirstName);
        instructor2.setLastName(newLastName);
        INSTRUCTOR_DAO.update(instructor2);
        Instructor instructor = INSTRUCTOR_DAO.getById(instructor2.getId());

        assertEquals(instructor2, instructor);
    }

    /** Ensure instructor can be inserted. */
    @Test
    void insert() {
        Instructor newInstructor = new Instructor("Walter", "Tobias");

        INSTRUCTOR_DAO.insert(newInstructor);
        Instructor instructor = INSTRUCTOR_DAO.getById(newInstructor.getId());

        assertEquals(newInstructor, instructor);
    }

    /** Ensure duplicate instructor names are not allowed. */
    @Test
    void insert_DuplicateName() {
        Instructor instructor3 = INSTRUCTORS.get(2);

        Instructor newInstructor = new Instructor(instructor3.getFirstName(), instructor3.getLastName());

        assertThrows(ConstraintViolationException.class, () -> INSTRUCTOR_DAO.insert(newInstructor));
    }

    /** Ensure instructor can be removed. */
    @Test
    void delete() {
        Instructor instructor4 = INSTRUCTORS.get(INITIAL_INSTRUCTOR_COUNT - 1);

        INSTRUCTOR_DAO.delete(instructor4);
        Instructor instructor = INSTRUCTOR_DAO.getById(instructor4.getId());
        INSTRUCTORS.remove(instructor4);

        assertNull(instructor);
    }

    /** Ensure all instructors can be loaded. */
    @Test
    void getAll() {
        List<Instructor> instsFromDb = INSTRUCTOR_DAO.getAll();

        assertNotNull(instsFromDb);
        assertEquals(instsFromDb.size(), INSTRUCTORS.size());

        // Instructors are not ordered by ID.
        instsFromDb.sort(Comparator.comparingLong(Instructor::getId));

        for(int i = 0; i < INSTRUCTORS.size(); i++) {
            assertEquals(INSTRUCTORS.get(i), instsFromDb.get(i));
        }
    }

    /** Ensure instructors can be searched by an exact property match. */
    @Test
    void getByPropertyEquals_String() {
        Instructor instructor1 = INSTRUCTORS.get(0);
        List<Instructor> foundInsts = INSTRUCTOR_DAO.getByPropertyEquals("lastName", instructor1.getLastName());

        assertNotNull(foundInsts);
        assertEquals(1, foundInsts.size());
        assertEquals(instructor1, foundInsts.get(0));
    }

    /** Ensure searching for instructors by invalid property match returns an empty list. */
    @Test
    void getByPropertyEquals_Missing() {
        List<Instructor> foundInsts = INSTRUCTOR_DAO.getByPropertyEquals("id", INSTRUCTORS.size() + 1);

        assertEquals(0, foundInsts.size());
    }

    /** Ensure instructors can be found by a property substring. */
    @Test
    void getByPropertySubstring() {
        List<Instructor> foundInsts = INSTRUCTOR_DAO.getByPropertySubstring("firstName", "ie");

        assertNotNull(foundInsts);
        assertEquals(1, foundInsts.size());
    }
}