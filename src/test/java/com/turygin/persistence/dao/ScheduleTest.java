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

/** Schedule DAO tests. */
public class ScheduleTest {
    private static final Logger LOG = LogManager.getLogger(ScheduleTest.class);

    /** List of schedules. */
    private static final List<Schedule> SCHEDULES = new ArrayList<>();

    /** DAO for working with schedules in the database. */
    private static final Dao<Schedule> SCHEDULE_DAO = new Dao<>(Schedule.class);

    /** DAO for working with users in the database. */
    private static final Dao<User> USER_DAO = new Dao<>(User.class);

    /** DAO for working with schedule sections in the database. */
    private static final Dao<ScheduleSection> SCHEDULE_SECTION_DAO = new Dao<>(ScheduleSection.class);

    /** Initial count of schedules. */
    private static final int INITIAL_SCHEDULE_COUNT = 3;

    /** Reset database before each run. */
    @BeforeEach
    void resetDatabase() {
        if(!ResetDatabaseHelper.reset()) {
            LOG.error("Could not reset database!");
            throw new RuntimeException("Could not reset database!");
        }

        // Reset lists
        SCHEDULES.clear();

        // Populate schedules
        SCHEDULES.addAll(SCHEDULE_DAO.getAll());
        assertEquals(INITIAL_SCHEDULE_COUNT, SCHEDULES.size());

        // Sort schedules by id to guarantee order for convenience
        SCHEDULES.sort(Comparator.comparingLong(Schedule::getId));
    }

    /** Ensure schedule can be loaded by id. */
    @Test
    void getById() {
        Schedule schedule1 = SCHEDULES.get(0);

        Schedule schedule = SCHEDULE_DAO.getById(schedule1.getId());

        assertEquals(schedule1, schedule);
    }

    /** Ensure getting schedule by invalid id returns null. */
    @Test
    void getById_InvalidId() {
        Schedule schedule = SCHEDULE_DAO.getById(INITIAL_SCHEDULE_COUNT + 10);

        assertNull(schedule);
    }

    /** Ensure schedule can be inserted. */
    @Test
    void insert() {
        User user = USER_DAO.getById(2);
        Schedule newSchedule = new Schedule(user);

        SCHEDULE_DAO.insert(newSchedule);

        Schedule schedule = SCHEDULE_DAO.getById(newSchedule.getId());
        user = USER_DAO.getById(2);

        assertEquals(newSchedule, schedule);
        assertEquals(schedule.getUser(), user);
    }

    /** Ensure schedule can be updated. */
    @Test
    void update() {
        boolean selected = true;
        Schedule schedule1 = SCHEDULES.get(0);

        schedule1.setSelected(selected);
        SCHEDULE_DAO.update(schedule1);

        Schedule schedule = SCHEDULE_DAO.getById(schedule1.getId());

        assertEquals(schedule1, schedule);
    }

    /** Ensure schedule can be removed. */
    @Test
    void delete() {
        Schedule schedule2 = SCHEDULES.get(2);
        long scheduleSection1 = schedule2.getSections().get(0).getId();

        SCHEDULE_DAO.delete(schedule2);

        // Schedule removed
        Schedule schedule = SCHEDULE_DAO.getById(schedule2.getId());
        assertNull(schedule);

        // The schedule section also removed
        ScheduleSection section = SCHEDULE_SECTION_DAO.getById(scheduleSection1);
        assertNull(section);
    }

    /** Ensure all schedules can be loaded. */
    @Test
    void getAll() {
        List<Schedule> scheduleSectionsFromDb = SCHEDULE_DAO.getAll();

        assertNotNull(scheduleSectionsFromDb);
        assertEquals(SCHEDULES.size(), scheduleSectionsFromDb.size());

        scheduleSectionsFromDb.sort(Comparator.comparingLong(Schedule::getId));

        for(int i = 0; i < SCHEDULES.size(); i++) {
            assertEquals(SCHEDULES.get(i), scheduleSectionsFromDb.get(i));
        }
    }
}
