package com.turygin.api.server.resource;

import com.turygin.api.model.InstructorDTO;
import com.turygin.api.resource.IInstructorResource;
import com.turygin.persistence.dao.Dao;
import com.turygin.persistence.entity.Instructor;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * REST API instructor resource implementation.
 */
@Path("/instructor")
public class InstructorResource implements IInstructorResource {

    protected static final Logger LOG = LogManager.getLogger(InstructorResource.class);

    /** Handles instructor related database queries. */
    private final Dao<Instructor> INSTRUCTOR_DAO = new Dao<>(Instructor.class);

    /**
     * Fetches information about all available instructors.
     * @return a list of instructor DTOs
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public Response getAllInstructors() {
        LOG.debug("Fetching all instructors");
        List<Instructor> instructors = INSTRUCTOR_DAO.getAll();
        List<InstructorDTO> instructorDTOs = Mapper.toInstructorDTO(instructors);
        LOG.debug("Found {} instructors", instructorDTOs.size());
        return Response.ok(instructorDTOs).build();
    }
}
