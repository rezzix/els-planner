package els;

public class Course {
	
	public Course(String subject, Instructor instructor) {
		this.subject = subject;
		this.instructor = instructor;
	}
	String subject;
	Double durationInHours;
	Instructor instructor;
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Double getDurationInHours() {
		return durationInHours;
	}
	public void setDurationInHours(Double durationInHours) {
		this.durationInHours = durationInHours;
	}
	public Instructor getInstructor() {
		return instructor;
	}
	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

}
