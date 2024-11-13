package com.turygin.api.server.resource;

import com.turygin.api.model.CourseBasicDTO;
import com.turygin.api.resource.ICourseResource;
import com.turygin.persistence.dao.CourseDao;
import com.turygin.persistence.dao.Dao;
import com.turygin.persistence.entity.Course;
import com.turygin.persistence.entity.Department;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST API course resource.
 */
@Path("/course")
public class CourseResource implements ICourseResource {

    protected static final Logger LOG = LogManager.getLogger(CourseResource.class);

    private final CourseDao COURSE_DAO = new CourseDao();
    private final Dao<Department> DEPARTMENT_DAO = new Dao<>(Department.class);

    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    public CourseBasicDTO getCourse(@PathParam("id") long id) {
        Course course = COURSE_DAO.getById(id);
        return Mapper.mapToCourseBasic(course);
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public List<CourseBasicDTO> getAllCourses() {
        List<Course> courses = COURSE_DAO.getAll();
        return Mapper.mapToCourseBasic(courses);
    }

    @GET
    @Path("/find")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<CourseBasicDTO> findCourses(@QueryParam("title") String title, @QueryParam("departmentId") long departmentId) {
        LOG.debug("Received title '{}' and departmentId '{}'", title, departmentId);
        List<Course> courses = COURSE_DAO.findCourses(title, departmentId);
        return Mapper.mapToCourseBasic(courses);
    }

    @DELETE
    @Path("/{id}")
    public void deleteCourse(@PathParam("id") long courseId) {
        LOG.debug("Received course ID '{}'", courseId);
        Course course = COURSE_DAO.getById(courseId);
        if (course != null) {
            COURSE_DAO.delete(course);
        }
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public CourseBasicDTO addCourse(CourseBasicDTO courseBasicDTO) {
        LOG.debug("Received course DTO {}", courseBasicDTO != null ? courseBasicDTO.toString() : null);
        assert courseBasicDTO != null;

        Course course = Mapper.courseDTOToCourse(courseBasicDTO);
        Department department = DEPARTMENT_DAO.getById(courseBasicDTO.getDepartmentId());
        assert department != null;

        course.setDepartment(department);
        COURSE_DAO.insert(course);
        courseBasicDTO = Mapper.mapToCourseBasic(course);
        LOG.debug("Returned course DTO {}", courseBasicDTO.toString());
        return courseBasicDTO;
    }

    @PUT
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public CourseBasicDTO updateCourse(CourseBasicDTO courseBasicDTO) {
        LOG.debug("Received course DTO {}", courseBasicDTO != null ? courseBasicDTO.toString() : null);
        assert courseBasicDTO != null;

        // Load chat from DB and ensure it exists
        Course course = COURSE_DAO.getById(courseBasicDTO.getId());
        assert course != null;

        course.setDescription(courseBasicDTO.getDescription());
        course.setTitle(courseBasicDTO.getTitle());
        course.setNumber(courseBasicDTO.getNumber());
        course.setCredits(courseBasicDTO.getCredits());

        if (course.getDepartment().getId() != courseBasicDTO.getDepartmentId()) {
            Department department = DEPARTMENT_DAO.getById(courseBasicDTO.getDepartmentId());
            assert department != null;
            course.setDepartment(department);
        }
        COURSE_DAO.update(course);

        courseBasicDTO = Mapper.mapToCourseBasic(course);
        LOG.debug("Returned course DTO {}", courseBasicDTO.toString());
        return courseBasicDTO;
    }
}
