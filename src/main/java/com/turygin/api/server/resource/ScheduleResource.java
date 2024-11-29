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
 * REST API schedule resource implementation.
 */
@Path("/schedule")
public class ScheduleResource implements IScheduleResource {

    protected static final Logger LOG = LogManager.getLogger(ScheduleResource.class);

    /** Handles user related database queries. */
    private final Dao<User> USER_DAO = new Dao<>(User.class);

    /** Handles schedule related database queries. */
    private final Dao<Schedule> SCHEDULE_DAO = new Dao<>(Schedule.class);

    /**
     * Recursive backtracking algorithm that generates a list of all possible schedules.
     * @param courses a list of all courses that must be represented in the schedule
     * @param currentCourseIndex index of the current course being processed
     * @param currentSchedule current schedule represented by a list of sections
     * @param schedules a list of schedules (list of section lists)
     */
    private void recursiveScheduleBuilder(List<CartCourse> courses, int currentCourseIndex,
                                          List<Section> currentSchedule, List<List<Section>> schedules) {

        // Check if we have a schedule built ...
        if (currentCourseIndex == courses.size()) {
            // ... if so, add a copy of it to the list and return
            schedules.add(new ArrayList<>(currentSchedule));
            return;
        }

        CartCourse currentCourse = courses.get(currentCourseIndex);

        // Go through each cart section (selected section) in a cart course
        for (CartSection cartSection : currentCourse.getSections()) {
            Section section = cartSection.getSection();
            // Check if the section does not conflict with the current schedule. Skip if conflicts.
            if(!isConflicting(currentSchedule, section)) {
                // Add section to schedule
                currentSchedule.add(section);
                // Recursively process the next course
                recursiveScheduleBuilder(courses, currentCourseIndex + 1, currentSchedule, schedules);
                // All schedules with this section have been built. Remove it and try the next one.
                currentSchedule.removeLast();
            }
        }
    }

    /**
     * Helper method that checks whether a section conflicts with an existing schedule.
     * @param schedule a list of sections comprising a schedule
     * @param section section to check for conflict
     * @return true if section is conflicting with schedule, false otherwise
     */
    private boolean isConflicting(List<Section> schedule, Section section) {
        for (Section existingSections : schedule) {
            if (existingSections.isConflicting(section)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generates and returns all available schedules based on user's cart.
     * @param userId unique user ID
     * @return a list of schedule DTOs
     */
    @GET
    @Path("/{userId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response getSchedules(@PathParam("userId") long userId) {
        LOG.debug("Generating schedules for user with ID '{}'", userId);
        User user = USER_DAO.getById(userId);
        assert user != null;

        // Clear existing schedules
        user.setSchedules(null);
        USER_DAO.update(user);

        // Get courses in cart while filtering out the ones without any sections selected
        List<CartCourse> coursesWithSections = user.getCoursesInCart().stream().
                filter(cartCourse -> !cartCourse.getSections().isEmpty()).toList();

        // Build schedules
        List<Section> currentSchedule = new ArrayList<>();
        List<List<Section>> allSchedules = new ArrayList<>();
        recursiveScheduleBuilder(coursesWithSections, 0, currentSchedule, allSchedules);

        // Create schedule entities from section lists and insert them into the database
        List<Schedule> scheduleEntities = new ArrayList<>();
        for(List<Section> schedule : allSchedules) {
            Schedule scheduleEntity = new Schedule(user);

            // Create schedule sections from sections
            List<ScheduleSection> scheduleSections = new ArrayList<>();
            for(Section section : schedule) {
                scheduleSections.add(new ScheduleSection(scheduleEntity, section));
            }
            scheduleEntity.setSections(scheduleSections);

            // Insert schedule into the database
            SCHEDULE_DAO.insert(scheduleEntity);

            // Store to return from the endpoint
            scheduleEntities.add(scheduleEntity);
        }

        List<ScheduleDTO> scheduleDTOs = Mapper.toScheduleDTO(scheduleEntities);
        LOG.debug("Found {} schedules", scheduleDTOs.size());
        return Response.ok(scheduleDTOs).build();
    }
}
