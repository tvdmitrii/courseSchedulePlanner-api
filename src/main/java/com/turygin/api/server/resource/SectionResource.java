package com.turygin.api.server.resource;

import com.turygin.api.model.SectionDTO;
import com.turygin.api.resource.ISectionResource;
import com.turygin.persistence.dao.CourseDao;
import com.turygin.persistence.dao.Dao;
import com.turygin.persistence.entity.Course;
import com.turygin.persistence.entity.Instructor;
import com.turygin.persistence.entity.Section;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Time;
import java.util.List;

/**
 * REST API section resource implementation.
 */
@Path("/section")
public class SectionResource implements ISectionResource {

    protected static final Logger LOG = LogManager.getLogger(SectionResource.class);

    /** Handles course related database queries. */
    private final CourseDao COURSE_DAO = new CourseDao();

    private final Dao<Section> SECTION_DAO = new Dao<>(Section.class);

    private final Dao<Instructor> INSTRUCTOR_DAO = new Dao<>(Instructor.class);

    @GET
    @Path("/{courseId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response getAllCourseSections(@PathParam("courseId") long courseId) {
        LOG.debug("Fetching all sections for course ID {}", courseId);
        Course course = COURSE_DAO.getById(courseId);
        List<SectionDTO> sectionDTOs = Mapper.toSectionDTOList(course.getSections());
        LOG.debug("Found {} sections", sectionDTOs.size());
        return Response.ok(sectionDTOs).build();
    }

    @DELETE
    @Path("/{sectionId}")
    public Response deleteSection(@PathParam("sectionId") long sectionId) {
        LOG.debug("Deleting section with ID '{}'", sectionId);
        Section section = SECTION_DAO.getById(sectionId);
        assert section != null;
        SECTION_DAO.delete(section);
        LOG.debug("Section removed");
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @POST
    @Path("/{courseId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response addSection(@PathParam("courseId") long courseId, SectionDTO sectionDTO) {
        assert sectionDTO != null;
        LOG.debug("Inserting section {}", sectionDTO.toString());
        Section section = Mapper.createSection(sectionDTO);

        Instructor instructor = INSTRUCTOR_DAO.getById(sectionDTO.getInstructor().getId());
        assert instructor != null;
        section.setInstructor(instructor);

        Course course = COURSE_DAO.getById(courseId);
        assert course != null;
        section.setCourse(course);

        SECTION_DAO.insert(section);

        sectionDTO = Mapper.toSectionDTO(section);
        LOG.debug("Section inserted: {}", sectionDTO.toString());
        return Response.status(Response.Status.CREATED).entity(sectionDTO).build();
    }

    @PUT
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response updateSection(SectionDTO sectionDTO) {
        assert sectionDTO != null;
        LOG.debug("Updating section {}", sectionDTO.toString());

        // Load section from DB and ensure it exists
        Section section = SECTION_DAO.getById(sectionDTO.getId());
        assert section != null;

        section.setDaysOfWeek(Mapper.toSectionDayOfWeek(sectionDTO.getDaysOfWeek()));
        section.setFromTime(Time.valueOf(sectionDTO.getStartTime().getTime()));
        section.setToTime(Time.valueOf(sectionDTO.getEndTime().getTime()));

        // Update instructor if changed
        if (sectionDTO.getInstructor().getId() != section.getInstructor().getId()) {
            Instructor instructor = INSTRUCTOR_DAO.getById(sectionDTO.getInstructor().getId());
            assert instructor != null;
            section.setInstructor(instructor);
        }
        SECTION_DAO.update(section);

        sectionDTO = Mapper.toSectionDTO(section);
        LOG.debug("Updated section: {}", sectionDTO.toString());
        return Response.ok(sectionDTO).build();
    }
}
