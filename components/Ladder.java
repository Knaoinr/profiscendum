package components;

import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Color;

public class Ladder extends JComponent {
    private static final long serialVersionUID = 1L;

    public void paintComponent(Graphics g) {
        g.setColor(new Color(178, 138, 88));
        g.fillRect(0, 0, 3, getHeight());
        g.fillRect(getWidth() - 3, 0, 3, getHeight());
        for(int i = 1; i <= getHeight()/8; i++) {
            g.fillRect(3, getHeight() - 8*i, getWidth() - 6, 2);
        }
    }
}