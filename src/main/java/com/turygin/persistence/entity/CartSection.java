package com.turygin.persistence.entity;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * A class representing a selected section of a course in user's cart.
 */
@Entity
@Table(name = "cart_course_section")
public class CartSection {
    /** Unique ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** The associated course in cart. */
    @ManyToOne
    @JoinColumn(name = "cart_course_id")
    private CartCourse course;

    /** The underlying section. Unidirectional connection. */
    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    /** Empty constructor. */
    public CartSection() {}

    /**
     * Instantiates a CartSection that keeps track of user selected sections for a course in their cart.
     * @param course course from user's cart
     * @param section selected section of the course
     */
    public CartSection(CartCourse course, Section section) {
        this.course = course;
        this.section = section;
    }

    /**
     * Gets course.
     * @return the course
     */
    public CartCourse getCourse() {
        return course;
    }

    /**
     * Sets course.
     * @param course the course
     */
    public void setCourse(CartCourse course) {
        this.course = course;
    }

    /**
     * Gets id.
     * @return the id
     */
    public long getId() {
        return id;
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
     * Performs deep equality comparison. Ignores department field.
     * @param o object to compare to
     * @return true if all fields of the object are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartSection that = (CartSection) o;
        return Objects.equals(course, that.course) && Objects.equals(section, that.section);
    }

    /**
     * Generates a hash based on object fields. Ignores sections field.
     * @return hash value
     */
    @Override
    public int hashCode() {
        return Objects.hash(course, section);
    }
}
