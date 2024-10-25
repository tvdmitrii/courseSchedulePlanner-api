package com.turygin.api.server.resource;

import com.turygin.api.model.CourseBasicDTO;
import com.turygin.api.resource.ICourseResource;
import com.turygin.persistence.dao.CourseDao;
import com.turygin.persistence.entity.Course;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * REST API course resource.
 */
@Path("/course")
public class CourseResource implements ICourseResource {

    protected static final Logger LOG = LogManager.getLogger(CourseResource.class);

    private final CourseDao COURSE_DAO = new CourseDao();

    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    public CourseBasicDTO getCourse(@PathParam("id") long id) {
        Course course = COURSE_DAO.getById(id);
        CourseBasicDTO courseDTO = null;
        if (course != null) {
            courseDTO = new CourseBasicDTO(course.getId(), course.getCode(), course.getTitle(),
                    course.getDescription(), course.getCredits());
        }
        return courseDTO;
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

    @GET
    @Path("/find")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<CourseBasicDTO> findCourses(@QueryParam("title") String title, @QueryParam("departmentId") long departmentId) {
        LOG.debug("Received title '{}' and departmentId '{}'", title, departmentId);
        List<Course> courses = COURSE_DAO.findCourses(title, departmentId);
        List<CourseBasicDTO> courseDTOs = new ArrayList<>();
        courses.forEach(course -> courseDTOs.add(new CourseBasicDTO(course.getId(), course.getCode(), course.getTitle(),
                course.getDescription(), course.getCredits())));
        return courseDTOs;
    }
}
