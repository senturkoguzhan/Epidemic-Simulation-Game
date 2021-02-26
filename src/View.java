import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
/**
 * @author OGUZHAN SENTURK
 * This class represents the visualization of the game and data that model contains
 */
public class View extends Canvas {
    /** JFrame Width */
    private static final int WIDTH = 1250;
    /** JFrame Height */
    private static final int HEIGHT = 640;
    /** Start button */
    private JButton startButton;
    /** Stop button */
    private JButton stopButton;
    /** Reset button */
    private JButton resetButton;
    /** Add human button */
    private JButton addHuman;
    /** Add Infected button */
    private JButton addInfected;
    /** Label for total health people*/
    private JLabel totalHealthy;
    /** Label for total infected people */
    private JLabel totalInfected;
    /** Label for total hospitalized people */
    private JLabel totalHospitalized;
    /** Label for total dead people */
    private JLabel totalDead;
    /** Label for mortality rate */
    private JLabel mortalityRate;
    /** Label for spreading factor */
    private JLabel spreadingFactor;
    /** JFrame */
    private JFrame frame;
    /** Controller object */
    private Controller controller;
    /** Constructor */
    public View() {
        canvasSetup();
        newWindow();
    }
    /**
     * Function that returns the startButton
     * @return returns startButton
     */
    public JButton getStartButton() {
        return startButton;
    }
    /**
     * Function that returns the stopButton
     * @return returns stopButton
     */
    public JButton getStopButton() {
        return stopButton;
    }
    /**
     * Function that returns the resetButton
     * @return returns resetButton
     */
    public JButton getResetButton() {
        return resetButton;
    }
    /**
     * Function that returns addHuman button
     * @return returns addHuman
     */
    public JButton getAddHuman() {
        return addHuman;
    }
    /**
     * Function that returns the addInfected button
     * @return returns addInfected
     */
    public JButton getAddInfected() {
        return addInfected;
    }
    /**
     * Function that returns the totalHealthy Label
     * @return returns totalHealthy
     */
    public JLabel getTotalHealthy() {
        return totalHealthy;
    }
    /**
     * Function that returns the totalInfected Label
     * @return returns totalInfected
     */
    public JLabel getTotalInfected() {
        return totalInfected;
    }
    /**
     * Function that returns the totalHospitalized Label
     * @return returns totalHospitalized
     */
    public JLabel getTotalHospitalized() {
        return totalHospitalized;
    }
    /**
     * Function that returns the totalDead Label
     * @return returns totalDead
     */
    public JLabel getTotalDead() {
        return totalDead;
    }
    /**
     * Function that returns the mortalityRate Label
     * @return returns mortalityRate
     */
    public JLabel getMortalityRate() {
        return mortalityRate;
    }
    /**
     * Function that returns the spreadingFactor Label
     * @return returns spreadingFactor
     */
    public JLabel getSpreadingFactor() {
        return spreadingFactor;
    }
    /**
     * Function that returns the JFrame
     * @return returns frame
     */
    public JFrame getFrame() {
        return frame;
    }
    /**
     * Function to change the controller
     * @param controller is new controller
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }
    /** Create Window */
    public void newWindow() {

        startButton = new JButton("Start");
        startButton.setName("Start");
        startButton.setBounds(1075,20,80,30);

        stopButton = new JButton("Stop");
        stopButton.setName("Stop");
        stopButton.setBounds(1075,70,80,30);

        resetButton = new JButton("Reset");
        resetButton.setName("Reset");
        resetButton.setBounds(1075,120,80,30);

        addHuman = new JButton("Add Human");
        addHuman.setName("Add Human");
        addHuman.setBounds(1055,170,120,30);

        addInfected = new JButton("Add Infected");
        addInfected.setName("Add Infected");
        addInfected.setBounds(1050,220,130,30);

        spreadingFactor = new JLabel();
        spreadingFactor.setText("Spreading Factor   : ");
        spreadingFactor.setBounds(1020,300,200,30);

        mortalityRate = new JLabel();
        mortalityRate.setText("Mortality Rate         : ");
        mortalityRate.setBounds(1020,340,200,30);

        totalInfected = new JLabel();
        totalInfected.setText("Total Infected         : ");
        totalInfected.setBounds(1020,380,200,30);

        totalHealthy = new JLabel();
        totalHealthy.setText("Total Healthy          : ");
        totalHealthy.setBounds(1020,420,200,30);

        totalHospitalized = new JLabel();
        totalHospitalized.setText("Total Hospitalized : ");
        totalHospitalized.setBounds(1020,460,200,30);

        totalDead = new JLabel();
        totalDead.setText("Total Dead              : ");
        totalDead.setBounds(1020,500,200,30);

        frame = new JFrame("Epidemic Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setSize(1250,640);
        frame.setVisible(true);
        frame.add(startButton);
        frame.add(stopButton);
        frame.add(totalInfected);
        frame.add(totalHealthy);
        frame.add(totalHospitalized);
        frame.add(totalDead);
        frame.add(addHuman);
        frame.add(addInfected);
        frame.add(resetButton);
        frame.add(mortalityRate);
        frame.add(spreadingFactor);
        frame.setResizable(false);
        frame.add(this);
    }
    /** Initialize action listener */
    public void initializeController() {
        controller = new Controller( this);
        controller.initController();
    }
    /** Setup canvas */
    public void canvasSetup() {
        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);
    }
    /** Render function */
    public void render() {
        BufferStrategy buffer = this.getBufferStrategy();
        if (buffer == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics graphics = buffer.getDrawGraphics();
        drawBackground(graphics);
        drawWalls(graphics);
        if(controller.check())
            controller.draw(graphics);
        graphics.dispose();
        buffer.show();
    }
    /**
     * Function to draw Walls
     * @param graphics graphics component
     */
    private void drawWalls(Graphics graphics) {
        graphics.setColor(Color.yellow);
        Graphics2D g2d = (Graphics2D) graphics; // a more complex Graphics class used to draw Objects
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRect(0,0,1000,600);
    }
    /**
     * Function to draw Background
     * @param graphics graphics component
     */
    private void drawBackground(Graphics graphics) {
        graphics.setColor(Color.darkGray);
        graphics.fillRect(0, 0, 1000, 600);
    }
    /**
     * Function to draw Model
     * @param graphics graphics component
     * @param model Model want to draw
     */
    public void drawModel(Graphics graphics, Model model) {
        Color color;
        if (model.getState() == State.HEALTHY) {
            color = Color.GREEN;
            graphics.setColor(color);
            if (model.getxCord() > 0 && model.getxCord() + model.getSize() < 1000 &&
                    model.getyCord() > 0 && model.getyCord() + model.getSize() < 600)
                graphics.fillRect(model.getxCord(), model.getyCord(), model.getSize(), model.getSize());
        }
        else if(model.getState() == State.INFECTED) {
            color = Color.RED;
            graphics.setColor(color);
            if (model.getxCord() > 0 && model.getxCord() + model.getSize() < 1000 &&
                    model.getyCord() > 0 && model.getyCord() + model.getSize() < 600)
                graphics.fillRect(model.getxCord(), model.getyCord(), model.getSize(), model.getSize());
        }
    }
}
