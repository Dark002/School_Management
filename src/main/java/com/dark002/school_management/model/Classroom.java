package com.dark002.school_management.model;

public class Classroom {
    private int id;
    private String name;
    private int fee;
    private int sessionId;

    
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	// Extra fields
    // Will change with every student
    private ClassroomRegistration classroomRegistration;

    public ClassroomRegistration getClassroomRegistration() {
        return classroomRegistration;
    }

    public void setClassroomRegistration(ClassroomRegistration classroomRegistration) {
        this.classroomRegistration = classroomRegistration;
    }
}
