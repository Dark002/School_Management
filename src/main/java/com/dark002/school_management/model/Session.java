package com.dark002.school_management.model;

public class Session {
    private int id;
    private int startYear;
    private Boolean isComplete;
    private Boolean isRegistrationOpen;
    
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStartYear() {
		return startYear;
	}
	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}
	public Boolean getIsComplete() {
		return isComplete;
	}
	public void setIsComplete(Boolean isComplete) {
		this.isComplete = isComplete;
	}
	public Boolean getIsRegistrationOpen() {
		return isRegistrationOpen;
	}
	public void setIsRegistrationOpen(Boolean isRegistrationOpen) {
		this.isRegistrationOpen = isRegistrationOpen;
	}
	public void toggleIsComplete() {
        this.isComplete = !this.isComplete;
    }

    public void toggleIsRegistrationOpen() {
        this.isRegistrationOpen = !this.isRegistrationOpen;
    }
}
