package com.turygin.api.server.resource;

import com.turygin.api.model.CourseDTO;
import com.turygin.api.resource.ICourseResource;
import com.turygin.persistence.dao.CourseDao;
import com.turygin.persistence.dao.Dao;
import com.turygin.persistence.entity.Course;
import com.turygin.persistence.entity.Department;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

/**
 * REST API course resource implementation.
 */
@Path("/course")
public class CourseResource implements ICourseResource {

    protected static final Logger LOG = LogManager.getLogger(CourseResource.class);

    /** Handles course related database queries. */
    private final CourseDao COURSE_DAO = new CourseDao();

    /** Handles department related database queries. */
    private final Dao<Department> DEPARTMENT_DAO = new Dao<>(Department.class);

    /**
     * Fetches a course by ID.
     * @param id course ID
     * @return course DTO
     */
    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response getCourse(@PathParam("id") long id) {
        LOG.debug("Fetching course with ID '{}'", id);
        Course course = COURSE_DAO.getById(id);
        assert course != null;

        CourseDTO courseDTO = Mapper.toCourseDTO(course);
        LOG.debug("Found course '{}'", courseDTO.toString());
        return Response.ok(courseDTO).build();
    }

    /**
     * Fetches information about all available courses.
     * @return aa list of course DTOs
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public Response getAllCourses() {
        LOG.debug("Fetching all courses");
        List<Course> courses = COURSE_DAO.getAll();
        List<CourseDTO> courseDTOs = Mapper.toCourseDTO(courses);
        LOG.debug("Found {} courses", courseDTOs.size());
        return Response.ok(courseDTOs).build();
    }

    /**
     * Finds all courses by a title substring and/or department ID.
     * @param title course title substring
     * @param departmentId department ID
     * @return a list of course DTOs matching the search criteria
     */
    @GET
    @Path("/find")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response findCourses(@QueryParam("title") String title, @QueryParam("departmentId") long departmentId) {
        LOG.debug("Searching courses by title '{}' and departmentId '{}'", title, departmentId);
        List<Course> courses = COURSE_DAO.findCourses(title, departmentId);
        List<CourseDTO> courseDTOs = Mapper.toCourseDTO(courses);
        LOG.debug("Found {} courses", courseDTOs.size());
        return Response.ok(courseDTOs).build();
    }

    /**
     * Removes a course by ID.
     * @param courseId course ID
     * @return 204 response if course was found and deleted
     */
    @DELETE
    @Path("/{id}")
    public Response deleteCourse(@PathParam("id") long courseId) {
        LOG.debug("Deleting course with ID '{}'", courseId);
        Course course = COURSE_DAO.getById(courseId);
        assert course != null;
        COURSE_DAO.delete(course);
        LOG.debug("Course removed");
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    /**
     * Inserts a new course.
     * @param courseBasicDTO course DTO containing course information (request body)
     * @return newly created course DTO
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response addCourse(CourseDTO courseBasicDTO) {
        assert courseBasicDTO != null;
        LOG.debug("Inserting course {}", courseBasicDTO.toString());
        Course course = Mapper.createCourse(courseBasicDTO);

        Department department = DEPARTMENT_DAO.getById(courseBasicDTO.getDepartment().getId());
        assert department != null;
        course.setDepartment(department);
        COURSE_DAO.insert(course);

        courseBasicDTO = Mapper.toCourseDTO(course);
        LOG.debug("Course inserted: {}", courseBasicDTO.toString());
        return Response.status(Response.Status.CREATED).entity(courseBasicDTO).build();
    }

    /**
     * Updates an existing course.
     * @param courseBasicDTO course DTO containing new information (request body)
     * @return updated course DTO
     */
    @PUT
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response updateCourse(CourseDTO courseBasicDTO) {
        assert courseBasicDTO != null;
        LOG.debug("Updating course {}", courseBasicDTO.toString());

        // Load course from DB and ensure it exists
        Course course = COURSE_DAO.getById(courseBasicDTO.getId());
        assert course != null;

        course.setDescription(courseBasicDTO.getDescription());
        course.setTitle(courseBasicDTO.getTitle());
        course.setNumber(courseBasicDTO.getNumber());
        course.setCredits(courseBasicDTO.getCredits());

        // Update department if changed
        if (course.getDepartment().getId() != courseBasicDTO.getDepartment().getId()) {
            Department department = DEPARTMENT_DAO.getById(courseBasicDTO.getDepartment().getId());
            assert department != null;
            course.setDepartment(department);
        }
        COURSE_DAO.update(course);

        courseBasicDTO = Mapper.toCourseDTO(course);
        LOG.debug("Updated course: {}", courseBasicDTO.toString());
        return Response.ok(courseBasicDTO).build();
    }
}
