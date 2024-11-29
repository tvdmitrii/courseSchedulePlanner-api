package com.turygin.persistence.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Time;

import static org.junit.jupiter.api.Assertions.*;

/** Section entity test. */
class SectionTest {
    private Section section1;
    private Section section2;

    /**
     * Resets sections before each test run.
     */
    @BeforeEach
    void resetSections() {
        Instructor instructor1 = new Instructor("James", "Connor");
        Instructor instructor2 = new Instructor("Kelly", "Smith");
        Department compSci = new Department("CS", "Computer Science");
        Course course1 = new Course("Introduction to Python", "Introduces students to Python.", 3, 101, compSci);

        section1 = new Section(Section.Day.MONDAY.value | Section.Day.WEDNESDAY.value | Section.Day.FRIDAY.value,
                Time.valueOf("07:00:00"), Time.valueOf("09:00:00"));
        section2 = new Section(Section.Day.TUESDAY.value | Section.Day.THURSDAY.value,
                Time.valueOf("07:00:00"), Time.valueOf("09:00:00"));

        section1.setInstructor(instructor1);
        section2.setCourse(course1);
        section2.setInstructor(instructor2);
        section2.setCourse(course1);
    }

    /** Ensure that days of the week combinations  work properly. */
    @Test
    void setAllDaysOfWeek() {
        int allDays = 0;
        int sum = 0;
        for(Section.Day day : Section.Day.values()) {
            allDays = allDays | day.value;
            sum += day.value;
        }

        section1.setDaysOfWeek(allDays);

        assertEquals(sum, section1.getDaysOfWeek());
    }

    /** Ensure that each day reports proper state for M, W, F section. */
    @Test
    void mondayWednesdayFriday() {
        int days = section1.getDaysOfWeek();

        assertTrue((days & Section.Day.MONDAY.value) > 0);
        assertEquals(0, (days & Section.Day.TUESDAY.value));
        assertTrue((days & Section.Day.WEDNESDAY.value) > 0);
        assertEquals(0, (days & Section.Day.THURSDAY.value));
        assertTrue((days & Section.Day.FRIDAY.value) > 0);
    }

    /** Ensure that each day reports proper state for T, Th section. */
    @Test
    void tuesdayThursday() {
        int days = section2.getDaysOfWeek();

        assertEquals(0, (days & Section.Day.MONDAY.value));
        assertTrue((days & Section.Day.TUESDAY.value) > 0);
        assertEquals(0, (days & Section.Day.WEDNESDAY.value));
        assertTrue((days & Section.Day.THURSDAY.value) > 0);
        assertEquals(0, (days & Section.Day.FRIDAY.value));
    }

    /** Ensure negative week days are not allowed. */
    @Test
    void daysOfWeekNegative() {
        int days = -1;
        assertThrows(IllegalArgumentException.class, () -> section1.setDaysOfWeek(days));
    }

    /** Ensure that illegal week day combinations are not allowed. */
    @Test
    void daysOfWeekTooLarge() {
        int days = 32;
        assertThrows(IllegalArgumentException.class, () -> section1.setDaysOfWeek(days));
    }

    /** Ensure sections meeting at the same time but on different days do not conflict. */
    @Test
    void sectionDifferentDaysNoConflict() {
        assertFalse(section1.isConflicting(section2));
        assertFalse(section2.isConflicting(section1));
    }

    /** Ensure section conflicts with itself. */
    @Test
    void sectionConflictItself() {
        assertTrue(section1.isConflicting(section1));
    }

    /** Ensure sections conflict if start time of one falls between the start and end times of the other. */
    @Test
    void sectionStartTimeConflict() {
        section2 = new Section(Section.Day.MONDAY.value | Section.Day.WEDNESDAY.value | Section.Day.FRIDAY.value,
                Time.valueOf("08:00:00"), Time.valueOf("10:00:00"));
        assertTrue(section1.isConflicting(section2));
        assertTrue(section2.isConflicting(section1));
    }

    /** Ensure sections conflict if end time of one falls between the start and end times of the other. */
    @Test
    void sectionEndTimeConflict() {
        section2 = new Section(Section.Day.MONDAY.value | Section.Day.WEDNESDAY.value | Section.Day.FRIDAY.value,
                Time.valueOf("06:00:00"), Time.valueOf("09:00:00"));
        assertTrue(section1.isConflicting(section2));
        assertTrue(section2.isConflicting(section1));
    }

    /** Ensure sections conflict if one section contains the other. */
    @Test
    void sectionContainedConflict() {
        section2 = new Section(Section.Day.MONDAY.value | Section.Day.WEDNESDAY.value | Section.Day.FRIDAY.value,
                Time.valueOf("06:30:00"), Time.valueOf("10:30:00"));
        assertTrue(section1.isConflicting(section2));
        assertTrue(section2.isConflicting(section1));
    }

    /** Ensure sections conflict if they conflict at least on one of the meeting days. */
    @Test
    void sectionPartialDayMatchConflict() {
        section2 = new Section(Section.Day.WEDNESDAY.value,
                Time.valueOf("08:00:00"), Time.valueOf("10:00:00"));
        assertTrue(section1.isConflicting(section2));
        assertTrue(section2.isConflicting(section1));
    }

    /** Ensure deep comparison returns true for an object and its copy. */
    @Test
    void testSameObject() {
        Section section1_copy = new Section(section1.getDaysOfWeek(), section1.getFromTime(), section1.getToTime());
        section1_copy.setCourse(section1.getCourse());
        section1_copy.setInstructor(section1.getInstructor());

        assertEquals(section1, section1_copy);
    }

    /** Ensure deep comparison returns false for different objects. */
    @Test
    void testDifferentObject() {
        assertNotEquals(section1, section2);
    }

    /** Ensure that hash is the same for equal objects. */
    @Test
    void testHashCodeSameObject() {
        Section section1_copy = new Section(section1.getDaysOfWeek(), section1.getFromTime(), section1.getToTime());
        section1_copy.setCourse(section1.getCourse());
        section1_copy.setInstructor(section1.getInstructor());

        assertEquals(section1.hashCode(), section1_copy.hashCode());
    }

    /** Ensure that hash is different for different objects. */
    @Test
    void testHashCodeDifferentObject() {
        assertNotEquals(section1.hashCode(), section2.hashCode());
    }
}