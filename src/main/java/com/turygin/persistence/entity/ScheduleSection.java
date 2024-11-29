package com.turygin.persistence.entity;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * A class representing a section of a schedule.
 */
@Entity
@Table(name = "schedule_section")
public class ScheduleSection {
    /** Unique ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** The associated schedule. */
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    /** The underlying section. Unidirectional connection. */
    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    /** Empty constructor. */
    public ScheduleSection() {}

    /**
     * Instantiates an object that indicates a section is part of a schedule.
     * @param schedule the schedule containing the section
     * @param section the section that is a part of the schedule
     */
    public ScheduleSection(Schedule schedule, Section section) {
        this.schedule = schedule;
        this.section = section;
    }

    /**
     * Gets id.
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Gets schedule.
     * @return the schedule
     */
    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * Sets schedule.
     * @param schedule the schedule
     */
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    /**
     * Gets section.
     * @return the section
     */
    public Section getSection() {
        return section;
    }

    /**
     * Sets section.
     * @param section the section
     */
    public void setSection(Section section) {
        this.section = section;
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
        ScheduleSection that = (ScheduleSection) o;
        return Objects.equals(section, that.section);
    }

    /**
     * Generates a hash based on object fields.
     * @return hash value
     */
    @Override
    public int hashCode() {
        return Objects.hash(section);
    }
}
