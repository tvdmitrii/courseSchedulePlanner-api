package com.turygin.api.server.resource;

import com.turygin.api.model.CourseBasicDTO;
import com.turygin.api.model.CourseWithSectionsDTO;
import com.turygin.api.model.SectionDTO;
import com.turygin.api.resource.ICartResourse;
import com.turygin.persistence.dao.CourseDao;
import com.turygin.persistence.dao.Dao;
import com.turygin.persistence.entity.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Stream;

@Path("/cart")
public class CartResource implements ICartResourse {

    protected static final Logger LOG = LogManager.getLogger(CartResource.class);

    private final Dao<User> USER_DAO = new Dao<>(User.class);
    private final CourseDao COURSE_DAO = new CourseDao();

    @GET
    @Path("/{userId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<CourseWithSectionsDTO> cartGetCourses(@PathParam("userId") long userId) {
        User user = USER_DAO.getById(userId);
        List<CartCourse> cartCourses = user.getCoursesInCart();
        return Mapper.courseToCourseWithSections(cartCourses);
    }

    @POST
    @Path("{userId}/course/{courseId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public void cartAddCourseToCart(@PathParam("userId") long userId,
                                                     @PathParam("courseId") long courseId) {
        LOG.debug("Accepted request with userId '{}', courseId '{}'", userId, courseId);
        User user = USER_DAO.getById(userId);
        List<CartCourse> cartCourses = user.getCoursesInCart();

        // Check if course is already in cart
        CartCourse newCourse = null;
        for(CartCourse cartCourse : cartCourses) {
            if (cartCourse.getCourse().getId() == courseId) {
                newCourse = cartCourse;
                break;
            }
        }

        // Skip if course is already in cart
        if (newCourse == null) {
            LOG.debug("Course not found in cart. Adding.");
            Course courseToAdd = COURSE_DAO.getById(courseId);
            // Make sure course exists
            if (courseToAdd != null) {
                user.addCourseToCart(new CartCourse(user, courseToAdd));
                USER_DAO.update(user);
                LOG.debug("Added new course.");
            }
        }
    }

    @DELETE
    @Path("{userId}/course/{courseId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public void cartRemoveCourse(@PathParam("userId") long userId, @PathParam("courseId") long courseId) {
        LOG.debug("Accepted request with userId '{}', courseId '{}'", userId, courseId);
        User user = USER_DAO.getById(userId);
        List<CartCourse> cartCourses = user.getCoursesInCart();

        // Find course in cart
        int courseListId = -1;
        for(int i = 0; i < cartCourses.size(); i++) {
            if (cartCourses.get(i).getCourse().getId() == courseId) {
                courseListId = i;
                break;
            }
        }

        // Remove and update if found
        if (courseListId != -1) {
            LOG.debug("Found course in cart. Removing.");
            cartCourses.remove(courseListId);
            USER_DAO.update(user);
        }
    }

    @PUT
    @Path("{userId}/course/{courseId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public CourseWithSectionsDTO cartUpdateCourse(@PathParam("userId") long userId, @PathParam("courseId") long courseId,
                                 List<Long> sectionIds) {
        User user = USER_DAO.getById(userId);
        List<CartCourse> cartCourses = user.getCoursesInCart();

        LOG.debug("Received SectionIDs: {}", sectionIds.toString());

        // Find the course to update
        CartCourse currentCourse = null;
        for(CartCourse cartCourse : cartCourses) {
            if (cartCourse.getCourse().getId() == courseId) {
                currentCourse = cartCourse;
                break;
            }
        }

        // Make sure course was found in cart
        if (currentCourse == null) {
            return null;
        }
        LOG.debug("Found course in cart.");

        // Get all available course sections
        Map<Long,Section> allSections = new HashMap<>();
        for(Section s : currentCourse.getCourse().getSections()) {
            allSections.put(s.getId(), s);
        }
        LOG.debug("Listing all sections: {}",
                String.join(",\n", Stream.of(allSections.values()).map(Object::toString).toList()));

        // Populate selected sections
        List<Section> selectedSections = new ArrayList<>();
        for (Long sectionId : sectionIds) {
            Section section = allSections.get(sectionId);
            if (section != null) {
                selectedSections.add(section);
            }
        }
        LOG.debug("Selected sections: {}",
                String.join(",\n", Stream.of(selectedSections).map(Object::toString).toList()));

        // Update
        currentCourse.setSections(Mapper.sectionToCartSection(currentCourse, selectedSections));
        USER_DAO.update(user);

        // Return updated course
        currentCourse = null;
        for(CartCourse cartCourse : user.getCoursesInCart()) {
            if (cartCourse.getCourse().getId() == courseId) {
                currentCourse = cartCourse;
                break;
            }
        }

        CourseWithSectionsDTO updatedCourse = Mapper.courseToCourseWithSections(currentCourse);
        LOG.debug("Updated course: {}", updatedCourse);
        return updatedCourse;
    }
}
