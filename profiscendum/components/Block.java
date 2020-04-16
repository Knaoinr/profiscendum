package profiscendum.components;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Block extends JComponent {
    private static final long serialVersionUID = 1L;

    public void paintComponent(Graphics g) {
        g.setColor(new Color(84, 81, 80));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Fills up a rectangle with blocks of a maximum size of 10x10 (end blocks may be up to 19x19).
     * @param x The x coordinate of the rectangle.
     * @param y The y coordinate of the rectangle.
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     * @return An array containing all the blocks to add to your component.
     */
    public static Block[] fillRect(int x, int y, int width, int height) {
        int arrayWidth = (width + 9)/10;
        arrayWidth = width/(width/arrayWidth);
        int arrayHeight = (height + 9)/10;
        arrayHeight = height/(height/arrayHeight);
        Block[] array = new Block[arrayWidth * arrayHeight];
        int blockWidth, blockHeight;
        //for each block
        for(int i = 0; i < arrayWidth; i++) {
            for(int j = 0; j < arrayHeight; j++) {
                blockWidth = width/arrayWidth;
                blockHeight = height/arrayHeight;
                //if last in row/column
                if (i == arrayWidth - 1) {
                    blockWidth += width % arrayWidth;
                }
                if (j == arrayHeight - 1) {
                    blockHeight += height % arrayHeight;
                }
                array[arrayHeight*i + j] = new Block();
                array[arrayHeight*i + j].setBounds(i*(width/arrayWidth) + x, j*(height/arrayHeight) + y, blockWidth, blockHeight);
            }
        }
        return array;
    }

    /**
     * Fills up a rectangle with blocks of a maximum size of 10x10 (end blocks may be up to 19x19).
     * @param rect The rectangle you want to fill.
     * @return An array containing all the blocks to add to your component.
     */
    public static Block[] fillRect(Rectangle rect) {
        return fillRect(rect.x, rect.y, rect.width, rect.height);
    }
}