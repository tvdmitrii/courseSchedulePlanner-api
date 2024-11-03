package com.turygin.api.server.resource;

import com.turygin.api.model.CourseBasicDTO;
import com.turygin.api.model.CourseWithSectionsDTO;
import com.turygin.api.model.DepartmentBasicDTO;
import com.turygin.api.model.SectionDTO;
import com.turygin.persistence.entity.Course;
import com.turygin.persistence.entity.Department;
import com.turygin.persistence.entity.Section;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

    public static CourseBasicDTO mapToCourseBasic(Course course) {
        return course != null ? new CourseBasicDTO(course.getId(), course.getCode(), course.getTitle(),
                course.getDescription(), course.getCredits()) : null;
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
                section.getMeetingTimesString(), section.getInstructor().toString()) : null;
    }

    public static List<SectionDTO> mapToSection(List<Section> sections) {
        List<SectionDTO> sectionDTOList = new ArrayList<>();
        for (Section s : sections) {
            if (s == null) continue;
            sectionDTOList.add(mapToSection(s));
        }
        return sectionDTOList;
    }

    public static List<CourseWithSectionsDTO> mapToCourseWithSections(List<Course> courses) {
        List<CourseWithSectionsDTO> courseDTOs = new ArrayList<>();
        for (Course c : courses) {
            if (c == null) continue;
            CourseWithSectionsDTO courseWithSectionsDTO = new CourseWithSectionsDTO(mapToCourseBasic(c));
            List<SectionDTO> sectionDTOList = mapToSection(c.getSections());
            courseWithSectionsDTO.setSections(sectionDTOList);
            courseDTOs.add(courseWithSectionsDTO);
        }
        return courseDTOs;
    }
}
