package com.turygin.persistence.dao;

import com.turygin.persistence.entity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/** Schedule section DAO tests. */
public class ScheduleSectionTest {
    private static final Logger LOG = LogManager.getLogger(ScheduleSectionTest.class);

    /** List of schedule sections. */
    private static final List<ScheduleSection> SCHEDULE_SECTIONS = new ArrayList<>();

    /** DAO for working with schedule sections in the database. */
    private static final Dao<ScheduleSection> SCHEDULE_SECTION_DAO = new Dao<>(ScheduleSection.class);

    /** DAO for working with schedules in the database. */
    private static final Dao<Schedule> SCHEDULE_DAO = new Dao<>(Schedule.class);

    /** DAO for working with sections in the database. */
    private static final Dao<Section> SECTION_DAO = new Dao<>(Section.class);

    /** Initial schedule count. */
    private static final int INITIAL_SCHEDULE_SECTION_COUNT = 5;

    /** Reset database before each run. */
    @BeforeEach
    void resetDatabase() {
        if(!ResetDatabaseHelper.reset()) {
            LOG.error("Could not reset database!");
            throw new RuntimeException("Could not reset database!");
        }

        // Reset lists
        SCHEDULE_SECTIONS.clear();

        // Populate scheduleSections
        SCHEDULE_SECTIONS.addAll(SCHEDULE_SECTION_DAO.getAll());
        assertEquals(INITIAL_SCHEDULE_SECTION_COUNT, SCHEDULE_SECTIONS.size());

        // Sort schedule courses by id to guarantee order for convenience
        SCHEDULE_SECTIONS.sort(Comparator.comparingLong(ScheduleSection::getId));
    }

    /** Ensure schedule section can be loaded by id. */
    @Test
    void getById() {
        ScheduleSection scheduleSection1 = SCHEDULE_SECTIONS.get(0);

        ScheduleSection scheduleSection = SCHEDULE_SECTION_DAO.getById(scheduleSection1.getId());

        assertEquals(scheduleSection1, scheduleSection);
    }

    /** Ensure getting schedule section by invalid id returns null. */
    @Test
    void getById_InvalidId() {
        ScheduleSection scheduleSection = SCHEDULE_SECTION_DAO.getById(INITIAL_SCHEDULE_SECTION_COUNT + 10);

        assertNull(scheduleSection);
    }

    /** Ensure schedule section can be inserted. */
    @Test
    void insert() {
        Schedule schedule = SCHEDULE_DAO.getById(2);
        Section section = SECTION_DAO.getById(5);
        ScheduleSection newScheduleSection = new ScheduleSection(schedule, section);

        SCHEDULE_SECTION_DAO.insert(newScheduleSection);

        ScheduleSection scheduleSection = SCHEDULE_SECTION_DAO.getById(newScheduleSection.getId());
        schedule = SCHEDULE_DAO.getById(2);
        section = SECTION_DAO.getById(5);

        assertEquals(newScheduleSection, scheduleSection);
        assertEquals(scheduleSection.getSchedule(), schedule);
        assertEquals(scheduleSection.getSection(), section);
    }

    /** Ensure schedule section can be removed. */
    @Test
    void delete() {
        ScheduleSection scheduleSection1 = SCHEDULE_SECTIONS.get(1);
        long sectionId = scheduleSection1.getSection().getId();

        SCHEDULE_SECTION_DAO.delete(scheduleSection1);

        // ScheduleSection removed
        ScheduleSection scheduleSection = SCHEDULE_SECTION_DAO.getById(scheduleSection1.getId());
        assertNull(scheduleSection);

        // The underlying section was not affected
        Section section = SECTION_DAO.getById(sectionId);
        assertNotNull(section);
    }

    /** Ensure all schedule sections can be loaded. */
    @Test
    void getAll() {
        List<ScheduleSection> scheduleSectionsFromDb = SCHEDULE_SECTION_DAO.getAll();

        assertNotNull(scheduleSectionsFromDb);
        assertEquals(SCHEDULE_SECTIONS.size(), scheduleSectionsFromDb.size());

        scheduleSectionsFromDb.sort(Comparator.comparingLong(ScheduleSection::getId));

        for(int i = 0; i < SCHEDULE_SECTIONS.size(); i++) {
            assertEquals(SCHEDULE_SECTIONS.get(i), scheduleSectionsFromDb.get(i));
        }
    }
}
