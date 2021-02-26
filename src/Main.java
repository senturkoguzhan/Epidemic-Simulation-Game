/**
 * @author OGUZHAN SENTURK
 * Main Class game will start here
 */
public class Main {
    public static void main(String[] args) {
        View v  = new View();
        Controller c = new Controller(v);
        v.setController(c);
        c.initController();
    }
}
