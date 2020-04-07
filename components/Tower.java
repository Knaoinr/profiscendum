package components;

import java.awt.*;
import javax.swing.*;

public class Tower extends JLayeredPane {
    private static final long serialVersionUID = 1L;

    public Door leftDoor, rightDoor;

    public Tower() {
        JComponent tower = new JComponent() {
            private static final long serialVersionUID = 1L;

            // paint tower
            public void paintComponent(Graphics g) {
                g.setColor(new Color(84, 81, 80));
                g.fillRect(0, 0, 15, 177);
                g.fillRect(45, 0, 15, 177);
                g.fillRect(15, 10, 30, 15);
                g.fillRect(15, 165, 30, 10);
                g.setColor(new Color(84, 81, 80, 150));
                g.fillRect(0, 177, 15, 48);
                g.fillRect(45, 177, 15, 48);
                g.setColor(new Color(84, 81, 80, 125));
                g.fillRect(15, 25, 30, 140);
                g.setColor(new Color(84, 81, 80, 100));
                g.fillRect(15, 175, 30, 50);
            }
        };
        tower.setLocation(0, 0);
        tower.setSize(getPreferredSize());
        add(tower, Integer.valueOf(0));

        leftDoor = new Door(10);
        rightDoor = new Door(10);
        leftDoor.setLocation(2, 177);
        rightDoor.setLocation(47, 177);
        leftDoor.setSize(leftDoor.maxStrength, 48);
        rightDoor.setSize(rightDoor.maxStrength, 48);
        add(leftDoor, Integer.valueOf(10));
        add(rightDoor, Integer.valueOf(10));
    }

    //size info
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(60, 225);
    }
}