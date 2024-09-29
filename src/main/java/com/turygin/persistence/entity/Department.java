package com.turygin.persistence.entity;

import jakarta.persistence.*;

import java.util.Objects;

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

    /**
     * Empty constructor.
     */
    public Department() {}

    /**
     * Instantiates a new department object.
     * @param name name of the department
     */
    public Department(String code, String name) {
        this.code = code.toUpperCase();
        this.name = name;
    }

    /**
     * Gets department id.
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the department id.
     * @param id new department id
     */
    public void setId(long id) {
        this.id = id;
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
        return id == that.id && Objects.equals(code, that.code) && Objects.equals(name, that.name);
    }

    /**
     * Generates a hash based on object fields.
     * @return hash value
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, code, name);
    }
}