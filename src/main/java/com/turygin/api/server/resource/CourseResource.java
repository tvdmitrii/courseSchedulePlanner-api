package com.turygin.api.server.resource;

import com.turygin.api.server.model.CourseDTO;
import com.turygin.api.server.repository.CourseRepository;
import com.turygin.api.server.repository.CourseRepositoryImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/course")
public class CourseResource {

    private static final CourseRepository COURSE_REPOSITORY = new CourseRepositoryImpl();

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
