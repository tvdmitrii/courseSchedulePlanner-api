package com.turygin.api.server.resource;

import com.turygin.api.model.CourseWithSectionsDTO;
import com.turygin.api.resource.ICartResource;
import com.turygin.persistence.dao.CourseDao;
import com.turygin.persistence.dao.Dao;
import com.turygin.persistence.entity.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * REST API user cart resource implementation.
 */
@Path("/cart")
public class CartResource implements ICartResource {

    protected static final Logger LOG = LogManager.getLogger(CartResource.class);

    /** Handles user related database queries. */
    private final Dao<User> USER_DAO = new Dao<>(User.class);

    /** Handles course related database queries. */
    private final CourseDao COURSE_DAO = new CourseDao();

    /** Handles cart course related database queries. */
    private final Dao<CartCourse> CART_COURSE_DAO = new Dao<>(CartCourse.class);

    /**
     * Fetches all courses and associated sections that are in user's cart.
     * @param userId user ID
     * @return a list of course with sections DTOs
     */
    @GET
    @Path("/{userId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response cartGetCourses(@PathParam("userId") long userId) {
        LOG.debug("Fetching cart courses for user with ID '{}'", userId);
        User user = USER_DAO.getById(userId);
        assert user != null;

        List<CartCourse> cartCourses = user.getCoursesInCart();
        List<CourseWithSectionsDTO> courseWithSectionsDTOs = Mapper.toCourseWithSections(cartCourses);
        LOG.debug("Found {} courses", courseWithSectionsDTOs.size());
        return Response.ok(courseWithSectionsDTOs).build();
    }

    /**
     * Adds a course to user's cart.
     * @param userId user ID
     * @param courseId course ID
     * @return course DTO object with status 204 if course was added or 200 if course already exists
     */
    @POST
    @Path("{userId}/course/{courseId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response cartAddCourse(@PathParam("userId") long userId,
                                                     @PathParam("courseId") long courseId) {
        LOG.debug("Adding a new course to cart with userId '{}', courseId '{}'", userId, courseId);
        User user = USER_DAO.getById(userId);
        assert user != null;

        // Check if course is already in cart
        List<CartCourse> cartCourses = user.getCoursesInCart();
        CartCourse newCourse = null;
        for(CartCourse cartCourse : cartCourses) {
            if (cartCourse.getCourse().getId() == courseId) {
                newCourse = cartCourse;
                break;
            }
        }

        if(newCourse != null) {
            LOG.debug("Course already exists in cart.");
            return Response.ok(Mapper.cartCourseToCourseDTO(newCourse)).build();
        }

        LOG.debug("Course not found in cart. Adding.");
        Course courseToAdd = COURSE_DAO.getById(courseId);
        assert courseToAdd != null;

        newCourse = new CartCourse(user, courseToAdd);
        CART_COURSE_DAO.insert(newCourse);
        LOG.debug("Added new course.");

        return Response.status(Response.Status.CREATED).entity(Mapper.cartCourseToCourseDTO(newCourse)).build();
    }

    /**
     * Removes a course from user's cart.
     * @param userId user ID
     * @param courseId course ID
     * @return 204 status if removal was successful
     */
    @DELETE
    @Path("{userId}/course/{courseId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response cartRemoveCourse(@PathParam("userId") long userId, @PathParam("courseId") long courseId) {
        LOG.debug("Removing a course from cart with userId '{}', courseId '{}'", userId, courseId);
        User user = USER_DAO.getById(userId);
        assert user != null;

        List<CartCourse> cartCourses = user.getCoursesInCart();

        // Remove course if in cart
        for (CartCourse cartCourse : cartCourses) {
            if (cartCourse.getCourse().getId() == courseId) {
                LOG.debug("Found course in cart. Removing.");
                CART_COURSE_DAO.delete(cartCourse);
                break;
            }
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    /**
     * Updates selected sections on a course in user's cart.
     * @param userId user ID
     * @param courseId course ID
     * @param sectionIds a list of selected section IDs
     * @return an updated course with sections DTO
     */
    @PUT
    @Path("{userId}/course/{courseId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response cartUpdateSections(@PathParam("userId") long userId, @PathParam("courseId") long courseId,
                                 List<Long> sectionIds) {
        LOG.debug("Updating cart course sections with userId '{}', courseId '{}', sectionIds {}",
                userId, courseId, sectionIds.toString());

        User user = USER_DAO.getById(userId);
        assert user != null;
        List<CartCourse> cartCourses = user.getCoursesInCart();

        // Find the course to update
        CartCourse currentCourse = null;
        for(CartCourse cartCourse : cartCourses) {
            if (cartCourse.getCourse().getId() == courseId) {
                currentCourse = cartCourse;
                LOG.debug("Found course in cart.");
                break;
            }
        }

        // Make sure course was found in cart
        assert currentCourse != null;

        // Get all available course sections into a map
        Map<Long,Section> allSections = new HashMap<>();
        for(Section s : currentCourse.getCourse().getSections()) {
            allSections.put(s.getId(), s);
        }

        // Populate selected sections
        List<Section> selectedSections = new ArrayList<>();
        for (Long sectionId : sectionIds) {
            Section section = allSections.get(sectionId);
            if (section != null) {
                selectedSections.add(section);
            }
        }

        // Update
        currentCourse.setSections(Mapper.createCartSection(currentCourse, selectedSections));
        CART_COURSE_DAO.update(currentCourse);


        CourseWithSectionsDTO updatedCourse = Mapper.toCourseWithSections(currentCourse);
        LOG.debug("Updated course: {}", updatedCourse.toString());
        return Response.ok(updatedCourse).build();
    }
}
