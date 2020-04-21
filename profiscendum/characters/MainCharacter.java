package profiscendum.characters;

import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
import profiscendum.Profiscendum;
import profiscendum.components.*;
import java.awt.Container;

public class MainCharacter extends Character {
    private static final long serialVersionUID = 1L;

    public ImageChoice imageChoice = ImageChoice.RIGHT;
    private ImageChoice lastDirection = ImageChoice.RIGHT;

    public MainCharacter() {
        Image image = getImage();
        width = image.getWidth(null);
        height = image.getHeight(null);

        x = 400;
        y = 380 - height;

        lastX = x;
        lastY = y;
    }

    public enum ImageChoice {
        RIGHT("RIGHT"), LEFT("LEFT"), LADDER("LADDER"), CROUCHLEFT("CROUCH-LEFT"), CROUCHRIGHT("CROUCH-RIGHT");

        public final String value;
        private ImageChoice(String value) {
            this.value = value;
        }
    }

    @Override
    public Image getImage() {
        ImageIcon ii = new ImageIcon("profiscendum/images/maincharacter/mc" + imageChoice.value + ".png");
        return ii.getImage();
    }

    @Override
    public void move(JLayeredPane mainPanel) {
        super.move(mainPanel);

        //choose image

        //if ladder intersects
        Rectangle selfRect = Profiscendum.getRectangleRelativeTo(this, mainPanel);
        selfRect.x = x; //corrections for incorrect getLocation()
        selfRect.y = y;
        for(int i = 0; i < mainPanel.getComponentCount(); i++) {
            if (selfRect.intersects(mainPanel.getComponent(i).getBounds()) && (mainPanel.getComponent(i).getClass() == Tower.class || mainPanel.getComponent(i).getClass() == Building.class)) {
                Container comp = (Container) mainPanel.getComponent(i);
                for(int j = 0; j < comp.getComponentCount(); j++) {
                    //if intersects
                    Rectangle otherRect = Profiscendum.getRectangleRelativeTo((Container) comp.getComponent(j), mainPanel);
                    if (selfRect.intersects(otherRect)) {
                        //if ladder
                        if (comp.getComponent(j).getClass() == Ladder.class && y + selfRect.height < otherRect.y + otherRect.height && (y + selfRect.height) - otherRect.y > 10) {
                            imageChoice = ImageChoice.LADDER;
                            return;
                        }
                    }
                }
            }
        }

        //crouch
        if (verticalDirection == Direction.DOWN) {
            if (horizontalDirection == Direction.RIGHT || (horizontalDirection == Direction.NONE && lastDirection == ImageChoice.RIGHT)) {
                imageChoice = ImageChoice.CROUCHRIGHT;
                lastDirection = ImageChoice.RIGHT;
            } else {
                imageChoice = ImageChoice.CROUCHLEFT;
                lastDirection = ImageChoice.LEFT;
            }
            return;
        }

        //if new direction
        if (horizontalDirection == Direction.RIGHT) {
            imageChoice = ImageChoice.RIGHT;
            lastDirection = ImageChoice.RIGHT;
        } else if (horizontalDirection == Direction.LEFT) {
            imageChoice = ImageChoice.LEFT;
            lastDirection = ImageChoice.LEFT;
        } else {
            imageChoice = lastDirection;
        }
    }
}