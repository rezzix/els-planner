package els;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class WeeklySession {

	@PlanningId
    private Long id;
	
	@PlanningVariable
	TimeSlot timeSlot;

	@PlanningVariable
	Facility facility;
	
	@PlanningVariable(valueRangeProviderRefs = "dayOfWeekValueRange")
	String dayOfWeek;
	
	public String getDayOfWeek() {
		return dayOfWeek;
	}


	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}


	public String getStudentGroup() {
		return studentGroup;
	}


	public void setStudentGroup(String studentGroup) {
		this.studentGroup = studentGroup;
	}

	Course course;
	String studentGroup;
	
	public WeeklySession(long id, Course course, String studentGroup) {
		this.id = id;
		this.course = course;
		this.studentGroup = studentGroup;
	}
	
	public WeeklySession() {
		
	}

	public TimeSlot getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(TimeSlot timeSlot) {
		this.timeSlot = timeSlot;
	}

	public Facility getFacility() {
		return facility;
	}

	public void setFacility(Facility facility) {
		this.facility = facility;
	}

	public Course getCourse() {
		return course;
	}
	
	public Instructor getInstructor() {
		return course.getInstructor();
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDayTimeSlot() {
		return dayOfWeek + timeSlot;
	}

	public String toString() {
		return course.subject + ", " + course.instructor.name + " @" + facility;
	}

	public String toLongString() {
		return dayOfWeek + "-" + timeSlot + " | " + course.subject + " " + course.instructor.name + " " + studentGroup;
	}
	
	
}
