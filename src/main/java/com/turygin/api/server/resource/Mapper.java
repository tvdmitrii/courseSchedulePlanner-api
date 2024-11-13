package com.turygin.api.server.resource;

import com.turygin.api.model.CourseBasicDTO;
import com.turygin.api.model.CourseWithSectionsDTO;
import com.turygin.api.model.DepartmentBasicDTO;
import com.turygin.api.model.SectionDTO;
import com.turygin.persistence.entity.*;

import java.util.*;

public class Mapper {

    public static CourseBasicDTO mapToCourseBasic(Course course) {
        return course != null ? new CourseBasicDTO(course.getId(), course.getCode(), course.getTitle(),
                course.getDescription(), course.getCredits(), course.getDepartment().getId(), course.getNumber()) : null;
    }

    public static List<CourseBasicDTO> mapToCourseBasic(List<Course> courses) {
        List<CourseBasicDTO> courseBasicDTOList = new ArrayList<>();
        for (Course c : courses) {
            if (c == null) continue;
            courseBasicDTOList.add(mapToCourseBasic(c));
        }
        return courseBasicDTOList;
    }

    public static DepartmentBasicDTO mapToDepartmentBasic(Department department) {
        return department != null ?
                new DepartmentBasicDTO(department.getId(), department.getCode(), department.getName()) : null;
    }

    public static List<DepartmentBasicDTO> mapToDepartmentBasic(List<Department> departments) {
        List<DepartmentBasicDTO> departmentBasicDTOList = new ArrayList<>();
        for (Department d : departments) {
            if (d == null) continue;
            departmentBasicDTOList.add(mapToDepartmentBasic(d));
        }
        return departmentBasicDTOList;
    }

    public static SectionDTO mapToSection(Section section) {
        return section != null ? new SectionDTO(section.getId(), section.getMeetingDaysString(),
                section.getMeetingTimesString(), section.getInstructor().getFullName()) : null;
    }

    public static SortedMap<Long,SectionDTO> mapToSection(List<Section> sections) {
        SortedMap<Long,SectionDTO> sectionMap = new TreeMap<>();
        for (Section s : sections) {
            if (s == null) continue;
            sectionMap.put(s.getId(), mapToSection(s));
        }
        return sectionMap;
    }

    public static CartSection sectionToCartSection(CartCourse cartCourse, Section section) {
        return section != null ? new CartSection(cartCourse, section) : null;
    }

    public static List<CartSection> sectionToCartSection(CartCourse cartCourse, List<Section> sections) {
        List<CartSection> sectionList = new ArrayList<>();
        for (Section s : sections) {
            if (s == null) continue;
            sectionList.add(sectionToCartSection(cartCourse, s));
        }
        return sectionList;
    }

    public static SectionDTO mapCartSectionToSection(CartSection cartSection) {
        return cartSection != null ? mapToSection(cartSection.getSection()) : null;
    }

    public static SortedMap<Long,SectionDTO> mapCartSectionToSection(List<CartSection> sections) {
        SortedMap<Long,SectionDTO> sectionMap = new TreeMap<>();
        for (CartSection cs : sections) {
            if (cs == null) continue;
            SectionDTO mappedSection = mapCartSectionToSection(cs);
            sectionMap.put(mappedSection.getId(), mappedSection);
        }
        return sectionMap;
    }

    public static CourseWithSectionsDTO mapToCourseWithAllSections(Course course) {
        if (course == null) return null;
        CourseWithSectionsDTO courseWithSectionsDTO = new CourseWithSectionsDTO(mapToCourseBasic(course));
        SortedMap<Long,SectionDTO> allSectionsMap = mapToSection(course.getSections());
        courseWithSectionsDTO.setSections(allSectionsMap);
        return courseWithSectionsDTO;
    }

    public static List<CourseWithSectionsDTO> courseToCourseWithSections(List<CartCourse> courses) {
        List<CourseWithSectionsDTO> courseDTOs = new ArrayList<>();
        for (CartCourse cc : courses) {
            if (cc == null) continue;
            courseDTOs.add(courseToCourseWithSections(cc));
        }
        return courseDTOs;
    }

    public static CourseWithSectionsDTO courseToCourseWithSections(CartCourse course) {
        if (course == null) {
            return null;
        }
        // Get course with all available sections unselected.
        CourseWithSectionsDTO mappedCourse = mapToCourseWithAllSections(course.getCourse());
        // Load all sections that user has in their cart
        SortedMap<Long,SectionDTO> selectedSections = mapCartSectionToSection(course.getSections());
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

    public static Course courseDTOToCourse(CourseBasicDTO courseBasicDTO) {
        return courseBasicDTO != null ?
                new Course(courseBasicDTO.getTitle(), courseBasicDTO.getDescription(),
                        courseBasicDTO.getCredits(), courseBasicDTO.getNumber()) : null;
    }

}
