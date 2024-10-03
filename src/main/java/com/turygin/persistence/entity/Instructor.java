package com.turygin.persistence.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class representing an instructor.
 */
@Entity
@Table(name = "instructor")
public class Instructor {

    /** Unique instructor ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** Instructor's first name. */
    @Column(name = "first_name")
    private String firstName;

    /** Instructor's last name. */
    @Column(name = "last_name")
    private String lastName;

    /** Sections taught by the instructor. */
    @OneToMany(mappedBy = "instructor", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Section> sections = new ArrayList<>();

    /**
     * Empty constructor.
     */
    public Instructor() {}

    /**
     * Instantiates a new instructor object.
     * @param firstName first name of the instructor
     * @param lastName last name of the instructor
     */
    public Instructor(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Gets instructor id.
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Gets instructor's first name.
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets instructor's first name.
     * @param firstName new instructor first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets instructor's last name.
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets instructor's last name.
     * @param lastName new instructor last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    /**
     * Gets full name.
     * @return the full name
     */
    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }

    /**
     * Returns a list of sections associated with the instructor.
     * @return a list of sections
     */
    public List<Section> getSections() {
        return sections;
    }

    /**
     * Set section list.
     * @param sections new section list
     */
    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    /**
     * Add a section to the list.
     * @param section a new section to add
     */
    public void addSection(Section section) {
        sections.add(section);
        section.setInstructor(this);
    }

    /**
     * Remove section from the list.
     * @param section a section to remove.
     */
    public void removeSection(Section section) {
        sections.remove(section);
        section.setInstructor(null);
    }

    /**
     * Generates a string representation of the instructor object.
     * @return string representation of the instructor
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Instructor{");
        sb.append("id=").append(id);
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
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
        Instructor that = (Instructor) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName);
    }

    /**
     * Generates a hash based on object fields.
     * @return hash value
     */
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }
}
