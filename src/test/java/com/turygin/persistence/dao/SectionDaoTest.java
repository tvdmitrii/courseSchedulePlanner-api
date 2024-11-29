package com.turygin.persistence.dao;

import com.turygin.persistence.entity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Section DAO tests. */
public class SectionDaoTest {
    private static final Logger LOG = LogManager.getLogger(SectionDaoTest.class);

    /** List of sections. */
    private static final List<Section> SECTIONS = new ArrayList<>();

    /** DAO for working with sections in the database. */
    private static final Dao<Section> SECTION_DAO = new Dao<>(Section.class);

    /** DAO for working with courses in the database. */
    private static final CourseDao COURSE_DAO = new CourseDao();

    /** DAO for working with instructors in the database. */
    private static final Dao<Instructor> INSTRUCTOR_DAO = new Dao<>(Instructor.class);

    /** Initial section count. */
    private static final int INITIAL_SECTION_COUNT = 10;

    /** Reset database before each run. */
    @BeforeEach
    void resetDatabase() {
        if(!ResetDatabaseHelper.reset()) {
            LOG.error("Could not reset database!");
            throw new RuntimeException("Could not reset database!");
        }

        // Reset lists
        SECTIONS.clear();

        // Populate sections
        SECTIONS.addAll(SECTION_DAO.getAll());
        assertEquals(INITIAL_SECTION_COUNT, SECTIONS.size());

        // Sort sections by id to guarantee order for convenience
        SECTIONS.sort(Comparator.comparingLong(Section::getId));
    }

    /** Ensure section can be loaded by id. */
    @Test
    void getById() {
        Section section1 = SECTIONS.get(0);

        Section section = SECTION_DAO.getById(section1.getId());

        assertEquals(section1, section);
    }

    /** Ensure getting section by invalid id returns null. */
    @Test
    void getById_InvalidId() {
        Section section = SECTION_DAO.getById(INITIAL_SECTION_COUNT + 10);

        assertNull(section);
    }

    /** Ensure section can be updated. */
    @Test
    void update() {
        int newDaysOfWeek = Section.Day.THURSDAY.value & Section.Day.FRIDAY.value;
        Time newTime = Time.valueOf("11:45:00");
        Section section2 = SECTIONS.get(1);

        section2.setDaysOfWeek(newDaysOfWeek);
        section2.setFromTime(newTime);
        SECTION_DAO.update(section2);

        Section section = SECTION_DAO.getById(section2.getId());

        assertEquals(section2, section);
    }

    /** Ensure section can be inserted. */
    @Test
    void insert() {
        Course testCourse = COURSE_DAO.getById(7);
        Instructor instructor = INSTRUCTOR_DAO.getById(1);

        Section newSection = new Section(Section.Day.THURSDAY.value & Section.Day.FRIDAY.value,
                Time.valueOf("15:45:00"), Time.valueOf("17:30:00"));
        newSection.setCourse(testCourse);
        newSection.setInstructor(instructor);

        SECTION_DAO.insert(newSection);

        Section section = SECTION_DAO.getById(newSection.getId());
        testCourse = COURSE_DAO.getById(7);
        instructor = INSTRUCTOR_DAO.getById(1);

        assertEquals(newSection, section);
        assertEquals(section.getCourse(), testCourse);
        assertEquals(section.getInstructor(), instructor);
        assertTrue(section.getCourse().getSections().contains(section));
        assertTrue(section.getInstructor().getSections().contains(section));
    }

    /** Ensure section can be removed. */
    @Test
    void delete() {
        Section section4 = SECTIONS.get(3);
        long courseId = section4.getCourse().getId();
        long instructorId = section4.getInstructor().getId();

        SECTION_DAO.delete(section4);

        // Section removed
        Section section = SECTION_DAO.getById(section4.getId());
        assertNull(section);

        // Sections removed from course
        Course course = COURSE_DAO.getById(courseId);
        assertFalse(course.getSections().contains(section4));

        // Sections removed from instructor
        Instructor instructor = INSTRUCTOR_DAO.getById(instructorId);
        assertFalse(instructor.getSections().contains(section4));
    }

    /** Ensure all sections can be loaded. */
    @Test
    void getAll() {
        List<Section> sectionsFromDb = SECTION_DAO.getAll();

        assertNotNull(sectionsFromDb);
        assertEquals(SECTIONS.size(), sectionsFromDb.size());

        sectionsFromDb.sort(Comparator.comparingLong(Section::getId));

        for(int i = 0; i < SECTIONS.size(); i++) {
            assertEquals(SECTIONS.get(i), sectionsFromDb.get(i));
        }
    }

    /** Ensure sections can be searched by an exact property match. */
    @Test
    void getByPropertyEquals() {
        Section section1 = SECTIONS.get(0);
        List<Section> foundSections = SECTION_DAO.getByPropertyEquals("daysOfWeek", 10);

        assertNotNull(foundSections);
        assertEquals(4, foundSections.size());
    }

    /** Ensure searching for sections by invalid property match returns an empty list. */
    @Test
    void getByPropertyEquals_Missing() {
        List<Section> foundSections = SECTION_DAO.getByPropertyEquals("id", SECTIONS.size() + 1);

        assertEquals(0, foundSections.size());
    }
}
