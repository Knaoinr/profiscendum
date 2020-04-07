package components;

import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;

public class Door extends JComponent {
    private static final long serialVersionUID = 1L;

    private boolean isOpen = false;

    private int strength = 0;
    public final int maxStrength;

    public Door(int maxStrength) {
        this.maxStrength = maxStrength;
        strength = maxStrength;
    }

    //size info
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(maxStrength, getHeight());
    }

    //change door condition
    /**
     * Changes door to open or closed position.
     * @param toOpen True if setting to open, false if to closed.
     * @return Whether the state of the door was changed.
     */
    public boolean setDoor(boolean toOpen) {
        boolean didChange = (isOpen != toOpen);
        isOpen = toOpen;
        if (didChange) {
            repaint();
        }
        return didChange;
    }

    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Sets the strength of the door to a value [0-maxStrength]. This is also the width of the door.
     * @return Whether you need to immediately remove the door.
     */
    public boolean setStrength(int value) {
        boolean didChange = (strength != value);
        if (value > maxStrength) {
            strength = maxStrength;
        } else if (value < 0) {
            strength = 0;
        } else {
            strength = value;
        }
        if (didChange && !isOpen) {
            repaint();
        }
        return strength <= 0;
    }

    public int getStrength() {
        return strength;
    }

    //paint door according to strength
    public void paintComponent(Graphics g) {
        int height = getHeight();
        if (isOpen && strength > 0) {
            int width = Math.max(15, strength);
            g.setClip(new Rectangle(width, height));
            g.setColor(new Color(178, 138, 88));
            g.fillRect((maxStrength-strength)/2, height/2, width, (height+1)/2);
            g.fillOval((maxStrength-strength)/2, 0, width, height);
            g.fillOval((maxStrength-strength)/2 - 1, 0, width, height);
            g.setColor(new Color(42, 32, 27));
            g.fillOval(width-6, height/2, 3, 3);
            g.setColor(new Color(55, 104, 41));
            g.fillRect((maxStrength-strength)/2 - 1, height/4 - 1, 4, 2);
            g.fillRect((maxStrength-strength)/2 - 1, height/2 - 1, 4, 2);
            g.fillRect((maxStrength-strength)/2 - 1, height*3/4 - 1, 4, 2);
        } else {
            g.setColor(new Color(178, 138, 88));
            g.fillRect((maxStrength-strength)/2, 0, strength, height);
            g.setColor(new Color(55, 104, 41));
            g.fillRect((maxStrength-strength)/2, height/4 - 1, strength, 3);
            g.fillRect((maxStrength-strength)/2, height/2 - 1, strength, 3);
            g.fillRect((maxStrength-strength)/2, height*3/4 - 1, strength, 3);
        }
    }
}