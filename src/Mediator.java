import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
/**
 * @author OGUZHAN SENTURK
 * Mediator class model and implement the interaction between individuals
 * By retrieving the user's coordinates and status information,
 * it calculates the collision status with other users,
 * the stopping of two users if two users collided,
 * the infected status, and changes the status of the users accordingly.
 */
public class Mediator {
    public static double max_duration;
    /**
     * Collision Function
     * Controller call this function every pair of person
     * Controller has no information, is there any collision situation,
     * Controller just calls the black box and the mediator takes the necessary actions itself
     * @param p1 first person
     * @param p2 second person
     */
    public void collided(Model p1, Model p2) {

        Rectangle nextMe = new Rectangle((int) Math.ceil(p1.getNextX()), (int) Math.ceil(p1.getNextY()), p1.getSize(), p1.getSize());

        boolean collided = nextMe.intersectsLine(p2.getxCord(), p2.getyCord(), p2.getNextX(), p2.getNextY());

        if(collided && !p1.isBusy() && !p2.isBusy() && p1.getState() != State.DEAD && p1.getState() != State.HOSPITALIZED &&
                p2.getState() != State.DEAD && p2.getState() != State.HOSPITALIZED){

            p1.setBusy(true);
            p2.setBusy(true);
            Timer timer = new Timer();
            TimerTask task = new CounterTimerTask(p1, p2, timer);
            timer.schedule(task,0,1000);

            max_duration = Math.max(p1.getDurationConstant(),p2.getDurationConstant());
            double min_social_distance = Math.min(p1.getSocialDistance(),p2.getSocialDistance());
            double possibility = Math.min((Controller.spreadingFactor * (1 + max_duration/10)* p1.getMask()
                    * p2.getMask() *(1 - min_social_distance / 10)),1);

            if(Math.random() < possibility){
                if (p2.getState() == State.INFECTED && p1.getState() == State.HEALTHY)
                    p1.setSick();
                else if(p1.getState() == State.INFECTED && p2.getState() == State.HEALTHY)
                    p2.setSick();
            }
        }
    }
    /**
     * Inner class for help mediator,
     * If collision occurs, timer count how long they will spend together
     */
    private static class CounterTimerTask extends TimerTask {
        /** star time for timer */
        int count;
        /** timer object */
        Timer timer;
        /** Person1 inside collision*/
        Model person1;
        /** Person2 inside collision*/
        Model person2;
        /** Person1 x Coordinate speed*/
        int person1_xVel;
        /** Person1 y Coordinate speed*/
        int person1_yVel;
        /** Person2 x Coordinate speed*/
        int person2_xVel;
        /** Person2 y Coordinate speed*/
        int person2_yVel;
        /**
         * Constructor
         * @param person1 person1
         * @param person2 person2
         * @param timer timer object
         */
        public CounterTimerTask(Model person1, Model person2, Timer timer) {
            this.person1 = person1;
            this.person2 = person2;
            person1_xVel= person1.getxVel();
            person1_yVel= person1.getyVel();
            person2_xVel = person2.getxVel();
            person2_yVel = person2.getyVel();
            count = 0;
            this.timer = timer;
        }
        @Override
        public void run() {
            if(Controller.isRunning()){
                person1.setxVel(0);
                person1.setyVel(0);
                person2.setxVel(0);
                person2.setyVel(0);
                if(count >= max_duration){
                    person1.setxVel(person2_xVel);
                    person1.setyVel(person2_yVel);
                    person2.setxVel(person1_xVel);
                    person2.setyVel(person1_yVel);
                    count = 0;
                    person1.setBusy(false);
                    person2.setBusy(false);
                    timer.cancel();
                }
                count++;
            }
        }
    }

}
