package profiscendum.components;

import javax.swing.JLayeredPane;
import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Color;
import profiscendum.components.Flag.Pattern;

public class Building extends JLayeredPane {
    private static final long serialVersionUID = 1L;

    public final int floorHeight;

    public Building(int width, int height, int numOfFloors) {
        floorHeight = (height-3)/numOfFloors; //includes 12px for floor

        //background
        JComponent building = new JComponent() {
            private static final long serialVersionUID = 1L;

            //paint building
            public void paintComponent(Graphics g) {
                g.setColor(new Color(84, 81, 80, 150)); //door thresholds
                g.fillRect(0, height - 35, 15, 35);
                g.fillRect(width - 15, height - 35, 15, 35);
                g.setColor(new Color(84, 81, 80, 125)); //other floors
                g.fillRect(15, 15, width - 30, height - 15 - floorHeight);
                g.setColor(new Color(84, 81, 80, 100)); //1st floor
                g.fillRect(15, height - floorHeight, width - 30, floorHeight);
            }
        };
        building.setBounds(0, 0, width, height);
        add(building, Integer.valueOf(1));

        //fill solid blocks
        fillRectWithBlocks(0, 0, 15, height - 35);
        fillRectWithBlocks(width - 15, 0, 15, height - 35);
        fillRectWithBlocks(15, 0, width - 30, 15);
        for(int i = 1; i < numOfFloors; i++) {
            fillRectWithBlocks(15, i*floorHeight + 3, width - 30, 12);
        }

        //add outside doors
        Door leftDoor = new Door(4);
        Door rightDoor = new Door(4);
        leftDoor.setBounds(5, height - 35, leftDoor.maxStrength, 35);
        rightDoor.setBounds(width - 9, height - 35, rightDoor.maxStrength, 35);
        add(leftDoor, Integer.valueOf(10));
        add(rightDoor, Integer.valueOf(10));

        //and flags at top
        Flag leftFlag = new Flag(Pattern.PLAIN);
        Flag rightFlag = new Flag(Pattern.PLAIN);
        leftFlag.setBounds(6, -20, 4, 21);
        rightFlag.setBounds(width - 9, -20, 4, 21);
        add(leftFlag, Integer.valueOf(0));
        add(rightFlag, Integer.valueOf(0));
    }

    public void fillRectWithBlocks(int x, int y, int width, int height) {
        Block[] blocks = Block.fillRect(x, y, width, height);
        for(int i = 0; i < blocks.length; i++) {
            add(blocks[i], Integer.valueOf(2));
        }
    }
}