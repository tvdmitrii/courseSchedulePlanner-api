package com.turygin.api.server.resource;

import com.turygin.api.model.CourseBasicDTO;
import com.turygin.api.model.CourseWithSectionsDTO;
import com.turygin.api.model.SectionDTO;
import com.turygin.api.resource.ICartResourse;
import com.turygin.persistence.dao.CourseDao;
import com.turygin.persistence.dao.Dao;
import com.turygin.persistence.entity.CartCourse;
import com.turygin.persistence.entity.Course;
import com.turygin.persistence.entity.Section;
import com.turygin.persistence.entity.User;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

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
        return Mapper.mapToCourseWithSections(cartCourses);
    }

    @Override
    public void cartAddCourseToCart(long l, long l1) {

    }

    @Override
    public void cartRemoveCourse(long l, long l1) {

    }

    @Override
    public void cartUpdateCourse(long l, long l1, List<Long> list) {

    }
}
