package els;

import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@PlanningSolution
public class TimeTable {

    @ValueRangeProvider
    @ProblemFactCollectionProperty
    private List<TimeSlot> timeslotList;
    
    @ValueRangeProvider
    @ProblemFactCollectionProperty
    private List<Facility> facilityList;
    
    List<String> dayOfWeekList;
    
    @ValueRangeProvider(id = "dayOfWeekValueRange")
    public List<String> getDayOfWeekValueRange() {
        // Define possible values for the string variable, e.g., room names, subjects, etc.
        return List.of("Mon", "Tue", "Wed", "Thu");
    }
    
    @PlanningEntityCollectionProperty
    private List<WeeklySession> sessionList;
    
    

    @PlanningScore
    private HardSoftScore score;

    public TimeTable() {
    }

    public TimeTable(List<TimeSlot> timeslotList, List<Facility> facilityList, List<WeeklySession> sessionList, List<String> dayOfWeekList) {
        this.timeslotList = timeslotList;
        this.facilityList = facilityList;
        this.sessionList = sessionList;
        this.dayOfWeekList = dayOfWeekList;
    }

    public List<TimeSlot> getTimeslotList() {
        return timeslotList;
    }

    public List<Facility> getFacilityList() {
        return facilityList;
    }

    public List<WeeklySession> getSessionList() {
        return sessionList;
    }

    public HardSoftScore getScore() {
        return score;
    }

}