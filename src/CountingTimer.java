import java.util.Timer;
import java.util.TimerTask;
/**
 * @author OGUZHAN SENTURK
 * This class used for counting recoveryTime,deadTime,dischargetTime every person
 */
public class CountingTimer extends TimerTask {
    /** start time for timer*/
    private int start;
    /** end time for timer*/
    private final double deadline;
    /** timer object */
    private final Timer timer;
    /** Person */
    Model model;
    /**
     * Constructor
     * @param deadline end time for timer
     * @param timer timer object
     * @param model the person
     */
    public CountingTimer(double deadline, Timer timer, Model model) {
        start = 0;
        this.deadline = deadline;
        this.timer = timer;
        this.model = model;
    }
    /** Run method */
    @Override
    public void run() {
        if(Controller.isRunning()){
            if(start >= deadline){
                if(model.getState() == State.INFECTED && deadline == model.getDeadTime() ){
                    model.setDead();
                }
                else if(model.getState() == State.HOSPITALIZED && deadline == model.getDischargeTime()){
                    model.setHealthy();
                    model.getDeadTimeTimer().cancel();
                }
                else if (model.getState() == State.INFECTED && deadline == model.getRecoveryTime() ){
                    model.getHospital().addPatient(model);
                    //setHospitalized();
                }
                start = 0;
                timer.cancel();
            }
            start++;
        }
    }
}
