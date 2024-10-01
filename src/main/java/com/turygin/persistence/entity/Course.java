package com.turygin.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.util.Objects;

/**
 * A class representing a course.
 */
@Entity
@Table(name = "course")
public class Course {

    /** Unique course ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** Course title. */
    @Column(name = "title")
    private String title;

    /** Course description. */
    @Column(name = "description")
    private String description;

    /** Number of credits. */
    @Column(name = "credits")
    private byte credits;

    /** Course number. */
    @Column(name = "number")
    private short number;

    /** Course department. */
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    /**
     * Empty constructor.
     */
    public Course() {}

    /**
     * Instantiates a course object.
     *
     * @param title       course title
     * @param description course description
     * @param credits     number of credit hours
     * @param number      course number
     * @param department  department object
     */
    public Course(String title, String description, int credits, int number, Department department) {
        this.title = title;
        this.description = description;
        this.department = department;
        setCredits(credits);
        setNumber(number);
    }

    /**
     * Instantiates a course object omitting the department.
     *
     * @param title       course title
     * @param description course description
     * @param credits     number of credit hours
     * @param number      course number
     */
    public Course(String title, String description, int credits, int number) {
        this.title = title;
        this.description = description;
        setCredits(credits);
        setNumber(number);
    }

    /**
     * Gets credits.
     * @return the credits
     */
    public int getCredits() {
        return Byte.toUnsignedInt(credits);
    }

    /**
     * Sets credits.
     * @param credits the credits
     */
    public void setCredits(byte credits) {
        this.credits = credits;
    }

    /**
     * Sets credits, safely casting it from an integer.
     * @param credits the credits as an integer
     * @throws IllegalArgumentException if the number of credits is non-positive or does not fit into an unsigned byte
     */
    public void setCredits(int credits) {
        if (credits <= 0) throw new IllegalArgumentException("The number of credits must be positive.");

        if (credits > Byte.MAX_VALUE + Math.abs(Byte.MIN_VALUE)) {
            throw new IllegalArgumentException("The number of credits must fit into an unsigned byte.");
        }
        setCredits((byte) credits);
    }

    /**
     * Gets department.
     * @return the department
     */
    public Department getDepartment() {
        return department;
    }

    /**
     * Sets department.
     * @param department the department
     */
    public void setDepartment(Department department) {
        this.department = department;
    }

    /**
     * Gets description.
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets id.
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Gets course number.
     * @return the course number
     */
    public int getNumber() {
        return Short.toUnsignedInt(number);
    }

    /**
     * Sets course number.
     * @param number the course number
     */
    public void setNumber(short number) {
        this.number = number;
    }

    /**
     * Sets course number, safely casting it from an integer.
     * @param number the course number as an integer
     * @throws IllegalArgumentException if the course number is non-positive or does not fit into an unsigned short
     */
    public void setNumber(int number) {
        if (number <= 0) throw new IllegalArgumentException("The course number must be positive.");

        if (number > Short.MAX_VALUE + Math.abs(Short.MIN_VALUE)) {
            throw new IllegalArgumentException("The number of credits must fit into an unsigned short.");
        }
        setNumber((short) number);
    }

    /**
     * Gets title.
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the course code.
     * @return course code
     */
    public String getCode() {
        return String.format("%s %d", department.getCode(), number);
    }

    /**
     * Generates a string representation of the course object.
     * @return string representation of the course
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Course{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", credits=").append(getCredits());
        sb.append(", number=").append(getNumber());
        sb.append(", department=").append(department.toString());
        sb.append('}');
        return sb.toString();
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
        Course course = (Course) o;
        return credits == course.credits &&
                number == course.number &&
                Objects.equals(title, course.title) &&
                Objects.equals(description, course.description);
    }

    /**
     * Generates a hash based on object fields. Ignores department field.
     * @return hash value
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, description, credits, number);
    }
}
