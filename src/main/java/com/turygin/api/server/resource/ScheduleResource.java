package com.turygin.api.server.resource;

import com.turygin.api.model.ScheduleDTO;
import com.turygin.api.resource.IScheduleResource;
import com.turygin.persistence.dao.Dao;
import com.turygin.persistence.entity.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * REST API user cart resource implementation.
 */
@Path("/schedule")
public class ScheduleResource implements IScheduleResource {

    protected static final Logger LOG = LogManager.getLogger(ScheduleResource.class);

    /** Handles user related database queries. */
    private final Dao<User> USER_DAO = new Dao<>(User.class);

    /** Handles schedule related database queries. */
    private final Dao<Schedule> SCHEDULE_DAO = new Dao<>(Schedule.class);

    private void recursiveScheduleBuilder(List<CartCourse> courses, int currentCourseIndex,
                                          List<Section> currentSchedule, List<List<Section>> schedules) {

        // Check if we have a schedule built
        if (currentCourseIndex == courses.size()) {
            schedules.add(new ArrayList<>(currentSchedule));
            return;
        }

        CartCourse currentCourse = courses.get(currentCourseIndex);
        for (CartSection cartSection : currentCourse.getSections()) {
            Section section = cartSection.getSection();
            // If the section does not conflict with the current schedule
            if(!isConflicting(currentSchedule, section)) {
                currentSchedule.add(section);
                // Proceed to the next course
                recursiveScheduleBuilder(courses, currentCourseIndex + 1, currentSchedule, schedules);
                // All schedules with this section have been built. Remove it and try the next one.
                currentSchedule.removeLast();
            }
        }
    }

    private boolean isConflicting(List<Section> schedule, Section section) {
        for (Section existingSections : schedule) {
            if (existingSections.isConflicting(section)) {
                return true;
            }
        }
        return false;
    }

    @GET
    @Path("/{userId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response getSchedules(@PathParam("userId") long userId) {
        LOG.debug("Generating schedules for user with ID '{}'", userId);
        User user = USER_DAO.getById(userId);
        assert user != null;

        // Clear schedules
        user.replaceSchedules(null);
        USER_DAO.update(user);

        // Build schedules
        List<Section> schedule = new ArrayList<>();
        List<List<Section>> schedules = new ArrayList<>();

        // Get courses in cart while filtering out the ones without any sections selected
        List<CartCourse> courses = user.getCoursesInCart().stream().
                filter(cartCourse -> cartCourse.getSections() != null && !cartCourse.getSections().isEmpty()).toList();
        recursiveScheduleBuilder(courses, 0, schedule, schedules);

        // Generate schedule entities from section lists
        List<Schedule> scheduleEnitites = new ArrayList<>();
        for(List<Section> sections : schedules) {
            Schedule scheduleEntity = new Schedule(user);
            List<ScheduleSection> scheduleSections = new ArrayList<>();
            for(Section section : sections) {
                scheduleSections.add(new ScheduleSection(scheduleEntity, section));
            }
            scheduleEntity.setSections(scheduleSections);
            SCHEDULE_DAO.insert(scheduleEntity);
            scheduleEnitites.add(scheduleEntity);
        }

        List<ScheduleDTO> scheduleDTOs = Mapper.toScheduleDTO(scheduleEnitites);
        LOG.debug("Found {} schedules", scheduleDTOs.size());
        return Response.ok(scheduleDTOs).build();
    }
}
