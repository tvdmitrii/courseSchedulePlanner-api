package com.turygin.persistence.entity;

import jakarta.persistence.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class representing a course section.
 */
@Entity
@Table(name = "section")
public class Section {

    public enum Day {
        MONDAY((byte) 1, "Monday"),
        TUESDAY((byte) 2, "Tuesday"),
        WEDNESDAY((byte) 4, "Wednesday"),
        THURSDAY((byte) 8, "Thursday"),
        FRIDAY((byte) 16, "Friday");

        public final byte value;
        public final String name;

        Day(byte value, String name) {
            this.value = value;
            this.name = name;
        }
    }

    /** Unique section ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** Section meeting days. */
    @Column(name = "days_of_week")
    private byte daysOfWeek;

    /** Section start time. */
    @Column(name = "from_time")
    private Time fromTime;

    /** Section end time. */
    @Column(name = "to_time")
    private Time toTime;

    /** Instructor teaching the section. */
    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    /** Course associated with the section. */
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    /** Empty constructor. */
    public Section() {}

    /**
     * Instantiates a new section object.
     * @param daysOfWeek days of the week the section meets
     * @param fromTime section start time
     * @param toTime section end time
     */
    public Section(int daysOfWeek, Time fromTime, Time toTime) {
        this.fromTime = fromTime;
        this.toTime = toTime;
        setDaysOfWeek(daysOfWeek);
    }

    /**
     * Gets course.
     * @return the course
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Sets course.
     * @param course the course
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Gets days of week.
     * @return the days of week
     */
    public byte getDaysOfWeek() {
        return daysOfWeek;
    }

    /**
     * Sets days of week.
     * @param daysOfWeek the days of week
     */
    public void setDaysOfWeek(int daysOfWeek) {
        if (daysOfWeek < 0 || daysOfWeek > 31) throw new IllegalArgumentException("The invalid days of the week encoded.");
        this.daysOfWeek = (byte) daysOfWeek;
    }

    /**
     * Gets start time.
     * @return the start time
     */
    public Time getFromTime() {
        return fromTime;
    }

    /**
     * Sets start time.
     * @param fromTime the start time
     */
    public void setFromTime(Time fromTime) {
        this.fromTime = fromTime;
    }

    /**
     * Gets id.
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Gets instructor.
     * @return the instructor
     */
    public Instructor getInstructor() {
        return instructor;
    }

    /**
     * Sets instructor.
     * @param instructor the instructor
     */
    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    /**
     * Gets end time.
     * @return the end time
     */
    public Time getToTime() {
        return toTime;
    }

    /**
     * Sets end time.
     * @param toTime the end time
     */
    public void setToTime(Time toTime) {
        this.toTime = toTime;
    }

    /**
     * Generates a string representation of the section object.
     * @return string representation of the section
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Section{");
        sb.append("id=").append(id);

        List<String> matchingDays = new ArrayList<>();
        for(Day day : Day.values()) {
            if((daysOfWeek & day.value) != 0 ) {
                matchingDays.add(day.name);
            }
        }
        sb.append(", daysOfWeek={").append(String.join(",", matchingDays)).append("}");

        sb.append(", fromTime='").append(fromTime).append('\'');
        sb.append(", toTime='").append(toTime).append('\'');
        sb.append(", instructor=").append(instructor.toString());
        sb.append(", course=").append(course.toString());
        sb.append('}');
        return sb.toString();
    }

    /**
     * Performs deep equality comparison.
     * @param o object to compare to
     * @return true if all fields of the object are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return daysOfWeek == section.daysOfWeek &&
                Objects.equals(fromTime, section.fromTime) &&
                Objects.equals(toTime, section.toTime) &&
                Objects.equals(instructor, section.instructor) &&
                Objects.equals(course, section.course);
    }

    /**
     * Generates a hash based on object fields.
     * @return hash value
     */
    @Override
    public int hashCode() {
        return Objects.hash(daysOfWeek, fromTime, toTime, instructor, course);
    }
}
