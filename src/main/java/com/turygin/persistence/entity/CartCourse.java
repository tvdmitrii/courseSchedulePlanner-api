package com.turygin.persistence.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class representing a course in user's cart.
 */
@Entity
@Table(name = "cart_course")
public class CartCourse {

    /** Unique ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** The user that has the course in their cart. */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /** The underlying course. Unidirectional connection. */
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    /** Sections of the course selected by the user. */
    @OneToMany(mappedBy = "course", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CartSection> sections = new ArrayList<>();

    /** Empty constructor. */
    public CartCourse() {}

    /**
     * Instantiates a new cart course.
     * @param user user who added the course to cart
     * @param course underlying course
     */
    public CartCourse(User user, Course course) {
        this.user = user;
        this.course = course;
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
    public List<CartSection> getSections() {
        return sections;
    }

    /**
     * Sets sections.
     * @param sections the sections
     */
    public void setSections(List<CartSection> sections) {
        this.sections = sections;
    }

    /**
     * Adds section.
     * @param section the section
     */
    public void addSection(CartSection section) {
        this.sections.add(section);
        section.setCourse(this);
    }

    /**
     * Removes section.
     * @param section the section
     */
    public void removeSection(CartSection section) {
        this.sections.remove(section);
        section.setCourse(null);
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
     * Performs deep equality comparison. Ignores department field.
     * @param o object to compare to
     * @return true if all fields of the object are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartCourse that = (CartCourse) o;
        return Objects.equals(user, that.user) && Objects.equals(course, that.course);
    }

    /**
     * Generates a hash based on object fields. Ignores sections field.
     * @return hash value
     */
    @Override
    public int hashCode() {
        return Objects.hash(user, course);
    }
}
