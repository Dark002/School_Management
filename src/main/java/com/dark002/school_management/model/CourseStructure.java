package com.dark002.school_management.model;

import java.util.List;

public class CourseStructure {
    private int id;
    private int streamId;
    private int classroomId;

    public int getId() {
        return id;
    }

    public int getStreamId() {
        return streamId;
    }

    public int getClassroomId() {
        return classroomId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStreamId(int streamId) {
        this.streamId = streamId;
    }

    public void setClassroomId(int classroomId) {
        this.classroomId = classroomId;
    }

    // Extra Fields
    private List<Subject> optionalSubjects;
    private List<Subject> compulsorySubjects;
    private Stream stream;
    private Classroom classroom;

    public List<Subject> getCompulsorySubjects() {
        return compulsorySubjects;
    }

    public List<Subject> getOptionalSubjects() {
        return optionalSubjects;
    }

    public void setCompulsorySubjects(List<Subject> compulsorySubjects) {
        this.compulsorySubjects = compulsorySubjects;
    }

    public void setOptionalSubjects(List<Subject> optionalSubjects) {
        this.optionalSubjects = optionalSubjects;
    }

    public Stream getStream() {
        return stream;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }
}
