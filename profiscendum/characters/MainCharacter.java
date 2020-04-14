package profiscendum.characters;

import java.awt.Image;
import javax.swing.ImageIcon;

public class MainCharacter extends Character {
    private static final long serialVersionUID = 1L;

    public MainCharacter() {
        Image image = getImage();
        width = image.getWidth(null);
        height = image.getHeight(null);

        x = 400;
        y = 380 - height;

        lastX = x;
        lastY = y;
    } 

    @Override
    public Image getImage() {
        ImageIcon ii = new ImageIcon("profiscendum/images/maincharacter/mc" + horizontalDirection.value + verticalDirection.value + ".png");
        return ii.getImage();
    }
}