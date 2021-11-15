package com.dark002.school_management.model;

public class Subject {
    private int id;
    private String name;
    private String code;
    private Integer teacherId;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    // Extra Properties
    private Teacher assignedTo;

    public Teacher getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Teacher assignedTo) {
        this.assignedTo = assignedTo;
    }
}
