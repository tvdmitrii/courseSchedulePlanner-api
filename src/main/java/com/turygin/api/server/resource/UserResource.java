package com.turygin.api.server.resource;

import com.turygin.api.model.UserDTO;
import com.turygin.api.resource.IUserResource;
import com.turygin.persistence.dao.Dao;
import com.turygin.persistence.entity.User;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;

/**
 * REST API user resource.
 */
@Path("/user")
public class UserResource implements IUserResource {

    private final Dao<User> USER_DAO = new Dao<>(User.class);
    protected static final Logger LOG = LogManager.getLogger(UserResource.class);

    @POST
    @Path("")
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    public UserDTO createUserIfNotExists(@FormParam("uuid") String uuid) {
        LOG.debug("Received uuid '{}'", uuid);
        try {
            UUID cognitoId = UUID.fromString(uuid);
            List<User> users = USER_DAO.getByPropertyEquals("uuid", cognitoId);

            boolean isNewUser = users.size() != 1;
            User user;

            if (isNewUser) {
                user = new User(cognitoId, User.Type.USER);
                USER_DAO.insert(user);
            } else {
                user = users.get(0);
            }

            return new UserDTO(user.getId(), isNewUser, user.getRole() == User.Type.ADMIN);
        } catch (Exception e) {
            return null;
        }
    }
}
