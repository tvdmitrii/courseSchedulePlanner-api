package com.turygin.api.server.repository;

import com.turygin.api.model.CourseDTO;
import com.turygin.api.repository.ICourseRepository;
import com.turygin.persistence.dao.Dao;
import com.turygin.persistence.entity.Course;

import java.util.ArrayList;
import java.util.List;


/**
 * Implementation of ICourseRepository that handles course operations.
 * It serves as an abstraction layer between Dao and WebApp.
 */
public class CourseRepository implements ICourseRepository {
    private final Dao<Course> COURSE_DAO = new Dao<>(Course.class);

    /**
     * Fetches course information using unique course ID.
     * @param id unique course ID
     * @return course information
     */
    @Override
    public CourseDTO getCourse(long id) {
        Course course = COURSE_DAO.getById(id);
        return new CourseDTO(course.getCode(), course.getTitle());
    }

    /**
     * Fetches information about all courses as a list.
     * @return a list of course information objects
     */
    @Override
    public List<CourseDTO> getAllCourses() {
        List<Course> courses = COURSE_DAO.getAll();
        List<CourseDTO> courseDTOs = new ArrayList<>();
        courses.forEach(course -> courseDTOs.add(new CourseDTO(course.getCode(), course.getTitle())));
        return courseDTOs;
    }
}
