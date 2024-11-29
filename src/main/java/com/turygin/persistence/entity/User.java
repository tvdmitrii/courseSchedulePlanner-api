package com.turygin.persistence.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    @Column(name = "uuid", unique = true)
    private UUID uuid;

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
     * @param uuid      unique cognito uuid
     * @param role      the role
     */
    public User(UUID uuid, Type role) {
        this.uuid = uuid;
        this.role = role;
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
    public void replaceSchedules(List<Schedule> schedules) {
        this.schedules.clear();
        if (schedules != null) {
            for (Schedule schedule : schedules) {
                this.addSchedule(schedule);
            }
        }
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
        sb.append(", uuid='").append(uuid.toString()).append('\'');
        sb.append(", role=").append(role);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Gets uuid.
     * @return the uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Sets uuid.
     *
     * @param uuid the uuid
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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
        return Objects.equals(uuid, user.uuid) &&
                role == user.role;
    }

    /**
     * Generates a hash based on object fields.
     * @return hash value
     */
    @Override
    public int hashCode() {
        return Objects.hash(uuid, role);
    }
}
