package com.turygin.persistence.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class representing a schedule.
 */
@Entity
@Table(name = "schedule")
public class Schedule {
    /** Unique ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** The user that has the course in their cart. */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /** Is the selected user schedule? */
    @JoinColumn(name = "selected")
    private boolean selected;

    /** Sections of that are part of the schedule. */
    @OneToMany(mappedBy = "schedule", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ScheduleSection> sections = new ArrayList<>();

    /** Empty constructor. */
    public Schedule() {}

    /**
     * Instantiates a new schedule object for a user.
     * @param user owner of the schedule
     * @param selected is this the preferred user schedule
     */
    public Schedule(User user, boolean selected) {
        this.user = user;
        this.selected = selected;
    }

    /**
     * Instantiates a new schedule object for a user.
     * @param user owner of the schedule
     */
    public Schedule(User user) {
        this.user = user;
        this.selected = false;
    }

    /**
     * Gets id.
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Gets sections.
     * @return the sections
     */
    public List<ScheduleSection> getSections() {
        return sections;
    }

    /**
     * Sets sections.
     * @param sections the sections
     */
    public void replaceSections(List<ScheduleSection> sections) {
        this.sections.clear();
        if (sections != null) {
            for (ScheduleSection section : sections) {
                this.addSection(section);
            }
        }
    }

    /**
     * Adds schedule section.
     * @param section the schedule section
     */
    public void addSection(ScheduleSection section) {
        sections.add(section);
        section.setSchedule(this);
    }

    /**
     * Adds a regular section.
     * @param section the regular section
     */
    public void addSection(Section section) {
        ScheduleSection newSection = new ScheduleSection(this, section);
        newSection.setSchedule(this);
        sections.add(newSection);
    }

    /**
     * Removes a section.
     * @param section the section
     */
    public void removeSection(ScheduleSection section) {
        sections.remove(section);
        section.setSchedule(null);
    }

    /**
     * Is the schedule preferred.
     * @return the boolean
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets preferred.
     * @param selected the selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Gets user.
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets user.
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
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
        Schedule schedule = (Schedule) o;
        return selected == schedule.selected && Objects.equals(user, schedule.user);
    }

    /**
     * Generates a hash based on object fields.
     * @return hash value
     */
    @Override
    public int hashCode() {
        return Objects.hash(user, selected);
    }
}
