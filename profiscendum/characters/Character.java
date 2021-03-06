package profiscendum.characters;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.AlphaComposite;
import profiscendum.Profiscendum;
import profiscendum.components.*;

public class Character extends JComponent {
    private static final long serialVersionUID = 1L;

    public int dx;
    public int dy;
    public int ay = -2;
    public int x;
    public int y;
    public int lastX;
    public int lastY;
    public int width;
    public int height;
    public Direction verticalDirection = Direction.NONE;
    public Direction horizontalDirection = Direction.RIGHT;

    public boolean onSolidGround = true;
    public int health;

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
        boolean ignoreVerticalBarriers = false;

        Rectangle selfRect = Profiscendum.getRectangleRelativeTo(this, mainPanel);
        selfRect.x = x; //corrections for incorrect getLocation()
        selfRect.y = y;

        //check for all possible barriers
        //floor
        if (y + selfRect.height >= 370 && dy <= 0) {
            isVerticalBarrier = true;
            onSolidGround = true;
            y = 380 - height;
        }
        //walls
        if (x <= 0 && dx < 0) {
            isHorizontalBarrier = true;
        } else if (x + selfRect.width >= 840 && dx > 0) {
            isHorizontalBarrier = true;
        }
        //ladders
        for(int i = 0; i < mainPanel.getComponentCount(); i++) {
            if (selfRect.intersects(mainPanel.getComponent(i).getBounds()) && (mainPanel.getComponent(i) instanceof Tower || mainPanel.getComponent(i) instanceof Building)) {
                Container comp = (Container) mainPanel.getComponent(i);
                for(int j = 0; j < comp.getComponentCount(); j++) {
                    //if intersects
                    Rectangle otherRect = Profiscendum.getRectangleRelativeTo((Container) comp.getComponent(j), mainPanel);
                    if (selfRect.intersects(otherRect) && comp.getComponent(j) instanceof Ladder) {
                        //if buttermode
                        if (this instanceof MainCharacter) {
                            if (((MainCharacter) this).buttermode) {
                                //fall through top of ladder
                                if (y + selfRect.height < otherRect.y + otherRect.height) {
                                    ignoreVerticalBarriers = true;
                                    dy += ay;
                                }
                                break;
                            }
                        }
                        //if at top of ladder
                        if (verticalDirection != Direction.DOWN && y + selfRect.height - otherRect.y < 5) {
                            onSolidGround = true;
                            y = otherRect.y - height + 1;
                            selfRect.y = y;
                            break;
                        }
                        //if on ladder
                        else if (y + selfRect.height < otherRect.y + otherRect.height) {
                            ignoreVerticalBarriers = true;
                            if (verticalDirection == Direction.UP) {
                                dy = 2;
                            } else if (verticalDirection == Direction.DOWN) {
                                dy = -2;
                            } else {
                                dy = 0;
                            }
                            break;
                        }
                    }
                }
            }
        }
        //buildings
        for(int i = 0; i < mainPanel.getComponentCount() && (!isHorizontalBarrier || !isVerticalBarrier); i++) {
            if (selfRect.intersects(mainPanel.getComponent(i).getBounds()) && (mainPanel.getComponent(i) instanceof Tower || mainPanel.getComponent(i) instanceof Building || mainPanel.getComponent(i) instanceof IndependentBlock)) {
                Container comp = (Container) mainPanel.getComponent(i);
                //(exception: you're mc and the block is your sponge block)
                if (this instanceof MainCharacter && comp instanceof IndependentBlock) {
                    if ((IndependentBlock) comp == ((MainCharacter) this).spongeBlock) {
                        isHorizontalBarrier = true;
                        isVerticalBarrier = true;
                        break;
                    }
                }
                for(int j = 0; j < comp.getComponentCount() && (!isHorizontalBarrier || !isVerticalBarrier); j++) {
                    //if intersects
                    Rectangle otherRect = Profiscendum.getRectangleRelativeTo((Container) comp.getComponent(j), mainPanel);
                    if (selfRect.intersects(otherRect)) {
                        //if block
                        if (comp.getComponent(j) instanceof Block) {
                            if (selfRect.intersection(otherRect).width > 2) {
                                //if on top && no other component on top
                                if (y < otherRect.y + otherRect.height && dy <= 0 && !ignoreVerticalBarriers) {
                                    if (comp.getComponentAt(comp.getComponent(j).getX() + otherRect.width/2, comp.getComponent(j).getY() - 4) == null) {
                                        isVerticalBarrier = true;
                                        onSolidGround = true;
                                        y = otherRect.y - height + 1;
                                        selfRect.y = y;
                                    }
                                    else if (! (comp.getComponentAt(comp.getComponent(j).getX() + otherRect.width/2, comp.getComponent(j).getY() - 4) instanceof Block)) {
                                        isVerticalBarrier = true;
                                        onSolidGround = true;
                                        y = otherRect.y - height + 1;
                                        selfRect.y = y;
                                    }
                                }
                                //if on bottom && no other component on bottom
                                else if (y + selfRect.height/2 > otherRect.y && dy > 0 && !ignoreVerticalBarriers) {
                                    if (comp.getComponentAt(comp.getComponent(j).getX() + otherRect.width/2, comp.getComponent(j).getY() + otherRect.height + 4) == null) {
                                        isVerticalBarrier = true;
                                        y = otherRect.y + otherRect.height + 1;
                                        selfRect.y = y;
                                    }
                                    else if (! (comp.getComponentAt(comp.getComponent(j).getX() + otherRect.width/2, comp.getComponent(j).getY() + otherRect.height + 4) instanceof Block)) {
                                        isVerticalBarrier = true;
                                        y = otherRect.y + otherRect.height + 1;
                                        selfRect.y = y;
                                    }
                                }
                            }
                            //if trying to enter horizontally
                            if ((otherRect.x + comp.getComponent(j).getWidth()/2 > x + width/2) == (dx > 0) && selfRect.intersection(otherRect).height > 2) {
                                //skip in ghost mode
                                if (this instanceof MainCharacter) {
                                    if (((MainCharacter) this).ghostmode) {
                                        continue;
                                    }
                                }
                                //no other component on that side
                                if (dx > 0) { //right
                                    Component border = comp.getComponentAt(comp.getComponent(j).getX() - 1, comp.getComponent(j).getY() + otherRect.height/2);
                                    if (border == null) {
                                        isHorizontalBarrier = true;
                                        x = otherRect.x - width + 1;
                                    }
                                    else if (! (border instanceof Block || border instanceof Ladder)) {
                                        isHorizontalBarrier = true;
                                        x = otherRect.x - width + 1;
                                    }
                                } else if (dx < 0) { //left
                                    Component border = comp.getComponentAt(comp.getComponent(j).getX() + otherRect.width + 1, comp.getComponent(j).getY() + otherRect.height/2);
                                    if (border == null) {
                                        isHorizontalBarrier = true;
                                        x = otherRect.x + otherRect.width - 1;
                                    }
                                    else if (! (border instanceof Block || border instanceof Ladder)) {
                                        isHorizontalBarrier = true;
                                        x = otherRect.x + otherRect.width - 1;
                                    }
                                }
                                selfRect.x = x;
                            }
                        }
                        //if door
                        else if (comp.getComponent(j) instanceof Door) {
                            //if buttermode or ghostmode
                            if (this instanceof MainCharacter) {
                                if (((MainCharacter) this).buttermode || ((MainCharacter) this).ghostmode) {
                                    continue;
                                }
                            }
                            //if closed
                            if (!((Door) comp.getComponent(j)).isOpen()) {
                                //if you're beneath it
                                if (y - (otherRect.y + otherRect.height) > -20 && (x - (otherRect.x + otherRect.width) < -1 && otherRect.x - (x + width) > 1) && dy > 0 && !ignoreVerticalBarriers) {
                                    isVerticalBarrier = true;
                                    y = otherRect.y + otherRect.height + 1;
                                    selfRect.y = y;
                                }
                                //or on top of it
                                else if ((y + selfRect.height) - otherRect.y < 10 && (x - (otherRect.x + otherRect.width) < -1 && otherRect.x - (x + width) > 1) && dy <= 0 && !ignoreVerticalBarriers) {
                                    isVerticalBarrier = true;
                                    onSolidGround = true;
                                    y = otherRect.y - height + 1;
                                    selfRect.y = y;
                                }
                                //else if you're trying to enter it horizontally
                                else if ((otherRect.x + comp.getComponent(j).getWidth()/2 > x + width/2) == (horizontalDirection == Direction.RIGHT)) {
                                    isHorizontalBarrier = true;
                                    if (horizontalDirection == Direction.RIGHT) {
                                        x = otherRect.x - width + 1;
                                    } else {
                                        x = otherRect.x + otherRect.width - 1;
                                    }
                                    selfRect.x = x;
                                }
                            }
                        }
                    }
                }
            }
        }

        //vertical things for butterflies
        if (this instanceof MainCharacter && !isVerticalBarrier) {
            if (((MainCharacter) this).imageChoice == MainCharacter.ImageChoice.BUTTERFLYLEFT || ((MainCharacter) this).imageChoice == MainCharacter.ImageChoice.BUTTERFLYRIGHT) {
                if (verticalDirection == Direction.UP) {
                    dy = 3;
                } else if (verticalDirection == Direction.DOWN) {
                    dy = -3;
                } else {
                    dy = 0;
                }
            }
        }

        if (!isHorizontalBarrier) {
            x += dx;
        }
        if (ignoreVerticalBarriers) {
            onSolidGround = false;
            y -= dy;
        }
        else if (!isVerticalBarrier) {
            onSolidGround = false;
            dy += ay;
            y -= dy;
        } else {
            dy = 0;
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
        if (this instanceof MainCharacter) {
            if (((MainCharacter) this).ghostmode) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            } else {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }
        }
        g2d.drawImage(getImage(), 0, 0, null);
    }
}