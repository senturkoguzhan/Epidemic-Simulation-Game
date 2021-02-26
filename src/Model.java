import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
/**
 * @author OGUZHAN SENTURK
 * This class holds data and represent data layer
 * Implements the Cloneable interface for the prototype design pattern
 */
public class Model implements Cloneable{
    /** Size of Person **/
    private final int size;
    /** x coordinate of the person **/
    private int xCord;
    /** Y coordinate of the person **/
    private int yCord;
    /** Person's speed in X coordinate **/
    private int xVel;
    /** Person's speed in Y coordinate **/
    private int yVel;
    /** Time until hospitalization */
    private final double recoveryTime;
    /** Time until discharge in hospital */
    private final double dischargeTime ;
    /** Time until the person died */
    private final double deadTime ;
    /** Person's state */
    private State state;
    /** Flag to understand that you are colliding with a person */
    private boolean busy;
    /** Timer for recoveryTime */
    private Timer recoveryTimeTimer;
    /** Timer for dischargeTime */
    private Timer dischargeTimeTimer;
    /** Timer for deadTime */
    private Timer deadTimeTimer;
    /** Hospital object */
    private final Hospital hospital;
    /** Border Width for wall collisions*/
    private final int borderWidth;
    /** Border Height for wall collisions*/
    private final int borderHeight;
    /** Mask constant*/
    private double mask;
    /** Social Distance constant*/
    private int socialDistance;
    /** Social Wait constant*/
    private int durationConstant;
    /** Speed constant.It is a number between 1-10. Because I render 50 frames per second. */
    private final int speed = 10;
    /**
     * Constructor
     * @param hospital Hospital object initialize instance variable in class
     */
    public Model(Hospital hospital) {
        Random rand = new Random();
        size = 5;
        borderWidth = 1000;
        borderHeight = 600;
        xCord = rand.nextInt(borderWidth - size);
        yCord = rand.nextInt(borderHeight - size);
        xVel = rand.nextInt(speed) + 1;
        yVel = rand.nextInt(speed) + 1;
        if(rand.nextInt(2) == 0){
            xVel = -xVel;
            yVel = -yVel;
            mask = 0.2;
        }
        else
            mask = 1.0;
        socialDistance = rand.nextInt(10);
        durationConstant = rand.nextInt(5) +1;
        state = State.HEALTHY;
        busy = false;
        recoveryTime = 25; // 25
        dischargeTime = 10; //10
        deadTime = 100 *(1-Controller.mortalityRate);
        this.hospital = hospital;
    }
    /**
     * Function to make people infected
     * When a person is sick, two timers are start(deadTimeTimer and recoveryTimeTimer)
     */
    public void setSick() {

        state = State.INFECTED;
        recoveryTimeTimer = new Timer("recoveryTimeTimer");
        TimerTask task1 = new CountingTimer(recoveryTime,recoveryTimeTimer,this);

        deadTimeTimer = new Timer("deadTimeTimer");
        TimerTask task3 = new CountingTimer(deadTime,deadTimeTimer,this);

        deadTimeTimer.schedule(task3,0,1000);
        recoveryTimeTimer.schedule(task1,0,1000);
    }
    /**
     * Function to hospitalization
     * When a person is hospitalized, dischargeTimeTimer timer is start
     */
    public void setHospitalized() {
        state = State.HOSPITALIZED;
        dischargeTimeTimer = new Timer("dischargeTimeTimer");
        TimerTask task2 = new CountingTimer(dischargeTime,dischargeTimeTimer,this);
        dischargeTimeTimer.schedule(task2,0,1000);
    }
    /**
     * Function that returns the mask constant
     * @return returns mask constant
     */
    public double getMask() {
        return mask;
    }
    /**
     * Function that returns the  socialDistance constant
     * @return returns socialDistance constant
     */
    public int getSocialDistance() {
        return socialDistance;
    }
    /**
     * Function that returns durationConstant
     * @return returns durationConstant
     */
    public int getDurationConstant() {
        return durationConstant;
    }
    /** Function to make healthy */
    public void setHealthy() {

        state = State.HEALTHY;
    }
    /** Function to make dead */
    public void setDead() {
        state = State.DEAD;
    }
    /**
     * Function that returns the x coordinate
     * @return returns X Coordinate
     */
    public int getxVel() {
        return xVel;
    }
    /**
     * Function to change the x coordinate
     * @param xVel is new x coordinate
     */
    public void setxVel(int xVel) {
        this.xVel = xVel;
    }
    /**
     * Function that returns the Y coordinate
     * @return returns Y Coordinate
     */
    public int getyVel() {
        return yVel;
    }
    /**
     * Function to change the Y coordinate
     * @param yVel is new Y coordinate
     */
    public void setyVel(int yVel) {
        this.yVel = yVel;
    }
    /**
     * Function that returns the next X coordinate
     * @return returns next x coordinate
     */
    public float getNextX() {
        return xCord + xVel;
    }
    /**
     * Function that returns the next Y coordinate
     * @return returns next Y coordinate
     */
    public float getNextY() {
        return yCord + yVel;
    }
    /** Function to move people */
    public void update() {
        xCord += xVel;
        yCord += yVel;
        if (xCord + size > borderWidth || xCord < 0)
            xVel = -xVel;
        if (yCord + size > borderHeight || yCord < 0)
            yVel = -yVel;
    }
    /**
     * Function that returns the size
     * @return returns size of Person
     */
    public int getSize() {
        return size;
    }
    /**
     * Function that returns the X coordinate
     * @return returns X coordinate
     */
    public int getxCord() {
        return xCord;
    }
    /**
     * Function that returns the Y coordinate
     * @return returns Y coordinate
     */
    public int getyCord() {
        return yCord;
    }
    /**
     * Function that returns the recoveryTime
     * @return returns recoveryTime
     */
    public double getRecoveryTime() {
        return recoveryTime;
    }
    /**
     * Function that returns the dischargeTime
     * @return returns dischargeTime
     */
    public double getDischargeTime() {
        return dischargeTime;
    }
    /**
     * Function that returns the deadTime
     * @return returns deadTime
     */
    public double getDeadTime() {
        return deadTime;
    }
    /**
     * Function that returns the current state
     * @return returns current state
     */
    public State getState() {
        return state;
    }
    /**
     * Function that returns the flag of busy
     * @return returns flag of busy
     */
    public boolean isBusy() {
        return busy;
    }
    /**
     * Function to change flag of busy
     * @param busy flag of busy set this parameter
     */
    public void setBusy(boolean busy) {
        this.busy = busy;
    }
    /**
     * Function that returns the deadTimeTimer
     * @return returns deadTimeTimer
     */
    public Timer getDeadTimeTimer() {
        return deadTimeTimer;
    }
    /**
     * Function that returns the hospital object
     * @return returns hospital object
     */
    public Hospital getHospital() {
        return hospital;
    }
    /***
     * Since people can be dynamically added in the program and t
     * he production of the Model class is costly, one object is created,
     * it is copied and returned after the necessary actions are taken.
     * @return new copy of the Model class
     * @throws CloneNotSupportedException If clone method does not implement
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        Model new_copy = (Model) super.clone();
        Random rand = new Random();
        new_copy.recoveryTimeTimer = null;
        new_copy.dischargeTimeTimer = null;
        new_copy.deadTimeTimer = null;
        new_copy.busy = false;
        new_copy.xCord = rand.nextInt(1000 - new_copy.size);
        new_copy.yCord = rand.nextInt(600 - new_copy.size);
        new_copy.xVel = rand.nextInt(speed) + 1;
        new_copy.yVel = rand.nextInt(speed) + 1;
        if(rand.nextInt(2) == 0){
            xVel = -xVel;
            yVel = -yVel;
            mask = 0.2;
        }
        else
            mask = 1.0;
        socialDistance = rand.nextInt(10);
        durationConstant = rand.nextInt(5) +1;
        return new_copy;
    }
}
