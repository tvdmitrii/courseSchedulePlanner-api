package com.turygin.persistence.entity;

import jakarta.persistence.*;

import java.util.*;

/**
 * A class representing a department.
 */
@Entity
@Table(name = "department")
public class Department {

    /** Unique department ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** Short department code name. Always upper case. */
    @Column(name = "code")
    private String code;

    /** Department name. */
    @Column(name = "name")
    private String name;

    /** Courses associated with the department. */
    @OneToMany(mappedBy = "department", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Course> courses = new ArrayList<>();

    /**
     * Empty constructor.
     */
    public Department() {}

    /**
     * Instantiates a new department object.
     * @param code short department code name
     * @param name name of the department
     */
    public Department(String code, String name) {
        this.name = name;
        setCode(code);
    }

    /**
     * Gets department id.
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Gets department name.
     * @return department name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the department name.
     * @param name new department name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets department code.
     * @return department code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the department code.
     * @param code new department code
     */
    public void setCode(String code) {
        this.code = code.toUpperCase();
    }

    /**
     * Returns a list of courses associated with the department.
     * @return a list of courses
     */
    public List<Course> getCourses() {
        return courses;
    }

    /**
     * Set course list.
     * @param courses new course list
     */
    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    /**
     * Add a course to the list.
     * @param course a new course to add
     */
    public void addCourse(Course course) {
        courses.add(course);
        course.setDepartment(this);
    }

    /**
     * Remove course from the list.
     * @param course a course to remove.
     */
    public void removeCourse(Course course) {
        courses.remove(course);
        course.setDepartment(null);
    }

    /**
     * Generates a string representation of the department object.
     * @return string representation of the department
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Department{");
        sb.append("id=").append(id);
        sb.append(", code='").append(code).append('\'');
        sb.append(", name='").append(name).append('\'');
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
        Department that = (Department) o;
        return Objects.equals(code, that.code) && Objects.equals(name, that.name);
    }

    /**
     * Generates a hash based on object fields.
     * @return hash value
     */
    @Override
    public int hashCode() {
        return Objects.hash(code, name);
    }
}
