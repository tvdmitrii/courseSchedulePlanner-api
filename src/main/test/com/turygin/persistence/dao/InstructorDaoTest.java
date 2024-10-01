package com.turygin.persistence.dao;

import com.turygin.persistence.entity.Instructor;
import com.turygin.persistence.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InstructorDaoTest {

    private static final Logger LOG = LogManager.getLogger(InstructorDaoTest.class);
    private static final List<Instructor> INSTRUCTORS = new ArrayList<>();
    private static final Dao<Instructor> INSTRUCTOR_DAO = new Dao<>(Instructor.class);
    private static final int INITIAL_INSTRUCTOR_COUNT = 4;

    @BeforeAll
    static void resetDatabase() {
        if(!ResetDatabaseHelper.reset()) {
            LOG.error("Could not reset database!");
            throw new RuntimeException("Could not reset database!");
        }

        // Populate instructors
        INSTRUCTORS.addAll(INSTRUCTOR_DAO.getAll());
        assertEquals(INITIAL_INSTRUCTOR_COUNT, INSTRUCTORS.size());

        // Sort by id to guarantee order for convenience
        INSTRUCTORS.sort(Comparator.comparingLong(Instructor::getId));
    }


    @Test
    void getById() {
        Instructor instructor1 = INSTRUCTORS.get(0);

        Instructor instructor = INSTRUCTOR_DAO.getById(instructor1.getId());

        assertEquals(instructor1, instructor);
    }

    @Test
    void getById_InvalidId() {
        Instructor instructor = INSTRUCTOR_DAO.getById(INSTRUCTORS.size() + 10);

        assertNull(instructor);
    }

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

    @Test
    void insert() {
        Instructor newInstructor = new Instructor("Walter", "Tobias");

        INSTRUCTOR_DAO.insert(newInstructor);
        Instructor instructor = INSTRUCTOR_DAO.getById(newInstructor.getId());

        assertEquals(newInstructor, instructor);
    }

    @Test
    void insert_DuplicateName() {
        Instructor instructor3 = INSTRUCTORS.get(2);

        Instructor newInstructor = new Instructor(instructor3.getFirstName(), instructor3.getLastName());

        assertThrows(ConstraintViolationException.class, () -> INSTRUCTOR_DAO.insert(newInstructor));
    }

    @Test
    void delete() {
        Instructor instructor4 = INSTRUCTORS.get(INITIAL_INSTRUCTOR_COUNT - 1);

        INSTRUCTOR_DAO.delete(instructor4);
        Instructor instructor = INSTRUCTOR_DAO.getById(instructor4.getId());
        INSTRUCTORS.remove(instructor4);

        assertNull(instructor);
    }

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

    @Test
    void getByPropertyEquals_String() {
        Instructor instructor1 = INSTRUCTORS.get(0);
        List<Instructor> foundInsts = INSTRUCTOR_DAO.getByPropertyEquals("lastName", instructor1.getLastName());

        assertNotNull(foundInsts);
        assertEquals(foundInsts.size(), 1);
        assertEquals(foundInsts.get(0), instructor1);
    }

    @Test
    void getByPropertyEquals_Missing() {
        List<Instructor> foundInsts = INSTRUCTOR_DAO.getByPropertyEquals("id", INSTRUCTORS.size() + 1);

        assertEquals(foundInsts.size(), 0);
    }

    @Test
    void getByPropertySubstring() {
        List<Instructor> foundInsts = INSTRUCTOR_DAO.getByPropertySubstring("firstName", "ie");

        assertNotNull(foundInsts);
        assertEquals(foundInsts.size(), 1);
    }
}