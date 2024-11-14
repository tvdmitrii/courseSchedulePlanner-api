package com.turygin.api.server.resource;

import com.turygin.api.model.DepartmentDTO;
import com.turygin.api.resource.IDepartmentResource;
import com.turygin.persistence.dao.Dao;
import com.turygin.persistence.entity.Department;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * REST API department resource implementation.
 */
@Path("/department")
public class DepartmentResource implements IDepartmentResource {

    protected static final Logger LOG = LogManager.getLogger(DepartmentResource.class);

    /** Handles department related database queries. */
    private final Dao<Department> DEPARTMENT_DAO = new Dao<>(Department.class);

    /**
     * Fetches a department by ID.
     * @param id department ID
     * @return department DTO
     */
    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response getDepartment(@PathParam("id") long id) {
        LOG.debug("Fetching department with ID '{}'", id);
        Department department = DEPARTMENT_DAO.getById(id);
        assert department != null;

        DepartmentDTO departmentDTO = Mapper.toDepartmentDTO(department);
        LOG.debug("Found department '{}'", departmentDTO.toString());
        return Response.ok(departmentDTO).build();
    }

    /**
     * Fetches information about all available departments.
     * @return aa list of department DTOs
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public Response getAllDepartments() {
        LOG.debug("Fetching all departments");
        List<Department> departments = DEPARTMENT_DAO.getAll();

        List<DepartmentDTO> departmentDTOs = Mapper.toDepartmentDTO(departments);
        LOG.debug("Found {} departments", departmentDTOs.size());
        return Response.ok(departmentDTOs).build();
    }
}
