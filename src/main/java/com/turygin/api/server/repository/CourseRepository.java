package com.turygin.api.server.repository;

import com.turygin.api.server.model.CourseDTO;

import java.util.List;

public interface CourseRepository {
    CourseDTO getCourse(long id);
    List<CourseDTO> getAllCourses();
}
