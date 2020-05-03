package profiscendum.components;

import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;

public class Flag extends JComponent {
    private static final long serialVersionUID = 1L;

    private Pattern pattern;

    public enum Pattern {
        PLAIN, PATTERNED;
    }

    public Flag(Pattern pattern) {
        this.pattern = pattern;
    }

    public void paintComponent(Graphics g) {
        g.setClip(new Rectangle(-20, 0, getWidth() + 20, getHeight()));
        g.setColor(new Color(178, 138, 88));
        g.fillRect(0, 0, getWidth(), getHeight());
        if (pattern == Pattern.PATTERNED) {
            g.setColor(Color.BLACK);
            int[] blackX = { 0, 0, -10 }; //(0, 0), (0, 4), (-10, 4)
            int[] blackY = { 0, 4, 4 };
            g.fillPolygon(new Polygon(blackX, blackY, 3));
            g.setColor(Color.GRAY);
            int[] greyX = { 0, -10, -20, 0 }; //(0, 4), (-10, 4), (-20, 8), (0, 8)
            int[] greyY = { 4, 4, 8, 8 };
            g.fillPolygon(new Polygon(greyX, greyY, 4));
            g.setColor(new Color(1f, 1f, 1f, 0.6f));
            int[] whiteX = { -20, 0, 0, -10 }; //(-20, 8), (0, 8), (0, 12), (-10, 12)
            int[] whiteY = { 8, 8, 12, 12 };
            g.fillPolygon(new Polygon(whiteX, whiteY, 4));
            g.setColor(new Color(130, 49, 128));
            int[] purpleX = { 0, -10, 0 }; //(0, 12), (-10, 12), (0, 16)
            int[] purpleY = { 12, 12, 16 };
            g.fillPolygon(new Polygon(purpleX, purpleY, 3));
        } else if (pattern == Pattern.PLAIN) {
            g.setColor(new Color(1f, 1f, 1f, 0.6f));
            int[] whiteX = { 0, 0, -20 }; //(0, 0), (0, 16), (-20, 8)
            int[] whiteY = { 0, 16, 8 };
            g.fillPolygon(new Polygon(whiteX, whiteY, 3));
        }
    }
}