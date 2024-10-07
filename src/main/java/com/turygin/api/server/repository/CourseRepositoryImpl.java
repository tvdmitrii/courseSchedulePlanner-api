package com.turygin.api.server.repository;

import com.turygin.api.server.model.CourseDTO;
import com.turygin.persistence.dao.Dao;
import com.turygin.persistence.entity.Course;

import java.util.ArrayList;
import java.util.List;


public class CourseRepositoryImpl implements CourseRepository {
    private static final Dao<Course> COURSE_DAO = new Dao<>(Course.class);

    @Override
    public CourseDTO getCourse(long id) {
        Course course = COURSE_DAO.getById(id);
        return new CourseDTO(course.getCode(), course.getTitle());
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        List<Course> courses = COURSE_DAO.getAll();
        List<CourseDTO> courseDTOs = new ArrayList<>();
        courses.forEach(course -> courseDTOs.add(new CourseDTO(course.getCode(), course.getTitle())));
        return courseDTOs;
    }
}
