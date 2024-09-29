package com.turygin.persistence.dao;

import com.turygin.persistence.entity.Course;
import com.turygin.persistence.entity.Department;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentDaoTest {

    private static final Logger logger = LogManager.getLogger(DepartmentDaoTest.class);
    private static final List<Department> departments = new ArrayList<>();
    private static final List<Course> csCourses = new ArrayList<>();
    private final Dao<Department> departmentDao = new Dao<>(Department.class);

    @BeforeAll
    static void resetDatabase() {
        if(!ResetDatabaseHelper.reset()) {
            logger.error("Could not reset database!");
            throw new RuntimeException("Could not reset database!");
        }

        // Populate departments
        departments.add(new Department("CS", "Computer Science"));
        departments.add(new Department("ENG", "Engineering"));
        departments.add(new Department("ENGL", "English"));
        departments.add(new Department("UD", "Unused Department"));
        for(int i = 0; i < departments.size(); i++) {
            departments.get(i).setId(i + 1);
        }

        // Populate CS department with courses
        csCourses.add(new Course("Introduction to Databases",
                "An introduction to databases course explores the fundamental concepts of database design, management, and querying, with a focus on relational databases and SQL.",
                3, 121, departments.get(0)));
        csCourses.add(new Course("Introduction to Programming",
                "An introduction to programming course teaches the basics of coding, problem-solving, and algorithmic thinking, using a beginner-friendly language to build foundational skills in software development.",
                3, 101, departments.get(0)));
        csCourses.add(new Course("Advanced Python",
                "An advanced Python course dives into complex programming concepts, including object-oriented design, multithreading, data structures, and advanced libraries, empowering students to build efficient, scalable applications.",
                4, 202, departments.get(0)));

        for(int i = 0; i < csCourses.size(); i++) {
            csCourses.get(i).setId(i + 1);
        }

        departments.get(0).setCourses(csCourses);
    }


    @Test
    void getById() {
        Department department1 = departments.get(0);

        Department department = departmentDao.getById(department1.getId());

        assertEquals(department1, department);
    }

    @Test
    void getById_InvalidId() {
        Department department = departmentDao.getById(departments.size() + 1);

        assertNull(department);
    }

    @Test
    void update() {
        String newCode = "ME";
        String newName = "Mechanical Engineering";
        Department department2 = departments.get(1);

        department2 = departmentDao.getById(department2.getId()); // get detached instance
        department2.setCode(newCode);
        department2.setName(newName);
        departmentDao.update(department2);
        Department department = departmentDao.getById(department2.getId());

        assertEquals(department2, department);
    }

    @Test
    void insert() {
        Department newDepartment = new Department("CHEM", "Chemistry");

        departmentDao.insert(newDepartment);
        Department department = departmentDao.getById(newDepartment.getId());

        assertEquals(newDepartment, department);
    }

    @Test
    void insert_DuplicateName() {
        Department department3 = departments.get(2);

        Department newDepartment = new Department(department3.getCode() + "_code", department3.getName());

        assertThrows(ConstraintViolationException.class, () -> departmentDao.insert(newDepartment));
    }

    @Test
    void insert_DuplicateCode() {
        Department department3 = departments.get(2);

        Department newDepartment = new Department(department3.getCode(), department3.getName() + "_name");

        assertThrows(ConstraintViolationException.class, () -> departmentDao.insert(newDepartment));
    }

    @Test
    void delete() {
        Department department4 = departments.get(3);

        departmentDao.delete(department4);
        Department department = departmentDao.getById(department4.getId());
        departments.remove(department4);

        assertNull(department);
    }

    @Test
    void getAll() {
        List<Department> deptsFromDb = departmentDao.getAll();

        assertNotNull(deptsFromDb);
        assertEquals(deptsFromDb.size(), departments.size());
        for(int i = 0; i < departments.size(); i++) {
            assertEquals(departments.get(i), deptsFromDb.get(i));
        }
    }

    @Test
    void getByPropertyEquals_String() {
        Department department1 = departments.get(0);
        List<Department> foundDepts = departmentDao.getByPropertyEquals("code", department1.getCode());

        assertNotNull(foundDepts);
        assertEquals(foundDepts.size(), 1);
        assertEquals(foundDepts.get(0), department1);
    }

    @Test
    void getByPropertyEquals_Missing() {
        List<Department> foundDepts = departmentDao.getByPropertyEquals("id", departments.size() + 1);

        assertEquals(foundDepts.size(), 0);
    }

    @Test
    void getByPropertySubstring() {
        List<Department> foundDepts = departmentDao.getByPropertySubstring("name", "Eng");

        assertNotNull(foundDepts);
        assertEquals(foundDepts.size(), 2);
    }

    @Test
    void getAllCourses() {
        Department department1 = departments.get(0);

        Department department = departmentDao.getById(department1.getId());
        assertEquals(csCourses.size(), department.getCourses().size());

        // Course are not necessarily ordered by ID.
        List<Course> coursesFromDb = department.getCourses();
        coursesFromDb.sort((c1, c2) -> {
            if( c1.getId() == c2.getId() ) return 0;
            return c1.getId() > c2.getId() ? 1 : -1;
        });

        for(int i = 0; i < csCourses.size(); i++) {
            assertEquals(csCourses.get(i), coursesFromDb.get(i));
        }
    }

    @Test
    void addCourse() {
        Department engineering = departments.get(1);
        Course statics = new Course("Statics", "Teaches about equilibrium force balance.",
                3, 123);
        engineering.addCourse(statics);
        departmentDao.update(engineering);

        Department department = departmentDao.getById(engineering.getId());
        assertEquals(3, department.getCourses().size());

        // Find course by name and update id
        boolean found = false;
        for (Course course : department.getCourses()) {
            if (course.getTitle().equals(statics.getTitle())) {
                found = true;
                statics.setId(course.getId());
                break;
            }
        }
        assertTrue(found);
        assertTrue(department.getCourses().contains(statics));
    }
}