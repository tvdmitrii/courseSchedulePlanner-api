package com.turygin.persistence.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class representing a user.
 */
@Entity
@Table(name = "user")
public class User {

    /** Enum describing user type. */
    public enum Type {
        USER,
        ADMIN
    }

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "role")
    private Type role;

    /** Courses in user's cart. */
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CartCourse> courses = new ArrayList<>();

    /** Generated user schedules. */
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Schedule> schedules = new ArrayList<>();

    /**
     * Empty constructor.
     */
    public User(){}

    /**
     * Instantiates a new User.
     *
     * @param firstName the first name
     * @param lastName  the last name
     * @param email     the email
     * @param role      the role
     */
    public User(String firstName, String lastName, String email, Type role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets first name.
     *
     * @param firstName the first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Gets last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets last name.
     *
     * @param lastName the last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets role.
     *
     * @return the role
     */
    public Type getRole() {
        return role;
    }

    /**
     * Sets role.
     *
     * @param role the role
     */
    public void setRole(Type role) {
        this.role = role;
    }

    /**
     * Gets full name.
     *
     * @return the full name
     */
    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }

    /**
     * Gets courses in cart.
     * @return the courses
     */
    public List<CartCourse> getCoursesInCart() {
        return courses;
    }

    /**
     * Sets courses in cart.
     * @param courses the courses
     */
    public void setCoursesInCart(List<CartCourse> courses) {
        this.courses = courses;
    }

    /**
     * Adds course to the cart.
     * @param course the course
     */
    public void addCourseToCart(CartCourse course) {
        this.courses.add(course);
        course.setUser(this);
    }

    /**
     * Removes course from cart.
     * @param course the course
     */
    public void removeCourseFromCart(CartCourse course) {
        this.courses.remove(course);
        course.setUser(null);
    }

    /**
     * Gets schedules.
     * @return the schedules
     */
    public List<Schedule> getSchedules() {
        return schedules;
    }

    /**
     * Sets schedules.
     * @param schedules the schedules
     */
    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    /**
     * Adds new schedule.
     * @param schedule the schedule
     */
    public void addSchedule(Schedule schedule) {
        this.schedules.add(schedule);
        schedule.setUser(this);
    }

    /**
     * Removes schedule.
     * @param schedule the schedule
     */
    public void removeSchedule(Schedule schedule) {
        this.schedules.remove(schedule);
        schedule.setUser(null);
    }

    /**
     * Generates a string representation of the user object.
     * @return string representation of the user
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", email='").append(email).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", role=").append(role);
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
        // Check if object is the same
        if (this == o) return true;
        // Check if object is null or is of a different class
        if (o == null || getClass() != o.getClass()) return false;
        // Now we can safely cast Object to User
        User user = (User) o;
        // Objects.equals checks for null before calling .equals()
        return Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(email, user.email) &&
                role == user.role;
    }

    /**
     * Generates a hash based on object fields.
     * @return hash value
     */
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, role);
    }
}
