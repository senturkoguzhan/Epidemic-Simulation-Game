import java.util.concurrent.LinkedBlockingQueue;
/**
 * @author OGUZHAN SENTURK
 * This class uses producer/consumer paradigm
 * Two thread created from controller one for consumer one for producer
 */
public class Hospital {
    /**
     * Real queue used inside synchronized block
     */
    private final LinkedBlockingQueue<Model> hospitalQueue;
    /**
     * If infected recoveryTime is finished added this queue
     */
    private final LinkedBlockingQueue<Model> waitingQueue;
    /**
     * Real queue capacity change dynamically from controller
     */
    private int capacity;
    /**
     * used for lock
     */
    private final Object lock = new Object();
    /**
     * Runnable producer object
     */
    private final Producer producer;
    /**
     * Runnable consumer object
     */
    private final Consumer consumer;
    /**
     * Constructor
     */
    public Hospital() {
        hospitalQueue = new LinkedBlockingQueue<>();
        waitingQueue = new LinkedBlockingQueue<>();
        producer = new Producer();
        consumer = new Consumer();
        capacity = 0;
    }
    /**
     * Function to add patient
     * @param model human want to add queue
     */
    public void addPatient(Model model) {
        waitingQueue.add(model);
    }
    /**
     * Function to set capacity
     * @param capacity new capacity
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    /**
     * Function to return producer object
     * @return returns producer
     */
    public Producer getProducer() {
        return producer;
    }
    /**
     * Function to return consumer object
     * @return returns consumer
     */
    public Consumer getConsumer() {
        return consumer;
    }
    /**
     * Producer class implements Runnable interface
     * Producer check hospital’s capacity is not full and the consumer is not working
     * and if there are people waiting in line it takes them.
     */
    public class Producer implements Runnable {
        @Override
        public void run() {
            while (Controller.isRunning()) {
                //synchronized (lock) {
                if (capacity > 0 && waitingQueue.size() > 0 && hospitalQueue.size() < capacity) {
                    for (Model m: waitingQueue) {
                        if (m.getState() == State.INFECTED) {
                            hospitalQueue.add(waitingQueue.poll());
                            break;
                        }
                    }
                }
                // }
                try {
                    waitingQueue.removeIf(p -> p.getState() == State.HOSPITALIZED || p.getState() == State.DEAD || p.getState() == State.HEALTHY);
                } catch (Exception ignored) {
                }
            }
        }
    }
    /**
     * Consumer class implements Runnable interface
     * Consumer checks if there are people in second Queue and producer is not working, consumes Queue.
     */
    public class Consumer implements Runnable {
        @Override
        public void run() {
            while (Controller.isRunning()) {
                //synchronized (lock) {
                if (capacity > 0 && hospitalQueue.size() > 0) {
                    for (Model m: hospitalQueue) {
                        if (m.getState() == State.INFECTED) {
                            m.setHospitalized();
                        } else {
                            break;
                        }
                    }
                }
                //  }
                try {
                    hospitalQueue.removeIf(p -> p.getState() == State.HEALTHY || p.getState() == State.DEAD);
                } catch (Exception ignored) {
                }
            }
        }
    }
}


