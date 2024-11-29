package com.turygin.api.server.resource;

import com.turygin.api.model.*;
import com.turygin.persistence.entity.*;

import java.sql.Time;
import java.util.*;

/**
 * Helper class that facilitates conversion between entities and DTOs.
 */
public class Mapper {

    /**
     * Converts course entity into course DTO.
     * @param course course entity
     * @return course DTO
     */
    public static CourseDTO toCourseDTO(Course course) {
        return new CourseDTO(course.getId(), course.getTitle(), course.getDescription(),
                course.getCredits(), course.getNumber(), toDepartmentDTO(course.getDepartment()));
    }

    /**
     * Converts a list of course entities into a list of course DTOs.
     * @param courses list of course entities
     * @return list of course DTOs
     */
    public static List<CourseDTO> toCourseDTO(List<Course> courses) {
        List<CourseDTO> courseDTOList = new ArrayList<>();
        for (Course c : courses) {
            courseDTOList.add(toCourseDTO(c));
        }
        return courseDTOList;
    }

    /**
     * Converts department entity into department DTO.
     * @param department department entity
     * @return department DTO
     */
    public static DepartmentDTO toDepartmentDTO(Department department) {
        return new DepartmentDTO(department.getId(), department.getCode(), department.getName());
    }

    /**
     * Converts a list of department entities into a list of department DTOs.
     * @param departments list of department entities
     * @return list of department DTOs
     */
    public static List<DepartmentDTO> toDepartmentDTO(List<Department> departments) {
        List<DepartmentDTO> departmentBasicDTOList = new ArrayList<>();
        for (Department d : departments) {
            departmentBasicDTOList.add(toDepartmentDTO(d));
        }
        return departmentBasicDTOList;
    }

    /**
     * Converts from section's way of storing days of week as a byte to days of week DTO.
     * @param daysOfWeek byte representing a combination of Section.Day values
     * @return days of week DTO
     */
    public static DaysOfWeekDTO toDaysOfWeekDTO(byte daysOfWeek) {
        DaysOfWeekDTO daysOfWeekDTO = new DaysOfWeekDTO();
        Boolean[] selectedDays = new Boolean[5];
        Section.Day[] days = Section.Day.values();
        for(int i = 0; i < days.length; i++) {
            if((daysOfWeek & days[i].value) != 0 ) {
                selectedDays[i] = true;
            } else {
                selectedDays[i] = false;
            }
        }
        daysOfWeekDTO.setDaysOfWeek(selectedDays);
        return daysOfWeekDTO;
    }

    /**
     * Converts SQL Time to meeting time DTO.
     * @param time SQL time
     * @return meeting time DTO
     */
    public static MeetingTimeDTO toMeetingTimeDTO(Time time) {
        return new MeetingTimeDTO(time.toLocalTime());
    }

    /**
     * Converts instructor entity to instructor DTO.
     * @param instructor instructor entity
     * @return instructor DTO
     */
    public static InstructorDTO toInstructorDTO(Instructor instructor) {
        return new InstructorDTO(instructor.getId(), instructor.getFullName());
    }

    /**
     * Converts a list of instructor entities to a list of instructor DTOs.
     * @param instructors a list of instructor entities
     * @return a list of instructor DTOs
     */
    public static List<InstructorDTO> toInstructorDTO(List<Instructor> instructors) {
        List<InstructorDTO> instructorList = new ArrayList<>();
        for (Instructor i : instructors) {
            instructorList.add(toInstructorDTO(i));
        }
        return instructorList;
    }

    /**
     * Converts section entity into section DTO.
     * @param section section entity
     * @return section DTO
     */
    public static SectionDTO toSectionDTO(Section section) {
        return new SectionDTO(section.getId(),
                toDaysOfWeekDTO(section.getDaysOfWeek()),
                toMeetingTimeDTO(section.getFromTime()),
                toMeetingTimeDTO(section.getToTime()),
                toInstructorDTO(section.getInstructor()));
    }

    /**
     * Convert section entity list to a sorted map with ID as the key and section DTO as the value.
     * @param sections section entity list
     * @return sorted map with ID as the key and section DTO as the value
     */
    public static SortedMap<Long,SectionDTO> toSectionDTOMap(List<Section> sections) {
        SortedMap<Long,SectionDTO> sectionMap = new TreeMap<>();
        for (Section s : sections) {
            sectionMap.put(s.getId(), toSectionDTO(s));
        }
        return sectionMap;
    }

    /**
     * Convert a list of section entities to a list of section DTOs.
     * @param sections a list of section entities
     * @return a list of section DTOs
     */
    public static List<SectionDTO> toSectionDTOList(List<Section> sections) {
        List<SectionDTO> sectionList = new ArrayList<>();
        for (Section s : sections) {
            sectionList.add(toSectionDTO(s));
        }
        return sectionList;
    }

    /**
     * Creates a cart section from cart course and section entities.
     * @param cartCourse cart course entity
     * @param section section entity
     * @return cart section entity
     */
    public static CartSection createCartSection(CartCourse cartCourse, Section section) {
        return new CartSection(cartCourse, section);
    }

    /**
     * Creates a cart section list from cart course and section entity list.
     * @param cartCourse cart course entity
     * @param sections section entity list
     * @return cart section entity list
     */
    public static List<CartSection> createCartSection(CartCourse cartCourse, List<Section> sections) {
        List<CartSection> sectionList = new ArrayList<>();
        for (Section s : sections) {
            sectionList.add(createCartSection(cartCourse, s));
        }
        return sectionList;
    }

    /**
     * Converts cart section entity to section DTO.
     * @param cartSection cart section entity
     * @return section DTO
     */
    public static SectionDTO cartSectionToSectionDTO(CartSection cartSection) {
        return toSectionDTO(cartSection.getSection());
    }

    /**
     * Converts a list of cart section entities to a sorted map with ID as the key and section DTO as the value
     * @param sections cart section entity list
     * @return sorted map with ID as the key and section DTO as the value
     */
    public static SortedMap<Long,SectionDTO> cartSectionToSectionDTO(List<CartSection> sections) {
        SortedMap<Long,SectionDTO> sectionMap = new TreeMap<>();
        for (CartSection cs : sections) {
            SectionDTO mappedSection = cartSectionToSectionDTO(cs);
            sectionMap.put(mappedSection.getId(), mappedSection);
        }
        return sectionMap;
    }

    /**
     * Converts a course entity to course DTO with a sorted map of sections.
     * @param course course entity
     * @return course DTO with a sorted map of sections
     */
    public static CourseWithSectionsDTO toCourseWithAllSections(Course course) {
        CourseWithSectionsDTO courseWithSectionsDTO = new CourseWithSectionsDTO(toCourseDTO(course));
        SortedMap<Long,SectionDTO> allSectionsMap = toSectionDTOMap(course.getSections());
        courseWithSectionsDTO.setSections(allSectionsMap);
        return courseWithSectionsDTO;
    }

    /**
     * Converts a list of cart course entities to a list of course with sections DTO.
     * The courses contain all the available sections and the ones that are in user's cart
     * are selected by setting isSelected to true.
     *
     * @param courses a list of cart course entities
     * @return a list of course with sections DTO
     */
    public static List<CourseWithSectionsDTO> toCourseWithSections(List<CartCourse> courses) {
        List<CourseWithSectionsDTO> courseDTOs = new ArrayList<>();
        for (CartCourse cc : courses) {
            courseDTOs.add(toCourseWithSections(cc));
        }
        return courseDTOs;
    }

    /**
     * Converts a cart course entity to a course with sections DTO.
     * The course contains all the available sections and the ones that are in user's cart
     * are selected by setting isSelected to true.
     *
     * @param course cart course entity
     * @return course with sections DTO
     */
    public static CourseWithSectionsDTO toCourseWithSections(CartCourse course) {
        // Get course with all available sections unselected.
        CourseWithSectionsDTO mappedCourse = toCourseWithAllSections(course.getCourse());
        // Load all sections that user has in their cart
        SortedMap<Long,SectionDTO> selectedSections = cartSectionToSectionDTO(course.getSections());
        // Mark selected sections as such
        SortedMap<Long,SectionDTO> allSections = mappedCourse.getSections();
        for (Long sectionId : selectedSections.keySet()) {
            SectionDTO currentSection = allSections.get(sectionId);
            if (currentSection != null) {
                currentSection.setIsSelected(true);
            }
        }
        return mappedCourse;
    }

    /**
     * Creates course entity from course DTO. Neither sections nor department are populated.
     * @param courseDTO course DTO.
     * @return course entity
     */
    public static Course createCourse(CourseDTO courseDTO) {
        return courseDTO != null ?
                new Course(courseDTO.getTitle(), courseDTO.getDescription(),
                        courseDTO.getCredits(), courseDTO.getNumber()) : null;
    }

    /**
     * Creates a section entity from a section DTO by populating meeting days and times.
     * @param sectionDTO source section DTO
     * @return section entity with meeting days and times populated
     */
    public static Section createSection(SectionDTO sectionDTO) {
        if(sectionDTO == null) { return null; }

        int daysOfWeek = toSectionDayOfWeek(sectionDTO.getDaysOfWeek());

        return new Section(daysOfWeek,
                Time.valueOf(sectionDTO.getStartTime().getTime()),
                Time.valueOf(sectionDTO.getEndTime().getTime()));
    }

    /**
     * Convert days of week DTO into an integer (byte) used by section entity.
     * @param daysOfWeekDTO source days of week DTO
     * @return an integer that encodes meeting days for section entity
     */
    public static int toSectionDayOfWeek(DaysOfWeekDTO daysOfWeekDTO) {
        int daysOfWeek = 0;
        Boolean[] selectedDays = daysOfWeekDTO.getDaysOfWeek();
        Section.Day[] days = Section.Day.values();
        for(int i = 0; i < selectedDays.length; i++) {
            if(selectedDays[i]) {
                daysOfWeek = daysOfWeek | days[i].value;
            }
        }
        return daysOfWeek;
    }

    /**
     * Maps user entity to user DTO.
     * @param user user entity
     * @param isNew is this a newly created user
     * @return user DTO
     */
    public static UserDTO toUserDTO(User user, boolean isNew) {
        return new UserDTO(user.getId(), isNew, user.getRole() == User.Type.ADMIN);
    }

    /**
     * Converts cart course entity to course DTO.
     * @param cartCourse cart course entity
     * @return course DTO
     */
    public static CourseDTO cartCourseToCourseDTO(CartCourse cartCourse) {
        return Mapper.toCourseDTO(cartCourse.getCourse());
    }

    /**
     * Converts a section into a section with course DTO.
     * @param section source section entity
     * @return section with course DTO containing both section and course information
     */
    public static SectionWithCourseDTO toSectionWithCourseDTO(Section section) {
        SectionDTO sectionDTO = Mapper.toSectionDTO(section);
        CourseDTO courseDTO = Mapper.toCourseDTO(section.getCourse());
        return new SectionWithCourseDTO(sectionDTO, courseDTO);
    }

    /**
     * Converts schedule entity to schedule DTO.
     * @param schedule source schedule entity
     * @return schedule DTO
     */
    public static ScheduleDTO toScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO(schedule.getId());

        // Populate sections
        List<SectionWithCourseDTO> sections = new ArrayList<>();
        for (ScheduleSection scheduleSection : schedule.getSections()) {
            sections.add(toSectionWithCourseDTO(scheduleSection.getSection()));
        }
        scheduleDTO.setSections(sections);

        return scheduleDTO;
    }

    /**
     * Convert a list of schedule entities to a list of schedule DTOs.
     * @param schedules source list of schedule entities
     * @return list of schedule DTOs
     */
    public static List<ScheduleDTO> toScheduleDTO(List<Schedule> schedules) {
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        for (Schedule schedule : schedules) {
            scheduleDTOs.add(toScheduleDTO(schedule));
        }
        return scheduleDTOs;
    }
}
