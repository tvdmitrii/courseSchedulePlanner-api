package com.turygin.api.server.resource;

import com.turygin.api.model.CourseDTO;
import com.turygin.api.repository.ICourseRepository;
import com.turygin.api.server.repository.CourseRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * REST API resource.
 */
@Path("/course")
public class CourseResource {

    private final ICourseRepository COURSE_REPOSITORY = new CourseRepository();

    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    public CourseDTO getCourse(@PathParam("id") long id) {
        return COURSE_REPOSITORY.getCourse(id);
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public List<CourseDTO> getAllCourses() {
        return COURSE_REPOSITORY.getAllCourses();
    }
}
