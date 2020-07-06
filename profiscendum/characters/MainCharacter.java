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

    public boolean paralyzed = false;
    public boolean buttermode = false;
    public boolean ghostmode = false;

    public IndependentBlock spongeBlock;

    public MainCharacter() {
        Image image = getImage();
        width = image.getWidth(null);
        height = image.getHeight(null);

        x = 400;
        y = 380 - height;

        lastX = x;
        lastY = y;

        health = 20;

        spongeBlock = new IndependentBlock();
        spongeBlock.setSize(10, 15);
        spongeBlock.setVisible(false);
    }

    public enum ImageChoice {
        RIGHT("RIGHT"), LEFT("LEFT"), LADDER("LADDER"), CROUCHLEFT("CROUCH-LEFT"), CROUCHRIGHT("CROUCH-RIGHT"), SPONGE("SPONGE"), BUTTERFLYRIGHT("BUTTERFLY-RIGHT"), BUTTERFLYLEFT("BUTTERFLY-LEFT");

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

        //if already foreign creature, don't change back
        if (imageChoice == ImageChoice.SPONGE || imageChoice == ImageChoice.BUTTERFLYRIGHT || imageChoice == ImageChoice.BUTTERFLYLEFT) {
            //if needing to add block for sponge
            if (imageChoice == ImageChoice.SPONGE && onSolidGround && !mainPanel.isAncestorOf(spongeBlock)) {
                spongeBlock.setLocation(x + 4, y + 15);
                mainPanel.add(spongeBlock, Integer.valueOf(30));
            }
            //butterfly needs direction
            if (imageChoice == ImageChoice.BUTTERFLYRIGHT || imageChoice == ImageChoice.BUTTERFLYLEFT) {
                if (horizontalDirection == Direction.RIGHT || (horizontalDirection == Direction.NONE && lastDirection == ImageChoice.RIGHT)) {
                    imageChoice = ImageChoice.BUTTERFLYRIGHT;
                    lastDirection = ImageChoice.RIGHT;
                } else {
                    imageChoice = ImageChoice.BUTTERFLYLEFT;
                    lastDirection = ImageChoice.LEFT;
                }
            }
            return;
        }

        //if ladder intersects
        Rectangle selfRect = Profiscendum.getRectangleRelativeTo(this, mainPanel);
        selfRect.x = x; //corrections for incorrect getLocation()
        selfRect.y = y;
        for(int i = 0; i < mainPanel.getComponentCount() && !buttermode; i++) {
            if (selfRect.intersects(mainPanel.getComponent(i).getBounds()) && (mainPanel.getComponent(i) instanceof Tower || mainPanel.getComponent(i) instanceof Building)) {
                Container comp = (Container) mainPanel.getComponent(i);
                for(int j = 0; j < comp.getComponentCount(); j++) {
                    //if intersects
                    Rectangle otherRect = Profiscendum.getRectangleRelativeTo((Container) comp.getComponent(j), mainPanel);
                    if (selfRect.intersects(otherRect)) {
                        //if ladder
                        if (comp.getComponent(j) instanceof Ladder && y + selfRect.height < otherRect.y + otherRect.height && (y + selfRect.height) - otherRect.y > 4) {
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