package com.turygin.api.server.resource;

import com.turygin.api.model.DepartmentBasicDTO;
import com.turygin.api.resource.IDepartmentResource;
import com.turygin.persistence.dao.Dao;
import com.turygin.persistence.entity.Department;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * REST API course department.
 */
@Path("/department")
public class DepartmentResource implements IDepartmentResource {

    private final Dao<Department> DEPARTMENT_DAO = new Dao<>(Department.class);

    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    public DepartmentBasicDTO getDepartment(@PathParam("id") long id) {
        Department department = DEPARTMENT_DAO.getById(id);
        DepartmentBasicDTO departmentDTO = null;
        if (department != null) {
            departmentDTO = new DepartmentBasicDTO(department.getId(), department.getCode(), department.getName());
        }
        return departmentDTO;
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public List<DepartmentBasicDTO> getAllDepartments() {
        List<Department> departments = DEPARTMENT_DAO.getAll();
        List<DepartmentBasicDTO> departmentDTOs = new ArrayList<>();
        departments.forEach(department -> departmentDTOs.add(
                new DepartmentBasicDTO(department.getId(), department.getCode(), department.getName())));
        return departmentDTOs;
    }
}
