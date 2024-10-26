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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CartCourseTest {
    private static final Logger LOG = LogManager.getLogger(CartCourseTest.class);
    private static final List<CartCourse> CART_COURSES = new ArrayList<>();
    private static final Dao<CartCourse> CART_COURSE_DAO = new Dao<>(CartCourse.class);
    private static final CourseDao COURSE_DAO = new CourseDao();
    private static final Dao<User> USER_DAO = new Dao<>(User.class);
    private static final int INITIAL_CART_COURSE_COUNT = 3;

    @BeforeEach
    void resetDatabase() {
        if(!ResetDatabaseHelper.reset()) {
            LOG.error("Could not reset database!");
            throw new RuntimeException("Could not reset database!");
        }

        // Reset lists
        CART_COURSES.clear();

        // Populate cartCourses
        CART_COURSES.addAll(CART_COURSE_DAO.getAll());
        assertEquals(INITIAL_CART_COURSE_COUNT, CART_COURSES.size());

        // Sort cart courses by id to guarantee order for convenience
        CART_COURSES.sort(Comparator.comparingLong(CartCourse::getId));
    }

    @Test
    void getById() {
        CartCourse cartCourse1 = CART_COURSES.get(0);

        CartCourse cartCourse = CART_COURSE_DAO.getById(cartCourse1.getId());

        assertEquals(cartCourse1, cartCourse);
    }

    @Test
    void getById_InvalidId() {
        CartCourse cartCourse = CART_COURSE_DAO.getById(INITIAL_CART_COURSE_COUNT + 10);

        assertNull(cartCourse);
    }

    @Test
    void insert() {
        Course course = COURSE_DAO.getById(7);
        User user = USER_DAO.getById(1);
        CartCourse newCartCourse = new CartCourse(user, course);

        CART_COURSE_DAO.insert(newCartCourse);

        CartCourse cartCourse = CART_COURSE_DAO.getById(newCartCourse.getId());
        course = COURSE_DAO.getById(7);
        user = USER_DAO.getById(1);

        assertEquals(newCartCourse, cartCourse);
        assertEquals(cartCourse.getUser(), user);
        assertEquals(cartCourse.getCourse(), course);
        assertTrue(user.getCoursesInCart().contains(cartCourse));
    }

    @Test
    void delete() {
        CartCourse cartCourse1 = CART_COURSES.get(1);
        long courseId = cartCourse1.getCourse().getId();
        long userId = cartCourse1.getUser().getId();

        CART_COURSE_DAO.delete(cartCourse1);

        // CartCourse removed
        CartCourse cartCourse = CART_COURSE_DAO.getById(cartCourse1.getId());
        assertNull(cartCourse);

        // CartCourse removed from user's cart
        User user = USER_DAO.getById(userId);
        assertFalse(user.getCoursesInCart().contains(cartCourse1));

        // The underlying course was not affected
        Course course = COURSE_DAO.getById(courseId);
        assertNotNull(course);
    }

    @Test
    void getAll() {
        List<CartCourse> cartCoursesFromDb = CART_COURSE_DAO.getAll();

        assertNotNull(cartCoursesFromDb);
        assertEquals(CART_COURSES.size(), cartCoursesFromDb.size());

        cartCoursesFromDb.sort(Comparator.comparingLong(CartCourse::getId));

        for(int i = 0; i < CART_COURSES.size(); i++) {
            assertEquals(CART_COURSES.get(i), cartCoursesFromDb.get(i));
        }
    }
}
