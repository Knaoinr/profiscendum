package profiscendum.characters;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Container;
import java.awt.Rectangle;
import profiscendum.Profiscendum;
import profiscendum.components.*;

public class Character extends JComponent {
    private static final long serialVersionUID = 1L;

    public int dx;
    public int dy;
    public int x;
    public int y;
    public int lastX;
    public int lastY;
    public int width;
    public int height;
    public Direction verticalDirection = Direction.NONE;
    public Direction horizontalDirection = Direction.RIGHT;

    public enum Direction {
        UP(1), DOWN(2), LEFT(3), RIGHT(4), NONE(0);

        public final int value;
        private Direction(int value) {
            this.value = value;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }

    public void move(JLayeredPane mainPanel) {
        boolean isHorizontalBarrier = false;
        boolean isVerticalBarrier = false;

        Rectangle selfRect = Profiscendum.getRectangleRelativeTo(this, mainPanel);
        selfRect.x += 25; //corrections for incorrect getLocation()
        selfRect.y += 38;

        //check for all possible barriers
        for(int i = 0; i < mainPanel.getComponentCount() && (!isHorizontalBarrier || !isVerticalBarrier); i++) {
            if (selfRect.intersects(mainPanel.getComponent(i).getBounds()) && mainPanel.getComponent(i).getClass() == Tower.class) {
                Container comp = (Container) mainPanel.getComponent(i);
                for(int j = 0; j < comp.getComponentCount() && (!isHorizontalBarrier || !isVerticalBarrier); j++) {
                    //if intersects
                    Rectangle otherRect = Profiscendum.getRectangleRelativeTo((Container) comp.getComponent(j), mainPanel);
                    if (selfRect.intersects(otherRect)) {
                        //if door
                        if (comp.getComponent(j).getClass() == Door.class) {
                            //if closed
                            if (!((Door) comp.getComponent(j)).isOpen()) {
                                //TODO: if you're on top of it
                                //else if you're trying to enter it horizontally
                                if ((otherRect.x + comp.getComponent(j).getWidth()/2 > selfRect.x + width/2) == (horizontalDirection == Direction.RIGHT)) {
                                    isHorizontalBarrier = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!isHorizontalBarrier) {
            x += dx;
        }
        if (!isVerticalBarrier) {
            y += dy;
        }
    }

    /**
     * Override me!
     * @return the image for this character's direction
     */
    public Image getImage() {
        return null;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(getImage(), 0, 0, null);
    }
}