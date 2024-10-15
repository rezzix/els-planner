package els;

import java.time.LocalTime;

public class TimeSlot {
	LocalTime start;
	LocalTime end;
	
	public TimeSlot(LocalTime start, LocalTime end) {
		this.start = start;
		this.end   = end;
	}

	public String toString() {
		return start.toString()+"->"+end.toString();
	}

}
