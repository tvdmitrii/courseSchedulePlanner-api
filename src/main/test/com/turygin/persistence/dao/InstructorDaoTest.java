package com.turygin.persistence.dao;

import com.turygin.persistence.entity.Instructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InstructorDaoTest {

    private static final Logger logger = LogManager.getLogger(InstructorDaoTest.class);
    private static final List<Instructor> instructors = new ArrayList<>();
    private final Dao<Instructor> instructorDao = new Dao<>(Instructor.class);

    @BeforeAll
    static void resetDatabase() {
        if(!ResetDatabaseHelper.reset()) {
            logger.error("Could not reset database!");
            throw new RuntimeException("Could not reset database!");
        }

        // Populate instructors
        instructors.add(new Instructor("Mattie", "Carpenter"));
        instructors.add(new Instructor("Clinton", "Edwards"));
        instructors.add(new Instructor("Raymond", "Walker"));
        instructors.add(new Instructor("Unused", "Instructor"));
        for(int i = 0; i < instructors.size(); i++) {
            instructors.get(i).setId(i + 1);
        }
    }


    @Test
    void getById() {
        Instructor instructor1 = instructors.get(0);

        Instructor instructor = instructorDao.getById(instructor1.getId());

        assertEquals(instructor1, instructor);
    }

    @Test
    void getById_InvalidId() {
        Instructor instructor = instructorDao.getById(instructors.size() + 1);

        assertNull(instructor);
    }

    @Test
    void update() {
        String newFirstName = "Collin";
        String newLastName = "Copper";
        Instructor instructor2 = instructors.get(1);

        instructor2 = instructorDao.getById(instructor2.getId()); // get detached instance
        instructor2.setFirstName(newFirstName);
        instructor2.setLastName(newLastName);
        instructorDao.update(instructor2);
        Instructor instructor = instructorDao.getById(instructor2.getId());

        assertEquals(instructor2, instructor);
    }

    @Test
    void insert() {
        Instructor newInstructor = new Instructor("Walter", "Tobias");

        instructorDao.insert(newInstructor);
        Instructor instructor = instructorDao.getById(newInstructor.getId());

        assertEquals(newInstructor, instructor);
    }

    @Test
    void insert_DuplicateName() {
        Instructor instructor3 = instructors.get(2);

        Instructor newInstructor = new Instructor(instructor3.getFirstName(), instructor3.getLastName());

        assertThrows(ConstraintViolationException.class, () -> instructorDao.insert(newInstructor));
    }

    @Test
    void delete() {
        Instructor instructor4 = instructors.get(3);

        instructorDao.delete(instructor4);
        Instructor instructor = instructorDao.getById(instructor4.getId());
        instructors.remove(instructor4);

        assertNull(instructor);
    }

    @Test
    void getAll() {
        List<Instructor> instsFromDb = instructorDao.getAll();

        assertNotNull(instsFromDb);
        assertEquals(instsFromDb.size(), instructors.size());

        // Instructors are not ordered by ID.
        instsFromDb.sort((i1, i2) -> {
            if( i1.getId() == i2.getId() ) return 0;
            return i1.getId() > i2.getId() ? 1 : -1;
        });

        for(int i = 0; i < instructors.size(); i++) {
            assertEquals(instructors.get(i), instsFromDb.get(i));
        }
    }

    @Test
    void getByPropertyEquals_String() {
        Instructor instructor1 = instructors.get(0);
        List<Instructor> foundInsts = instructorDao.getByPropertyEquals("lastName", instructor1.getLastName());

        assertNotNull(foundInsts);
        assertEquals(foundInsts.size(), 1);
        assertEquals(foundInsts.get(0), instructor1);
    }

    @Test
    void getByPropertyEquals_Missing() {
        List<Instructor> foundInsts = instructorDao.getByPropertyEquals("id", instructors.size() + 1);

        assertEquals(foundInsts.size(), 0);
    }

    @Test
    void getByPropertySubstring() {
        List<Instructor> foundInsts = instructorDao.getByPropertySubstring("firstName", "ie");

        assertNotNull(foundInsts);
        assertEquals(foundInsts.size(), 1);
    }
}