package com.turygin.api.server.model;


public class CourseDTO {
    private String code;
    private String title;

    public CourseDTO() {}
    public CourseDTO(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
