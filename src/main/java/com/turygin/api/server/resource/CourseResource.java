package com.turygin.api.server.resource;

import com.turygin.api.model.CourseBasicDTO;
import com.turygin.api.resource.ICourseResource;
import com.turygin.persistence.dao.Dao;
import com.turygin.persistence.entity.Course;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * REST API resource.
 */
@Path("/course")
public class CourseResource implements ICourseResource {

    private final Dao<Course> COURSE_DAO = new Dao<>(Course.class);

    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    public CourseBasicDTO getCourse(@PathParam("id") long id) {
        Course course = COURSE_DAO.getById(id);
        return new CourseBasicDTO(course.getId(), course.getCode(), course.getTitle(),
                course.getDescription(), course.getCredits());
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public List<CourseBasicDTO> getAllCourses() {
        List<Course> courses = COURSE_DAO.getAll();
        List<CourseBasicDTO> courseDTOs = new ArrayList<>();
        courses.forEach(course -> courseDTOs.add(new CourseBasicDTO(course.getId(), course.getCode(), course.getTitle(),
                course.getDescription(), course.getCredits())));
        return courseDTOs;
    }
}
