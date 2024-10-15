package els;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

public class TimeTableConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                // Hard constraints
                facilityConflict(constraintFactory),
                teacherConflict(constraintFactory),
                studentGroupConflict(constraintFactory),
                // Soft constraints are only implemented in the optaplanner-quickstarts code
        };
    }

    private Constraint facilityConflict(ConstraintFactory constraintFactory) {
        // A room can accommodate at most one lesson at the same time.

        // Select a lesson ...
        return constraintFactory
                .forEach(WeeklySession.class)
                // ... and pair it with another lesson ...
                .join(WeeklySession.class,
                        // ... in the same weekday and timeslot ...
                        Joiners.equal(WeeklySession::getDayTimeSlot),
                        // ... in the same room ...
                        Joiners.equal(WeeklySession::getFacility),
                        // ... and the pair is unique (different id, no reverse pairs) ...
                        Joiners.lessThan(WeeklySession::getId))
                // ... then penalize each pair with a hard weight.
                .penalize(HardSoftScore.of(2,0))
                .asConstraint("Room conflict");
    }

    private Constraint teacherConflict(ConstraintFactory constraintFactory) {
        // A teacher can teach at most one lesson at the same time.
        return constraintFactory.forEach(WeeklySession.class)
                .join(WeeklySession.class,
                        Joiners.equal(WeeklySession::getDayTimeSlot),
                        Joiners.equal(WeeklySession::getInstructor),
                        Joiners.lessThan(WeeklySession::getId))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Teacher conflict");
    }

    private Constraint studentGroupConflict(ConstraintFactory constraintFactory) {
        // A student can attend at most one lesson at the same time.
        return constraintFactory.forEach(WeeklySession.class)
                .join(WeeklySession.class,
                        Joiners.equal(WeeklySession::getDayTimeSlot),
                        Joiners.equal(WeeklySession::getStudentGroup),
                        Joiners.lessThan(WeeklySession::getId))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Student group conflict");
    }

}