package com.turygin.persistence.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Time;

import static org.junit.jupiter.api.Assertions.*;

class SectionTest {
    private Section section1;
    private Section section2;

    @BeforeEach
    void populateUsers() {
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

    @Test
    void MondayWednesdayFriday() {
        int days = section1.getDaysOfWeek();

        assertTrue((days & Section.Day.MONDAY.value) > 0);
        assertEquals(0, (days & Section.Day.TUESDAY.value));
        assertTrue((days & Section.Day.WEDNESDAY.value) > 0);
        assertEquals(0, (days & Section.Day.THURSDAY.value));
        assertTrue((days & Section.Day.FRIDAY.value) > 0);
    }

    @Test
    void TuesdayThursday() {
        int days = section2.getDaysOfWeek();

        assertEquals(0, (days & Section.Day.MONDAY.value));
        assertTrue((days & Section.Day.TUESDAY.value) > 0);
        assertEquals(0, (days & Section.Day.WEDNESDAY.value));
        assertTrue((days & Section.Day.THURSDAY.value) > 0);
        assertEquals(0, (days & Section.Day.FRIDAY.value));
    }

    @Test
    void DaysOfWeekNegative() {
        int days = -1;
        assertThrows(IllegalArgumentException.class, () -> section1.setDaysOfWeek(days));
    }

    @Test
    void DaysOfWeekTooLarge() {
        int days = 32;
        assertThrows(IllegalArgumentException.class, () -> section1.setDaysOfWeek(days));
    }

    @Test
    void testSameObject() {
        Section section1_copy = new Section(section1.getDaysOfWeek(), section1.getFromTime(), section1.getToTime());
        section1_copy.setCourse(section1.getCourse());
        section1_copy.setInstructor(section1.getInstructor());

        assertEquals(section1, section1_copy);
    }

    @Test
    void testDifferentObject() {
        assertNotEquals(section1, section2);
    }

    @Test
    void testHashCodeSameObject() {
        Section section1_copy = new Section(section1.getDaysOfWeek(), section1.getFromTime(), section1.getToTime());
        section1_copy.setCourse(section1.getCourse());
        section1_copy.setInstructor(section1.getInstructor());

        assertEquals(section1.hashCode(), section1_copy.hashCode());
    }

    @Test
    void testHashCodeDifferentObject() {
        assertNotEquals(section1.hashCode(), section2.hashCode());
    }
}