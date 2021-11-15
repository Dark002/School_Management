package com.dark002.school_management.model;

import java.util.List;

public class ClassroomRegistration {
    private int id;
    private String studentRollNo;
    private int classroomId;

    public int getId() {
        return id;
    }

    public int getClassroomId() {
        return classroomId;
    }

    public String getStudentRollNo() {
        return studentRollNo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setClassroomId(int classroomId) {
        this.classroomId = classroomId;
    }

    public void setStudentRollNo(String studentRollNo) {
        this.studentRollNo = studentRollNo;
    }

    // Extra Fields
    private List<SubjectRegistration> subjectRegistrations;
    private Classroom classroom;
    private Student student;

    public List<SubjectRegistration> getSubjectRegistrations() {
        return subjectRegistrations;
    }

    public void setSubjectRegistrations(List<SubjectRegistration> subjectRegistrations) {
        this.subjectRegistrations = subjectRegistrations;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }
}
