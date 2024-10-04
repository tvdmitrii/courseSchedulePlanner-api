package com.turygin.persistence.dao;

import com.turygin.persistence.entity.Course;
import com.turygin.persistence.entity.Department;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentDaoTest {

    private static final Logger LOG = LogManager.getLogger(DepartmentDaoTest.class);
    private static final List<Department> DEPARTMENTS = new ArrayList<>();
    private static final List<Course> CS_COURSES = new ArrayList<>();
    private static final Dao<Department> DEPARTMENT_DAO = new Dao<>(Department.class);
    private static final Dao<Course> COURSE_DAO = new Dao<>(Course.class);
    private static final int INITIAL_DEPARTMENT_COUNT = 5;
    private static final int INITIAL_CS_COURSE_COUNT = 3;

    @BeforeEach
    void resetDatabase() {
        if(!ResetDatabaseHelper.reset()) {
            LOG.error("Could not reset database!");
            throw new RuntimeException("Could not reset database!");
        }

        // Reset lists
        DEPARTMENTS.clear();
        CS_COURSES.clear();

        // Populate departments
        DEPARTMENTS.addAll(DEPARTMENT_DAO.getAll());
        assertEquals(INITIAL_DEPARTMENT_COUNT, DEPARTMENTS.size());

        // Sort departments by id to guarantee order for convenience
        DEPARTMENTS.sort(Comparator.comparingLong(Department::getId));

        // Populate CS courses
        CS_COURSES.addAll(DEPARTMENTS.get(0).getCourses());
        assertEquals(INITIAL_CS_COURSE_COUNT, CS_COURSES.size());

        // Sort cs courses by id to guarantee order for convenience
        CS_COURSES.sort(Comparator.comparingLong(Course::getId));
    }

    @Test
    void getById() {
        Department department1 = DEPARTMENTS.get(0);

        Department department = DEPARTMENT_DAO.getById(department1.getId());

        assertEquals(department1, department);
    }

    @Test
    void getById_InvalidId() {
        Department department = DEPARTMENT_DAO.getById(INITIAL_DEPARTMENT_COUNT + 10);

        assertNull(department);
    }

    @Test
    void update() {
        String newCode = "ME";
        String newName = "Mechanical Engineering";
        Department department2 = DEPARTMENTS.get(1);

        department2.setCode(newCode);
        department2.setName(newName);
        DEPARTMENT_DAO.update(department2);
        Department department = DEPARTMENT_DAO.getById(department2.getId());

        assertEquals(department2, department);
    }

    @Test
    void insert() {
        Department newDepartment = new Department("CHEM", "Chemistry");

        DEPARTMENT_DAO.insert(newDepartment);
        Department department = DEPARTMENT_DAO.getById(newDepartment.getId());

        assertEquals(newDepartment, department);
    }

    @Test
    void insert_DuplicateName() {
        Department department3 = DEPARTMENTS.get(2);

        Department newDepartment = new Department(department3.getCode() + "_code", department3.getName());

        assertThrows(ConstraintViolationException.class, () -> DEPARTMENT_DAO.insert(newDepartment));
    }

    @Test
    void insert_DuplicateCode() {
        Department department3 = DEPARTMENTS.get(2);

        Department newDepartment = new Department(department3.getCode(), department3.getName() + "_name");

        assertThrows(ConstraintViolationException.class, () -> DEPARTMENT_DAO.insert(newDepartment));
    }

    @Test
    void delete() {
        Department department4 = DEPARTMENTS.get(3);

        DEPARTMENT_DAO.delete(department4);
        Department department = DEPARTMENT_DAO.getById(department4.getId());
        DEPARTMENTS.remove(department4);

        assertNull(department);
    }

    @Test
    void getAll() {
        List<Department> deptsFromDb = DEPARTMENT_DAO.getAll();

        assertNotNull(deptsFromDb);
        assertEquals(deptsFromDb.size(), DEPARTMENTS.size());

        deptsFromDb.sort(Comparator.comparingLong(Department::getId));

        for(int i = 0; i < DEPARTMENTS.size(); i++) {
            assertEquals(DEPARTMENTS.get(i), deptsFromDb.get(i));
        }
    }

    @Test
    void getByPropertyEquals_String() {
        Department department1 = DEPARTMENTS.get(0);
        List<Department> foundDepts = DEPARTMENT_DAO.getByPropertyEquals("code", department1.getCode());

        assertNotNull(foundDepts);
        assertEquals(foundDepts.size(), 1);
        assertEquals(foundDepts.get(0), department1);
    }

    @Test
    void getByPropertyEquals_Missing() {
        List<Department> foundDepts = DEPARTMENT_DAO.getByPropertyEquals("id", DEPARTMENTS.size() + 1);

        assertEquals(foundDepts.size(), 0);
    }

    @Test
    void getByPropertySubstring() {
        List<Department> foundDepts = DEPARTMENT_DAO.getByPropertySubstring("name", "Eng");

        assertNotNull(foundDepts);
        assertEquals(foundDepts.size(), 2);
    }

    @Test
    void getAllCourses() {
        Department department1 = DEPARTMENTS.get(0);

        Department department = DEPARTMENT_DAO.getById(department1.getId());
        List<Course> coursesFromDb = department.getCourses();
        assertEquals(CS_COURSES.size(), coursesFromDb.size());

        // Course are not necessarily ordered by ID.
        coursesFromDb.sort(Comparator.comparingLong(Course::getId));

        for(int i = 0; i < CS_COURSES.size(); i++) {
            assertEquals(CS_COURSES.get(i), coursesFromDb.get(i));
        }
    }

    @Test
    void addCourse_NoSections() {
        Department engineering = DEPARTMENT_DAO.getById(2);
        Course statics = new Course("Statics", "Teaches about equilibrium force balance.",
                3, 123);
        engineering.addCourse(statics);
        DEPARTMENT_DAO.update(engineering);

        // Make sure department was updated
        Department department = DEPARTMENT_DAO.getById(engineering.getId());
        assertEquals(3, department.getCourses().size());

        // Find course by title
        List<Course> courseList = COURSE_DAO.getByPropertyEquals("title", statics.getTitle());
        assertEquals(1, courseList.size());
        Course course = courseList.get(0);
        assertEquals(statics, course);

        // Ensure the course is attached to the department
        assertTrue(department.getCourses().contains(course));
    }

    @Test
    void removeCourse_NoSections() {
        List<Department> deptList = DEPARTMENT_DAO.getByPropertyEquals("code", "TEST");
        assertEquals(1, deptList.size());
        Department testDepartment = deptList.get(0);

        Course testCourse2 = testDepartment.getCourses().get(1);
        testDepartment.removeCourse(testCourse2);
        DEPARTMENT_DAO.update(testDepartment);

        // Ensure the course was removed from the department
        testDepartment = DEPARTMENT_DAO.getById(testDepartment.getId());
        assertEquals(1, testDepartment.getCourses().size());

        // Ensure course was removed from the database
        Course course = COURSE_DAO.getById(testCourse2.getId());
        assertNull(course);
    }

    @Test
    void removeDepartmentWithCourses_NoSections() {
        List<Department> deptList = DEPARTMENT_DAO.getByPropertyEquals("code", "TEST");
        assertEquals(1, deptList.size());
        Department testDepartment = deptList.get(0);

        DEPARTMENT_DAO.delete(testDepartment);

        // Ensure the department was removed
        deptList = DEPARTMENT_DAO.getByPropertyEquals("code", "TEST");
        assertEquals(0, deptList.size());

        // Ensure courses were removed from the database
        List<Course> courses = COURSE_DAO.getByPropertySubstring("title", "Test");
        assertEquals(0, courses.size());
    }

    @Test
    void addDepartmentWithCourses_NoSections() {
        Department newDepartment = new Department("JUNIT", "Unit Testing");
        newDepartment.addCourse(new Course("Unit 1", "", 3, 100));
        newDepartment.addCourse(new Course("Unit 2", "", 3, 200));

        DEPARTMENT_DAO.insert(newDepartment);

        // Check department
        Department department = DEPARTMENT_DAO.getById(newDepartment.getId());
        assertEquals(newDepartment, department);
        assertEquals(newDepartment.getCourses().size(), department.getCourses().size());

        // Check courses
        List<Course> courses = COURSE_DAO.getByPropertySubstring("title", "Unit");
        assertEquals(2, courses.size());
    }
}