package profiscendum.components;

import javax.swing.JLayeredPane;
import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import profiscendum.components.Flag.Pattern;

public class Tower extends JLayeredPane {
    private static final long serialVersionUID = 1L;

    public Door leftDoor, rightDoor;

    public Tower() {
        //background
        JComponent tower = new JComponent() {
            private static final long serialVersionUID = 1L;

            // paint tower
            public void paintComponent(Graphics g) {
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
        add(tower, Integer.valueOf(1));

        //fill rest of solid blocks
        Block[] blocks = Block.fillRect(0, 0, 15, 177);
        for(int i = 0; i < blocks.length; i++) {
            add(blocks[i], Integer.valueOf(2));
        }
        blocks = Block.fillRect(45, 0, 15, 177);
        for(int i = 0; i < blocks.length; i++) {
            add(blocks[i], Integer.valueOf(2));
        }
        blocks = Block.fillRect(15, 10, 30, 15);
        for(int i = 0; i < blocks.length; i++) {
            add(blocks[i], Integer.valueOf(2));
        }
        blocks = Block.fillRect(15, 165, 30, 10);
        for(int i = 0; i < blocks.length; i++) {
            add(blocks[i], Integer.valueOf(2));
        }

        //doors
        leftDoor = new Door(10);
        rightDoor = new Door(10);
        leftDoor.setLocation(2, 177);
        rightDoor.setLocation(47, 177);
        leftDoor.setSize(leftDoor.maxStrength, 48);
        rightDoor.setSize(rightDoor.maxStrength, 48);
        add(leftDoor, Integer.valueOf(10));
        add(rightDoor, Integer.valueOf(10));

        //ladder
        Ladder ladder = new Ladder();
        ladder.setBounds(35, 10, 10, 140);
        add(ladder, Integer.valueOf(10));

        //flags
        Flag leftFlag = new Flag(Pattern.PLAIN);
        Flag rightFlag = new Flag(Pattern.PLAIN);
        leftFlag.setBounds(6, -20, 4, 21);
        rightFlag.setBounds(51, -20, 4, 21);
        add(leftFlag, Integer.valueOf(0));
        add(rightFlag, Integer.valueOf(0));
    }

    //size info
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(60, 225);
    }
}