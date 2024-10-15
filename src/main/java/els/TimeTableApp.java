package els;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.optaplanner.core.api.score.ScoreExplanation;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.solver.SolutionManager;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeTableApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeTableApp.class);

    public static void main(String[] args) {
        SolverFactory<TimeTable> solverFactory = SolverFactory.create(new SolverConfig()
                .withSolutionClass(TimeTable.class)
                .withEntityClasses(WeeklySession.class)
                .withConstraintProviderClass(TimeTableConstraintProvider.class)
                // The solver runs only for 5 seconds on this small dataset.
                // It's recommended to run for at least 5 minutes ("5m") otherwise.
                .withTerminationSpentLimit(Duration.ofSeconds(5)));

        // Load the problem
        TimeTable problem = generateDemoData();

        // Solve the problem
        Solver<TimeTable> solver = solverFactory.buildSolver();
        SolutionManager<TimeTable, HardSoftScore> solutionManager = SolutionManager.create(solverFactory);
        TimeTable solution = solver.solve(problem);

        // Visualize the solution
        printTimetable(solution, solutionManager);
    }

    public static TimeTable generateDemoData() {
        List<TimeSlot> timeslotList = new ArrayList<TimeSlot>();
        timeslotList.add(new TimeSlot(LocalTime.of(8, 30), LocalTime.of(9, 30)));
        timeslotList.add(new TimeSlot(LocalTime.of(9, 30), LocalTime.of(10, 30)));
        timeslotList.add(new TimeSlot(LocalTime.of(10, 30), LocalTime.of(11, 30)));
        timeslotList.add(new TimeSlot(LocalTime.of(11, 30), LocalTime.of(12, 30)));
        timeslotList.add(new TimeSlot(LocalTime.of(13, 30), LocalTime.of(14, 30)));
        timeslotList.add(new TimeSlot(LocalTime.of(14, 30), LocalTime.of(15, 30)));

        List<Facility> facilityList = new ArrayList<Facility>();
        facilityList.add(new Facility("Room A"));
        facilityList.add(new Facility("Room B"));
        facilityList.add(new Facility("Room C"));
        //facilityList.add(new Facility("Room D"));
        
        List<String> dayOfWeekList = new ArrayList<String>();
        dayOfWeekList.add("mon");
        dayOfWeekList.add("tue");
        dayOfWeekList.add("wed");
        dayOfWeekList.add("thu");
        dayOfWeekList.add("fri");
        
        Instructor aziz = new Instructor("Aziz");
        Course maths = new Course("Maths", aziz);
        Instructor widad = new Instructor("Widad");
        Course physics = new Course("Physics", widad);
        Instructor radi = new Instructor("Radi");
        Course phylosophy = new Course("Phylosophy", radi);
        
        List<Course> courses = new ArrayList<Course>();
        courses.add(maths);
        courses.add(physics);
        courses.add(phylosophy);
        
        List<WeeklySession> sessionList = new ArrayList<>();
        long id = 0;
        for (int i = 0 ; i < 7; i++)
        	for (Course c : courses)
        		sessionList.add(new WeeklySession(id++, c,"G1"));
        
        for (int i = 0 ; i < 7; i++)
        	for (Course c : courses)
        		sessionList.add(new WeeklySession(id++, c,"G2"));
        
        for (int i = 0 ; i < 7; i++)
        	for (Course c : courses)
        		sessionList.add(new WeeklySession(id++, c,"G3"));
        
        TimeTable timetable = new TimeTable(timeslotList, facilityList, sessionList, dayOfWeekList);
        
        System.out.println("Generated Data : " + courses.size() + " courses, " + timetable.getSessionList().size() + " sessions, " + dayOfWeekList.size()*timeslotList.size() +" timeslots in the week, "+timetable.getFacilityList().size()+" facilities" );
        
        return timetable;
    }

    private static void printTimetable(TimeTable solution, SolutionManager<TimeTable, HardSoftScore> solutionManager) {
        LOGGER.info("");
        List<Facility> facilityList = solution.getFacilityList();
        List<WeeklySession> sessionList = solution.getSessionList();
        
        System.out.println(sessionList);
        
        Map<String, List<WeeklySession>> sessionMap = sessionList.stream().collect(Collectors.groupingBy(WeeklySession::getStudentGroup));
        for(String group : sessionMap.keySet()) {
        	Map<String, List<WeeklySession>> groupSessionMap = sessionMap.get(group).stream().collect(Collectors.groupingBy(WeeklySession::getDayOfWeek));
        	
        	System.out.println("\n" + group + " timetable :\n---------------------");
        	for(String day : groupSessionMap.keySet()) {
        		System.out.print(day + " : ");
        		Map<TimeSlot, List<WeeklySession>> daySessionMap = groupSessionMap.get(day).stream().collect(Collectors.groupingBy(WeeklySession::getTimeSlot));
            	
        		for(TimeSlot ts : solution.getTimeslotList()) {
        			if (daySessionMap.get(ts)==null || daySessionMap.get(ts).size()==0)
        				System.out.print(" " + ts + " :                  ");
        			else
        				System.out.print(" " + ts + " : "+ daySessionMap.get(ts)+" ");
        		}
        		System.out.println();
        	}
        }
        
        ScoreExplanation<TimeTable, HardSoftScore> scoreExplanation = solutionManager.explain(solution);

        // Output the constraint violations
        scoreExplanation.getConstraintMatchTotalMap().forEach((constraintName, constraintMatchTotal) -> {
            System.out.println("Constraint: " + constraintName + " | Matches: " + constraintMatchTotal.getConstraintMatchSet().size()
                + " | Impact: " + constraintMatchTotal.getScore());
        });
        
        scoreExplanation.getConstraintMatchTotalMap().forEach((constraintName, constraintMatchTotal) -> {
            System.out.println("Constraint: " + constraintName + " | Impact: " + constraintMatchTotal.getScore());

            // Iterate through individual constraint matches
            constraintMatchTotal.getConstraintMatchSet().forEach(constraintMatch -> {
                System.out.println("  Justification: " + constraintMatch.getConstraintName() + " " + constraintMatch.getJustification() // Justification contains the entities involved
                    + " | Match weight: " + constraintMatch.getScore());
            });
        });

    }

}