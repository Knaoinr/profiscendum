package profiscendum;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import profiscendum.characters.MainCharacter;
import profiscendum.components.*;

/**
 * An overly simplistic scheduler that is paused when the game loop is paused.
 */
public class Scheduler {

    private List<JComponent> objects;
    private List<Double> times;
    private List<String> operations;

    public Scheduler() {
        objects = new ArrayList<JComponent>();
        times = new ArrayList<Double>();
        operations = new ArrayList<String>();
    }

    /**
     * Move the time ahead a second, updating everything that had 1 or less seconds left.
     */
    public void update() {
        int[] indexes = new int[times.size()+1];
        //execute
        for(int i = 0; i < times.size(); i++) {
            //lessen time
            times.set(i, Double.valueOf(times.get(i).doubleValue() - 1.0/30));

            //check if time
            if (times.get(i).doubleValue() <= 0) {
                switch (operations.get(i)) {
                    case "open":
                        ((Door) objects.get(i)).setDoor(true);
                        ((Door) objects.get(i)).waitingOnUpdate = false;
                        break;
                    case "close":
                        ((Door) objects.get(i)).setDoor(false);
                        ((Door) objects.get(i)).waitingOnUpdate = false;
                        break;
                    case "unparalyze":
                        ((MainCharacter) objects.get(i)).paralyzed = false;
                }
                indexes[0]++;
                indexes[indexes[0]] = i;
            }
        }
        //remove
        for(int i = 1; i <= indexes[0]; i++) {
            times.remove(indexes[i]);
            objects.remove(indexes[i]);
            operations.remove(indexes[i]);
        }
    }

    /**
     * Adds something to be scheduled.
     * @param object The object to be operated on.
     * @param seconds The number of seconds in which to do this.
     * @param operation The operation to do. (This string should have a reference in {@link Scheduler#update()}.)
     */
    public void add(JComponent object, double seconds, String operation) {
        objects.add(object);
        times.add(Double.valueOf(seconds));
        operations.add(operation);
    }

    /**
     * Cancels the first operation of this type scheduled to occur for this object.
     * @param object The object that was to be operated on.
     * @param operation The type of operation to cancel.
     */
    public void cancel(JComponent object, String operation) {
        for (int i = 0; i < objects.size(); i++) {
            if (object.equals(objects.get(i)) && operation.equals(operations.get(i))) {
                objects.remove(i);
                operations.remove(i);
                times.remove(i);
                return;
            }
        }
    }

}