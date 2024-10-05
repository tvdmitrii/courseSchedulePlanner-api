package com.turygin.persistence.dao;

import com.turygin.persistence.entity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class UserDaoTest {

    private static final Logger LOG = LogManager.getLogger(UserDaoTest.class);
    private static final List<User> USERS = new ArrayList<>();
    private static final Dao<User> USER_DAO = new Dao<>(User.class);
    private static final Dao<CartCourse> CART_COURSE_DAO = new Dao<>(CartCourse.class);
    private static final Dao<CartSection> CART_SECTION_DAO = new Dao<>(CartSection.class);
    private static final Dao<Course> COURSE_DAO = new Dao<>(Course.class);
    private static final Dao<Section> SECTION_DAO = new Dao<>(Section.class);
    private static final Dao<Schedule> SCHEDULE_DAO = new Dao<>(Schedule.class);
    private static final Dao<ScheduleSection> SCHEDULE_SECTION_DAO = new Dao<>(ScheduleSection.class);
    private static final int INITIAL_USER_COUNT = 5;

    @BeforeEach
    void resetDatabase() {
        if(!ResetDatabaseHelper.reset()) {
            LOG.error("Could not reset database!");
            throw new RuntimeException("Could not reset database!");
        }

        // Reset lists
        USERS.clear();

        // Populate users
        USERS.addAll(USER_DAO.getAll());
        assertEquals(INITIAL_USER_COUNT, USERS.size());

        // Sort by id to guarantee order for convenience
        USERS.sort(Comparator.comparingLong(User::getId));
    }
    
    @Test
    void getById() {
        User user1 = USERS.get(0);

        User user = USER_DAO.getById(user1.getId());

        assertNotNull(user);
        assertEquals(user1, user);
    }

    @Test
    void getById_InvalidId() {
        User user = USER_DAO.getById(INITIAL_USER_COUNT + 10);

        assertNull(user);
    }

    @Test
    void update() {
        String newLastName = "Thompson";
        User user2 = USERS.get(1);

        user2.setLastName(newLastName);
        USER_DAO.update(user2);
        User user = USER_DAO.getById(user2.getId());

        assertEquals(user2, user);
    }

    @Test
    void insert() {
        User newUser = new User("Another", "Person", "another.person@example.com", User.Type.USER);

        USER_DAO.insert(newUser);
        User user = USER_DAO.getById(newUser.getId());

        assertEquals(newUser, user);
    }

    @Test
    void insert_DuplicateEmail() {
        User user3 = USERS.get(2);

        User newUser = new User(user3.getFirstName(), user3.getLastName(), user3.getEmail(), user3.getRole());

        assertThrows(ConstraintViolationException.class, () -> USER_DAO.insert(newUser));
    }

    @Test
    void delete() {
        User user4 = USERS.get(INITIAL_USER_COUNT - 1);

        USER_DAO.delete(user4);
        User user = USER_DAO.getById(user4.getId());
        USERS.remove(user4);

        assertNull(user);
    }

    @Test
    void getAll() {
        List<User> usersFromDb = USER_DAO.getAll();

        assertNotNull(usersFromDb);
        assertEquals(usersFromDb.size(), USERS.size());
        usersFromDb.sort(Comparator.comparingLong(User::getId));

        for(int i = 0; i < USERS.size(); i++) {
            assertEquals(USERS.get(i), usersFromDb.get(i));
        }
    }

    @Test
    void getByPropertyEquals_String() {
        User user1 = USERS.get(0);
        List<User> foundUsers = USER_DAO.getByPropertyEquals("firstName", user1.getFirstName());

        assertNotNull(foundUsers);
        assertEquals(1, foundUsers.size());
        assertEquals(user1, foundUsers.get(0));
    }

    @Test
    void getByPropertyEquals_Long() {
        User user2 = USERS.get(1);
        List<User> foundUsers = USER_DAO.getByPropertyEquals("id", user2.getId());

        assertNotNull(foundUsers);
        assertEquals(1, foundUsers.size());
        assertEquals(user2, foundUsers.get(0));
    }

    @Test
    void getByPropertyEquals_Missing() {
        List<User> foundUsers = USER_DAO.getByPropertyEquals("id", 1000);

        assertEquals(0, foundUsers.size());
    }

    @Test
    void getByPropertySubstring() {
        List<User> foundUsers = USER_DAO.getByPropertySubstring("lastName", "in");

        assertNotNull(foundUsers);
        assertEquals(2, foundUsers.size());
    }

    @Test
    void addCourseWithSectionsToCart() {
        User user1 = USERS.get(3);
        Course pythonCourse = COURSE_DAO.getById(3);
        List<Section> courseSections = pythonCourse.getSections();

        // New cart course
        CartCourse cartCourse = new CartCourse(user1, pythonCourse);
        // Add all sections to cart
        courseSections.forEach(cartCourse::addSection);
        // Add course with sections to cart
        user1.addCourseToCart(cartCourse);
        // Save sections for comparison
        List<CartSection> cartCourseSections = cartCourse.getSections();
        cartCourseSections.sort(Comparator.comparingLong(CartSection::getId));

        USER_DAO.update(user1);
        User user = USER_DAO.getById(user1.getId());

        // Has course in cart
        assertTrue(user.getCoursesInCart().contains(cartCourse));

        // Has the right course
        CartCourse course = user.getCoursesInCart().get(0);
        assertEquals(pythonCourse, course.getCourse());

        // Has all the sections
        List<CartSection> sections =  course.getSections();
        sections.sort(Comparator.comparingLong(CartSection::getId));
        for (int i = 0; i < sections.size(); i++) {
            assertEquals(cartCourseSections.get(i), sections.get(i));
        }
    }

    @Test
    void removeCourseWithSectionsFromCart() {
        User user1 = USERS.get(0);
        CartCourse cartCourse = user1.getCoursesInCart().get(0);
        List<CartSection> cartSections = cartCourse.getSections();

        user1.removeCourseFromCart(cartCourse);
        USER_DAO.update(user1);

        // Course removed from users cart
        User user = USER_DAO.getById(user1.getId());
        assertFalse(user.getCoursesInCart().contains(cartCourse));

        // Double-check the cart table
        CartCourse course = CART_COURSE_DAO.getById(cartCourse.getId());
        assertNull(course);

        // Check that sections where removed
        for (CartSection section : cartSections) {
            assertNull(CART_SECTION_DAO.getById(section.getId()));
        }
    }

    @Test
    void addNewScheduleWithSections() {
        User user1 = USERS.get(3);
        Schedule schedule = new Schedule();
        List<Section> sections = SECTION_DAO.getAll();

        sections.forEach(schedule::addSection);
        List<ScheduleSection> scheduleSections = schedule.getSections();
        scheduleSections.sort(Comparator.comparingLong(ScheduleSection::getId));

        user1.addSchedule(schedule);
        USER_DAO.update(user1);

        // User has the new schedule
        User user = USER_DAO.getById(user1.getId());
        assertTrue(user.getSchedules().contains(schedule));

        // Schedule has the right sections
        List<ScheduleSection> dbSections = schedule.getSections();
        dbSections.sort(Comparator.comparingLong(ScheduleSection::getId));
        for (int i = 0; i < scheduleSections.size(); i++) {
            assertEquals(scheduleSections.get(i), dbSections.get(i));
        }
    }

    @Test
    void removeScheduleWithSections() {
        User user1 = USERS.get(0);
        Schedule schedule = user1.getSchedules().get(0);
        List<ScheduleSection> scheduleSections = schedule.getSections();

        user1.removeSchedule(schedule);
        USER_DAO.update(user1);

        // Schedule removed
        User user = USER_DAO.getById(user1.getId());
        assertFalse(user.getSchedules().contains(schedule));

        // Double-check the schedule table
        Schedule dbSchedule = SCHEDULE_DAO.getById(schedule.getId());
        assertNull(dbSchedule);

        // Check that sections where removed
        for (ScheduleSection section : scheduleSections) {
            assertNull(SCHEDULE_SECTION_DAO.getById(section.getId()));
        }
    }
}