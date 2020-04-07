import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import components.*;

public class Profiscendum extends JFrame implements KeyListener {

    private static final long serialVersionUID = 1L;  //version 1

    //MARK: - Objects

    private String[] alphabet = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
        "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };

    private JLayeredPane mainPanel;

    private Tower leftTower, rightTower;

    //MARK: - Constructor
 
    public Profiscendum() {
        //Window setup
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("mmmm destruction, yummy");
        setSize(840, 440);
        setResizable(false);

        //Layout setup
        JPanel cp = (JPanel)getContentPane();
        cp.setLayout(new BorderLayout());
        mainPanel = new JLayeredPane();
        cp.add("Center", mainPanel);

        //Input setup
        addKeyListener(this);
        shuffleArray(alphabet);

        // MARK: - Adding elements

        //background
        JComponent background = new JComponent() {
            private static final long serialVersionUID = 1L;

            //paint background color
            public void paintComponent(Graphics g) {
                g.setColor(new Color(192, 163, 148));
                g.fillRect(0, 0, 840, 420);
                g.setColor(new Color(79, 69, 64));
                g.fillRect(0, 380, 840, 40);
            }
        };
        background.setLocation(0, 0);
        background.setSize(getSize());
        mainPanel.add(background, Integer.valueOf(0));

        //towers
        leftTower = new Tower();
        rightTower = new Tower();
        leftTower.rightDoor.setDoor(true);
        leftTower.setLocation(50, 155);
        rightTower.setLocation(730, 155);
        leftTower.setSize(leftTower.getPreferredSize());
        rightTower.setSize(rightTower.getPreferredSize());
        mainPanel.add(leftTower, Integer.valueOf(10));
        mainPanel.add(rightTower, Integer.valueOf(10));

        //see!
        cp.validate();
        setVisible(true);
    }
 
    // The entry main() method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Profiscendum();
            }
        });
    }

    //MARK: - Implemented methods

    @Override
    public void keyPressed(KeyEvent e) {
        switch (KeyEvent.getKeyText(e.getKeyCode()).toLowerCase()) {
            case "␣": //space
                leftTower.rightDoor.setDoor(!leftTower.rightDoor.isOpen());
                mainPanel.repaint(leftTower.rightDoor.getX() + leftTower.getX(), leftTower.rightDoor.getY() + leftTower.getY(), Math.max(leftTower.rightDoor.maxStrength, 15), leftTower.rightDoor.getHeight());
                break;
            case "↑": //up
                break;
            case "↓": //down
                break;
            case "→": //left
                break;
            case "←": //right
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    //not used
    @Override public void keyTyped(KeyEvent e) {}

    //MARK: - Helper methods

    private void shuffleArray(String[] array) {
        Random rand = ThreadLocalRandom.current();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            // Simple swap
            String a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
    }
}