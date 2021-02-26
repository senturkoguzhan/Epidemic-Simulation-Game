import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
/**
 * @author OGUZHAN SENTURK
 * This class controls the data flow into model and updates view whenever data changes
 */
public class Controller implements Runnable {
    /** Person List inside the game*/
    private final ArrayList<Model> modelList;
    /** View object */
    private final View view;
    /** Maximum Spreading Factor */
    private static final double MAX_SF = 1.0;
    /** Minimum Spreading Factor */
    private static final double MIN_SF = 0.5;
    /** Maximum Mortality Rate */
    private static final double MAX_MR = 0.9;
    /** Minimum Spreading Factor */
    private static final double MIN_MR = 0.1;
    /** Population constant */
    private static final int PO_CONSTANT = 100;
    /** Spreading Factor */
    public static double spreadingFactor;
    /** Mortality Rate */
    public static double mortalityRate;
    /** flag of game */
    private static boolean running ;
    /** Game thread */
    private Thread gameThread;
    /** producer thread */
    private Thread producerThread;
    /** consumer thread */
    private Thread consumerThread;
    /** Mediator object for handle interaction between individuals */
    private final Mediator mediator;
    /** Hospital object */
    private Hospital hospital;
    /**
     * Constructor
     * @param view view object initialize instance variable
     */
    public Controller(View view){
        Random rand = new Random();
        this.view = view;
        spreadingFactor = MIN_SF + rand.nextDouble() * (MAX_SF - MIN_SF);
        mortalityRate = MIN_MR + rand.nextDouble() * (MAX_MR - MIN_MR);
        running = false;
        modelList = new ArrayList<>();
        mediator = new Mediator();
        hospital = new Hospital();
    }
    /**
     * Function that returns list is empty or not
     * @return returns true if list is not empty otherwise false
     */
    public boolean check(){
        return modelList.size() != 0;
    }
    /**
     * Function that returns the running
     * @return returns true if game is on otherwise false
     */
    public static boolean isRunning() {
        return running;
    }
    /**
     * Function to add human inside game
     * @param count how many human want to add
     * @param flag flag of infected or healthy
     */
    public void add_human(int count,boolean flag){
        for (int i = 0; i < count; i++){
            if(modelList.size() == 0){
                modelList.add( new Model(hospital));
                modelList.get(0).setSick();
            }
            else{
                try{
                    //PROTOTYPE
                    Model new_copy = (Model) modelList.get(0).clone();
                    if (!flag) {
                        new_copy.setHealthy();
                    } else {
                        new_copy.setSick();
                    }
                    modelList.add(new_copy);
                }
                catch (Exception ignored){
                }
            }
            hospital.setCapacity(modelList.size()/PO_CONSTANT);
        }
    }
    /**
     * Function to tell the view to draw Model
     * @param g graphics component
     */
    public void draw(Graphics g) {
        synchronized (modelList) {
            for (int i = 0; i < modelList.size(); i++) {
                view.drawModel(g,modelList.get(i));
            }
        }
    }
    /** Update stats inside game */
    public void update_stats(){
        int healthy = 0;
        int sick = 0;
        int hospitalized = 0;
        int dead = 0;
        synchronized (modelList){
            for (int i = 0; i < modelList.size(); i++) {
                switch (modelList.get(i).getState()) {
                    case HEALTHY -> healthy++;
                    case HOSPITALIZED -> hospitalized++;
                    case INFECTED -> sick++;
                    case DEAD -> dead++;
                }
            }
        }
        view.getTotalHealthy().setText("Total Healthy          : " + healthy);
        view.getTotalHospitalized().setText("Total Hospitalized : " + hospitalized);
        view.getTotalInfected().setText("Total Infected         : " + sick);
        view.getTotalDead().setText("Total Dead              : " + dead);
        view.getSpreadingFactor().setText("Spreading Factor   : " + spreadingFactor);
        view.getMortalityRate().setText("Mortality Rate         : " + mortalityRate);
    }
    /** Move individuals */
    public void update() {
        synchronized (modelList){
            for (int i = 0; i < modelList.size(); i++) {
                modelList.get(i).update();
                update_stats();
                for (int j = 0; j < modelList.size(); j++)
                    if (i != j) {
                        Model p1 = modelList.get(i);
                        Model p2 = modelList.get(j);
                        mediator.collided(p1,p2);
                    }
            }
        }
    }
    /** Initialize action listener */
    public void initController() {
        view.getStartButton().addActionListener(e -> start_button_action());
        view.getStopButton().addActionListener(e -> stop_button_action());
        view.getResetButton().addActionListener(e -> reset_button_action());
        view.getAddHuman().addActionListener(e -> add_human_action(false));
        view.getAddInfected().addActionListener(e -> add_human_action(true));
    }
    /**
     * Add human or infected button action
     * @param flag flag of healthy or infected
     */
    private void add_human_action(boolean flag) {
        int userInput = 0;
        String result = null;
        try{
            result = (String)JOptionPane.showInputDialog(
                    view.getFrame(),
                    "Enter individuals count want to add",
                    !flag ?"Add Human" :"Add Infected",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    1
            );
            if(result != null){
                userInput =  Integer.parseInt(result);
            }
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(view.getFrame(), "Please enter only integer!!!",
                    "ERROR",JOptionPane.ERROR_MESSAGE);
            userInput = 0;
        }
        if(userInput > 0 && running){
            add_human(userInput,flag);

        }
        else{
            if(!running && result != null)
                JOptionPane.showMessageDialog(view.getFrame(), "Please first start the game!!!",
                        "ERROR",JOptionPane.ERROR_MESSAGE);
        }
    }
    /** Reset button action */
    private void reset_button_action() {
        stop();
        view.canvasSetup();
        view.getFrame().dispose();
        view.newWindow();
        hospital = null;
        System.gc();
        view.initializeController();
    }
    /** Stop button action */
    private void stop_button_action() {
        running = false;
    }
    /** Start button action */
    private void start_button_action() {
        start();
    }
    @Override
    public void run() {
        view.requestFocus();
        double fps = 50.0;
        long start = System.nanoTime();
        double time = 0;
        while (running) {
            if(producerThread != null ){
                if( producerThread.getState() == Thread.State.TERMINATED){
                    System.gc();
                    producerThread = new Thread(hospital.getProducer());
                    producerThread.start();
                }
            }
            if(consumerThread != null ){
                if( consumerThread.getState() == Thread.State.TERMINATED){
                    System.gc();
                    consumerThread = new Thread(hospital.getConsumer());
                    consumerThread.start();
                }
            }
            long end = System.nanoTime();
            time += (end - start) / (1000000000 / fps);
            start = end;
            while (time >= 1 && running) {
                if(modelList.size() != 0)
                    update();
                view.render();
                time--;
            }
        }
    }
    /** Start the game */
    public synchronized void start() {
        gameThread = new Thread(this);
        gameThread.start();
        running = true;
        producerThread = new Thread(hospital.getProducer());
        producerThread.start();
        consumerThread = new Thread(hospital.getConsumer());
        consumerThread.start();
    }
    /** Stop the game */
    public void stop() {
        running = false;
        gameThread = null;
        producerThread = null;
        consumerThread = null;
    }
}
