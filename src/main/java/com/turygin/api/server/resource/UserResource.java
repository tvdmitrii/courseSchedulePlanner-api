package com.turygin.api.server.resource;

import com.turygin.api.model.ErrorDTO;
import com.turygin.api.model.UserDTO;
import com.turygin.api.resource.IUserResource;
import com.turygin.persistence.dao.Dao;
import com.turygin.persistence.entity.User;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;

/**
 * REST API user resource implementation.
 */
@Path("/user")
public class UserResource implements IUserResource {

    protected static final Logger LOG = LogManager.getLogger(UserResource.class);

    /** Handles user related database queries. */
    private final Dao<User> USER_DAO = new Dao<>(User.class);

    /**
     * Fetches information about an existing user or creates a new one.
     * @param uuid cognito uuid
     * @return user DTO
     */
    @POST
    @Path("")
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    public Response createUserIfNotExists(@FormParam("uuid") String uuid) {
        LOG.debug("Handling user with uuid '{}'", uuid);
        UUID cognitoId = UUID.fromString(uuid);
        List<User> users = USER_DAO.getByPropertyEquals("uuid", cognitoId);

        UserDTO userDTO;
        if (users.isEmpty()) {
            LOG.debug("User does not exist. Creating new user.");
            User user = new User(cognitoId, User.Type.USER);
            USER_DAO.insert(user);
            userDTO = Mapper.toUserDTO(user, true);
        } else if (users.size() == 1) {
            LOG.debug("Found a user.");
            userDTO = Mapper.toUserDTO(users.getFirst(), false);
        } else {
            LOG.error("UUID matches more than one user.");
            return Response.
                    status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(new ErrorDTO(Response.Status.INTERNAL_SERVER_ERROR,
                            "UUID matches more than one user.")).
                    build();
        }

        return Response.ok(userDTO).build();
    }
}
