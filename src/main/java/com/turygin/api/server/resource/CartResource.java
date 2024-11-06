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

    @GET
    @Path("/{userId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<CourseWithSectionsDTO> cartGetCourses(@PathParam("userId") long userId) {
        User user = USER_DAO.getById(userId);
        List<CartCourse> cartCourses = user.getCoursesInCart();
        return Mapper.courseToCourseWithSections(cartCourses);
    }

    @Override
    public void cartAddCourseToCart(long l, long l1) {

    }

    @Override
    public void cartRemoveCourse(long l, long l1) {

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
            }
        }
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
            }
        }

        CourseWithSectionsDTO updatedCourse = Mapper.courseToCourseWithSections(currentCourse);
        LOG.debug("Updated course: {}", updatedCourse);
        return updatedCourse;
    }
}
