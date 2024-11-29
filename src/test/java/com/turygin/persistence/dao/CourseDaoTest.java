package com.turygin.persistence.dao;

import com.turygin.persistence.entity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

/** Course DAO tests. */
public class CourseDaoTest {
    private static final Logger LOG = LogManager.getLogger(CourseDaoTest.class);

    /** List of courses. */
    private static final List<Course> COURSES = new ArrayList<>();

    /** List of sections associated with the first course in the list. */
    private static final List<Section> INTRO_DB_SECTIONS = new ArrayList<>();

    /** DAO for working with departments in the database. */
    private static final Dao<Department> DEPARTMENT_DAO = new Dao<>(Department.class);

    /** DAO for working with courses in the database. */
    private static final CourseDao COURSE_DAO = new CourseDao();

    /** DAO for working with sections in the database. */
    private static final Dao<Section> SECTION_DAO = new Dao<>(Section.class);

    /** Initial course count. */
    private static final int INITIAL_COURSE_COUNT = 8;

    /** Initial section count for the first course in the list. */
    private static final int INITIAL_INTRO_DB_SECTION_COUNT = 3;

    /** Reset database before each run. */
    @BeforeEach
    void resetDatabase() {
        if(!ResetDatabaseHelper.reset()) {
            LOG.error("Could not reset database!");
            throw new RuntimeException("Could not reset database!");
        }

        // Reset lists
        COURSES.clear();
        INTRO_DB_SECTIONS.clear();

        // Populate courses
        COURSES.addAll(COURSE_DAO.getAll());
        assertEquals(INITIAL_COURSE_COUNT, COURSES.size());

        // Sort courses by id to guarantee order for convenience
        COURSES.sort(Comparator.comparingLong(Course::getId));

        // Populate Introduction to Databases sections
        INTRO_DB_SECTIONS.addAll(COURSES.get(0).getSections());
        assertEquals(INITIAL_INTRO_DB_SECTION_COUNT, INTRO_DB_SECTIONS.size());

        // Sort Introduction to Databases sections by id to guarantee order for convenience
        INTRO_DB_SECTIONS.sort(Comparator.comparingLong(Section::getId));
    }

    /** Ensure course can be loaded by id. */
    @Test
    void getById() {
        Course course1 = COURSES.get(0);

        Course course = COURSE_DAO.getById(course1.getId());

        assertEquals(course1, course);
    }

    /** Ensure getting course by invalid id returns null. */
    @Test
    void getById_InvalidId() {
        Course course = COURSE_DAO.getById(INITIAL_COURSE_COUNT + 10);

        assertNull(course);
    }

    /** Ensure course can be updated. */
    @Test
    void update() {
        String newTitle = "Some other course";
        String newDescription = "Will teach some other thing.";
        Course course2 = COURSES.get(1);

        course2.setTitle(newTitle);
        course2.setDescription(newDescription);
        COURSE_DAO.update(course2);
        Course course = COURSE_DAO.getById(course2.getId());

        assertEquals(course2, course);
    }

    /** Ensure course can be inserted. */
    @Test
    void insert() {
        List<Department> deptList = DEPARTMENT_DAO.getByPropertyEquals("name", "English");
        Department english = deptList.get(0);
        Course newCourse =
                new Course("Composition II", "Even more writing.", 4, 200, english);

        COURSE_DAO.insert(newCourse);
        Course course = COURSE_DAO.getById(newCourse.getId());

        assertEquals(newCourse, course);
    }

    /** Ensure course can be inserted together with associated sections. */
    @Test
    void insertWithSection() {
        List<Department> deptList = DEPARTMENT_DAO.getByPropertyEquals("name", "English");
        Department english = deptList.get(0);
        Course newCourse =
                new Course("Composition II", "Even more writing.", 4, 200, english);
        Section newSection =
                new Section(Section.Day.MONDAY.value, Time.valueOf("07:00:00"), Time.valueOf("08:00:00"));
        newSection.setInstructor(english.getCourses().get(0).getSections().get(0).getInstructor());
        newCourse.addSection(newSection);

        COURSE_DAO.insert(newCourse);
        Course course = COURSE_DAO.getById(newCourse.getId());
        Section section = SECTION_DAO.getById(newSection.getId());

        assertEquals(newCourse, course);
        assertEquals(newSection, section);
    }

    /** Ensure that courses cannot have the same code (e.g. ENG 101). */
    @Test
    void insert_DuplicateCourseDesignation() {
        List<Department> deptList = DEPARTMENT_DAO.getByPropertyEquals("name", "English");
        Department english = deptList.get(0);
        Course newCourse =
                new Course("Composition II", "Even more writing.", 4, 101, english);

        assertThrows(ConstraintViolationException.class, () -> COURSE_DAO.insert(newCourse));
    }

    /** Ensure deleting a course deletes associated sections. */
    @Test
    void deleteWithSections() {
        Course course4 = COURSES.get(3);
        List<Section> sections = course4.getSections();

        COURSE_DAO.delete(course4);

        // Course removed
        Course course = COURSE_DAO.getById(course4.getId());
        assertNull(course);

        // Sections removed
        Section foundSection;
        for (Section section : sections) {
            foundSection = SECTION_DAO.getById(section.getId());
            assertNull(foundSection);
        }
    }

    /** Ensure all courses can be loaded. */
    @Test
    void getAll() {
        List<Course> coursesFromDb = COURSE_DAO.getAll();

        assertNotNull(coursesFromDb);
        assertEquals(COURSES.size(), coursesFromDb.size());

        coursesFromDb.sort(Comparator.comparingLong(Course::getId));

        for(int i = 0; i < COURSES.size(); i++) {
            assertEquals(COURSES.get(i), coursesFromDb.get(i));
        }
    }

    /** Ensure a course can be found via exact property match. */
    @Test
    void getByPropertyEquals() {
        Course course1 = COURSES.get(0);
        List<Course> foundCourses = COURSE_DAO.getByPropertyEquals("credits", 3);

        assertNotNull(foundCourses);
        assertEquals(6, foundCourses.size());
    }

    /** Ensure searching for a course by invalid property match returns an empty list. */
    @Test
    void getByPropertyEquals_Missing() {
        List<Course> foundCourses = COURSE_DAO.getByPropertyEquals("id", COURSES.size() + 1);

        assertEquals(0, foundCourses.size());
    }

    /** Ensure courses can be found by a property substring. */
    @Test
    void getByPropertySubstring() {
        List<Course> foundCourses = COURSE_DAO.getByPropertySubstring("title", "Introduction");

        assertNotNull(foundCourses);
        assertEquals(2, foundCourses.size());
    }

    /** Ensure find courses method works when only title substring is provided. */
    @Test
    void findCoursesByTitleOnly() {
        List<Course> foundCourses = COURSE_DAO.findCourses("intro",  -1);

        assertEquals(2, foundCourses.size());
    }

    /** Ensure find courses method works when only department id is provided. */
    @Test
    void findCoursesByDepartmentOnly() {
        List<Course> foundCourses = COURSE_DAO.findCourses("",  2);

        assertEquals(2, foundCourses.size());
    }

    /** Ensure find courses method works when both title substring and department ID are provided. */
    @Test
    void findCoursesByTitleAndDepartment() {
        List<Course> foundCourses = COURSE_DAO.findCourses("comp",  3);

        assertEquals(1, foundCourses.size());
    }

    /** Ensure find courses method returns all courses if not search parameters are provided. */
    @Test
    void findCoursesWithoutParameters() {
        List<Course> foundCourses = COURSE_DAO.findCourses("",  -1);

        assertEquals(INITIAL_COURSE_COUNT, foundCourses.size());
    }

    /** Ensure find courses method returns an empty list if no courses were found. */
    @Test
    void findCoursesNoResults() {
        List<Course> foundCourses = COURSE_DAO.findCourses("xyz",  12);

        assertEquals(0, foundCourses.size());
    }

    /** Ensure that associated course sections are properly loaded. */
    @Test
    void getAllSections() {
        Course course1 = COURSES.get(0);

        List<Section> sectionsFromDb = course1.getSections();
        assertEquals(INTRO_DB_SECTIONS.size(), sectionsFromDb.size());

        // Section are not necessarily ordered by ID.
        sectionsFromDb.sort(Comparator.comparingLong(Section::getId));

        for(int i = 0; i < INTRO_DB_SECTIONS.size(); i++) {
            assertEquals(INTRO_DB_SECTIONS.get(i), sectionsFromDb.get(i));
        }
    }

    /** Ensure a section can be added to a course. */
    @Test
    void addSection() {
        Course composition = COURSES.get(5);
        Section newSection = new Section(Section.Day.MONDAY.value,
                Time.valueOf("07:00:00"),
                Time.valueOf("08:00:00"));
        newSection.setInstructor(composition.getSections().get(0).getInstructor());
        composition.addSection(newSection);

        COURSE_DAO.update(composition);
        Course course = COURSE_DAO.getById(composition.getId());

        // Ensure the section is attached to the course
        assertTrue(course.getSections().contains(newSection));
    }

    /** Ensure a section can be removed. */
    @Test
    void removeSection() {
        Course composition = COURSES.get(5);
        Section sectionToRemove = composition.getSections().get(0);
        composition.removeSection(sectionToRemove);

        COURSE_DAO.update(composition);
        Course course = COURSE_DAO.getById(composition.getId());
        Section section = SECTION_DAO.getById(sectionToRemove.getId());

        // Ensure the section is not attached to the course
        assertFalse(course.getSections().contains(sectionToRemove));
        assertNull(section);
    }
}
