package profiscendum;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.plaf.basic.BasicScrollBarUI;
import profiscendum.components.*;
import profiscendum.characters.*;
import profiscendum.characters.Character.Direction;

public class Profiscendum extends JFrame implements KeyListener {

    private static final long serialVersionUID = 1L; // version 1

    // MARK: - Objects

    private String[] alphabet = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
            "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
    private String input;
    private Random rand;

    private JLayeredPane mainPanel;
    private JLayeredPane characterPanel;

    // Labels & buttons
    private JPanel startPanel;
    private JLabel startLabel;
    private JLabel highScoreLabel;
    private int highScore;
    private JLabel howToPlayLabel;
    private JLabel backLabel;
    private JScrollPane howToPlayScroll;
    private JTextArea howToPlayArea;
    private JLabel randomLetterLabel;

    // Game loop variables
    private boolean running = false;
    private boolean paused = false;
    private int fps = 30;
    private int frameCount = 0;

    // Characters
    private MainCharacter MC;

    //Structures
    private Tower leftTower, rightTower;
    private Building leftBuilding, centerBuilding, rightBuilding;

    // MARK: - Constructor

    public Profiscendum() {
        // Window setup
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("mmmm destruction, yummy");
        setSize(840, 440);
        setLocation(165, 75);
        setResizable(false);

        // Layout setup
        JPanel cp = (JPanel) getContentPane();
        cp.setLayout(new BorderLayout());
        mainPanel = new JLayeredPane();
        cp.add("Center", mainPanel);

        rand = ThreadLocalRandom.current();

        // Retrieve high score
        try {
            BufferedReader f = new BufferedReader(new FileReader("highScore.txt"));
            highScore = Integer.parseInt(f.readLine());
            f.close();
        } catch (IOException e) {
        }

        // Add elements
        setupComponents();

        // see!
        cp.validate();
        setVisible(true);
    }

    // MARK: - Adding elements

    private void setupComponents() {
        // background
        JComponent background = new JComponent() {
            private static final long serialVersionUID = 1L;

            // paint background color
            public void paintComponent(Graphics g) {
                g.setColor(new Color(192, 163, 148));
                g.fillRect(0, 0, 840, 420);
                g.setColor(new Color(79, 69, 64));
                g.fillRect(0, 380, 840, 40);
            }
        };
        background.setLocation(0, 0);
        background.setSize(getSize());
        mainPanel.add(background, Integer.valueOf(0));

        characterPanel = new JLayeredPane();
        characterPanel.setBounds(0, 0, 840, 420);
        mainPanel.add(characterPanel, Integer.valueOf(30));

        // towers
        leftTower = new Tower();
        rightTower = new Tower();
        leftTower.setLocation(50, 155);
        rightTower.setLocation(730, 155);
        leftTower.setSize(leftTower.getPreferredSize());
        rightTower.setSize(rightTower.getPreferredSize());
        mainPanel.add(leftTower, Integer.valueOf(10));
        mainPanel.add(rightTower, Integer.valueOf(10));

        //buildings
        leftBuilding = new Building(145, 250, 5);
        leftBuilding.setBounds(145, 130, 145, 250);
            Ladder ladder = new Ladder();
            ladder.setBounds(28, 0, 12, 2*leftBuilding.floorHeight + 3);
            leftBuilding.add(ladder, Integer.valueOf(10));
            ladder = new Ladder();
            ladder.setBounds(leftBuilding.getWidth() - 28, leftBuilding.floorHeight + 3, 12, 2*leftBuilding.floorHeight);
            leftBuilding.add(ladder, Integer.valueOf(10));
            ladder = new Ladder();
            ladder.setBounds(39, 3*leftBuilding.floorHeight + 3, 12, leftBuilding.floorHeight);
            leftBuilding.add(ladder, Integer.valueOf(10));
            ladder = new Ladder();
            ladder.setBounds(90, 4*leftBuilding.floorHeight + 3, 12, leftBuilding.floorHeight);
            leftBuilding.add(ladder, Integer.valueOf(10));
            Door door = new Door(3);
            door.setBounds(75, 15, door.maxStrength, leftBuilding.floorHeight - 12);
            leftBuilding.add(door, Integer.valueOf(10));
            door = new Door(3);
            door.setBounds(65, 15 + 3*leftBuilding.floorHeight, door.maxStrength, leftBuilding.floorHeight - 12);
            leftBuilding.add(door, Integer.valueOf(10));
            leftBuilding.fillRectWithBlocks(65, leftBuilding.floorHeight + 15, 10, leftBuilding.floorHeight - 12);

        centerBuilding = new Building(145, 160, 3);
        centerBuilding.setBounds(350, 220, 145, 160);
            ladder = new Ladder();
            ladder.setBounds(108, 0, 12, 2*centerBuilding.floorHeight + 3);
            centerBuilding.add(ladder, Integer.valueOf(10));
            ladder = new Ladder();
            ladder.setBounds(40, 2*centerBuilding.floorHeight + 3, 12, centerBuilding.floorHeight);
            centerBuilding.add(ladder, Integer.valueOf(10));
            door = new Door(8);
            door.setBounds(62, 15, door.maxStrength, centerBuilding.floorHeight - 12);
            centerBuilding.add(door, Integer.valueOf(10));
            door = new Door(8);
            door.setBounds(62, 15 + centerBuilding.floorHeight, door.maxStrength, centerBuilding.floorHeight - 12);
            centerBuilding.add(door, Integer.valueOf(10));

        rightBuilding = new Building(145, 350, 8);
        rightBuilding.setBounds(550, 30, 145, 350);
            ladder = new Ladder();
            ladder.setBounds(rightBuilding.getWidth() - 28, 0, 12, rightBuilding.floorHeight + 3);
            rightBuilding.add(ladder, Integer.valueOf(10));
            ladder = new Ladder();
            ladder.setBounds(17, rightBuilding.floorHeight + 3, 12, 2*rightBuilding.floorHeight);
            rightBuilding.add(ladder, Integer.valueOf(10));
            ladder = new Ladder();
            ladder.setBounds(83, rightBuilding.floorHeight + 3, 12, rightBuilding.floorHeight);
            rightBuilding.add(ladder, Integer.valueOf(10));
            ladder = new Ladder();
            ladder.setBounds(63, 3*rightBuilding.floorHeight + 3, 12, 2*rightBuilding.floorHeight);
            rightBuilding.add(ladder, Integer.valueOf(10));
            ladder = new Ladder();
            ladder.setBounds(94, 3*rightBuilding.floorHeight + 3, 12, rightBuilding.floorHeight);
            rightBuilding.add(ladder, Integer.valueOf(10));
            ladder = new Ladder();
            ladder.setBounds(20, 5*rightBuilding.floorHeight + 3, 12, 2*rightBuilding.floorHeight);
            rightBuilding.add(ladder, Integer.valueOf(10));
            ladder = new Ladder();
            ladder.setBounds(109, 5*rightBuilding.floorHeight + 3, 12, rightBuilding.floorHeight);
            rightBuilding.add(ladder, Integer.valueOf(10));
            ladder = new Ladder();
            ladder.setBounds(52, 7*rightBuilding.floorHeight + 3, 12, rightBuilding.floorHeight + 3);
            rightBuilding.add(ladder, Integer.valueOf(10));
            door = new Door(3);
            door.setBounds(50, 15, door.maxStrength, rightBuilding.floorHeight - 12);
            rightBuilding.add(door, Integer.valueOf(10));
            door = new Door(3);
            door.setBounds(100, 15, door.maxStrength, rightBuilding.floorHeight - 12);
            rightBuilding.add(door, Integer.valueOf(10));
            door = new Door(3);
            door.setBounds(55, 2*rightBuilding.floorHeight + 15, door.maxStrength, rightBuilding.floorHeight - 12);
            rightBuilding.add(door, Integer.valueOf(10));
            door = new Door(3);
            door.setBounds(45, 4*rightBuilding.floorHeight + 15, door.maxStrength, rightBuilding.floorHeight - 12);
            rightBuilding.add(door, Integer.valueOf(10));
            door = new Door(3);
            door.setBounds(81, 6*rightBuilding.floorHeight + 15, door.maxStrength, rightBuilding.floorHeight - 12);
            rightBuilding.add(door, Integer.valueOf(10));
            door = new Door(3);
            door.setBounds(94, 7*rightBuilding.floorHeight + 15, door.maxStrength, rightBuilding.floorHeight - 9);
            rightBuilding.add(door, Integer.valueOf(10));
            rightBuilding.fillRectWithBlocks(68, rightBuilding.floorHeight + 15, 10, rightBuilding.floorHeight - 12);
            rightBuilding.fillRectWithBlocks(80, 3*rightBuilding.floorHeight + 15, 10, rightBuilding.floorHeight - 12);
            rightBuilding.fillRectWithBlocks(56, 5*rightBuilding.floorHeight + 15, 10, rightBuilding.floorHeight - 12);
            //make room for doors ->
            rightBuilding.remove(rightBuilding.getComponentAt(4, 3*rightBuilding.floorHeight+15 + 5));
            rightBuilding.remove(rightBuilding.getComponentAt(4, 3*rightBuilding.floorHeight+15 + 15));
            rightBuilding.remove(rightBuilding.getComponentAt(4, 3*rightBuilding.floorHeight+15 + 25));
            rightBuilding.remove(rightBuilding.getComponentAt(4, 3*rightBuilding.floorHeight+15 + 35));
            rightBuilding.remove(rightBuilding.getComponentAt(8, 3*rightBuilding.floorHeight+15 + 5));
            rightBuilding.remove(rightBuilding.getComponentAt(8, 3*rightBuilding.floorHeight+15 + 15));
            rightBuilding.remove(rightBuilding.getComponentAt(8, 3*rightBuilding.floorHeight+15 + 25));
            rightBuilding.remove(rightBuilding.getComponentAt(8, 3*rightBuilding.floorHeight+15 + 35));
            rightBuilding.fillRectWithBlocks(0, 4*rightBuilding.floorHeight + 3, 15, 10);
            Block platform = new Block();
            platform.setBounds(-10, 4*rightBuilding.floorHeight + 3, 11, 10);
            rightBuilding.add(platform, Integer.valueOf(9));
            rightBuilding.remove(rightBuilding.getComponentAt(rightBuilding.getWidth() - 4, 2*rightBuilding.floorHeight+15 + 5));
            rightBuilding.remove(rightBuilding.getComponentAt(rightBuilding.getWidth() - 4, 2*rightBuilding.floorHeight+15 + 15));
            rightBuilding.remove(rightBuilding.getComponentAt(rightBuilding.getWidth() - 4, 2*rightBuilding.floorHeight+15 + 20));
            rightBuilding.remove(rightBuilding.getComponentAt(rightBuilding.getWidth() - 4, 2*rightBuilding.floorHeight+15 + 30));
            rightBuilding.remove(rightBuilding.getComponentAt(rightBuilding.getWidth() - 9, 2*rightBuilding.floorHeight+15 + 5));
            rightBuilding.remove(rightBuilding.getComponentAt(rightBuilding.getWidth() - 9, 2*rightBuilding.floorHeight+15 + 15));
            rightBuilding.remove(rightBuilding.getComponentAt(rightBuilding.getWidth() - 9, 2*rightBuilding.floorHeight+15 + 20));
            rightBuilding.remove(rightBuilding.getComponentAt(rightBuilding.getWidth() - 9, 2*rightBuilding.floorHeight+15 + 30));
            rightBuilding.fillRectWithBlocks(rightBuilding.getWidth() - 15, 3*rightBuilding.floorHeight + 3, 15, 4);
            rightBuilding.fillRectWithBlocks(rightBuilding.getWidth() - 15, 2*rightBuilding.floorHeight + 11, 15, 4);
            platform = new Block();
            platform.setBounds(rightBuilding.getWidth() - 1, 3*rightBuilding.floorHeight + 3, 11, 10);
            rightBuilding.add(platform, Integer.valueOf(9));
            JComponent threshold = new JComponent() {
				private static final long serialVersionUID = 1L;
				@Override
                protected void paintComponent(Graphics g) {
                    g.setColor(new Color(84, 81, 80, 150)); //door thresholds
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            threshold.setBounds(0, 3*rightBuilding.floorHeight + 15, 15, rightBuilding.floorHeight - 12);
            rightBuilding.add(threshold, Integer.valueOf(1));
            threshold = new JComponent() {
				private static final long serialVersionUID = 1L;
				@Override
                protected void paintComponent(Graphics g) {
                    g.setColor(new Color(84, 81, 80, 150)); //door thresholds
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            threshold.setBounds(rightBuilding.getWidth() - 15, 2*rightBuilding.floorHeight + 15, 15, rightBuilding.floorHeight - 12);
            rightBuilding.add(threshold, Integer.valueOf(1));
            door = new Door(4);
            door.setBounds(5, 3*rightBuilding.floorHeight + 15, door.maxStrength, rightBuilding.floorHeight - 12);
            rightBuilding.add(door, Integer.valueOf(10));
            door = new Door(4);
            door.setBounds(rightBuilding.getWidth() - 9, 2*rightBuilding.floorHeight + 15, door.maxStrength, rightBuilding.floorHeight - 12);
            rightBuilding.add(door, Integer.valueOf(10));

        mainPanel.add(leftBuilding, Integer.valueOf(10));
        mainPanel.add(centerBuilding, Integer.valueOf(10));
        mainPanel.add(rightBuilding, Integer.valueOf(10));

        // labels
        randomLetterLabel = new JLabel();
        randomLetterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        randomLetterLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        randomLetterLabel.setForeground(new Color(1f, 1f, 1f, 0.6f));
        randomLetterLabel.setBounds(0, 380, 840, 40);
        mainPanel.add(randomLetterLabel, JLayeredPane.PALETTE_LAYER, Integer.valueOf(0));

        // start panel
        startPanel = new JPanel(new GridBagLayout()) {
            private static final long serialVersionUID = 1L;

            // grey background
            public void paintComponent(Graphics g) {
                g.setColor(new Color(0f, 0f, 0f, 0.7f));
                g.fillRect(0, 0, 840, 420);
            }
        };
        startPanel.setOpaque(false);
        startPanel.setBounds(0, 0, 840, 420);

        GridBagConstraints c = new GridBagConstraints();

        startLabel = new JLabel("START GAME");
        startLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 40));
        startLabel.setForeground(new Color(1f, 1f, 1f, 0.6f));
        c.gridwidth = 2;
        c.ipady = 10;
        startLabel.addMouseListener(new MouseAdapter() {
            // bold on hover
            @Override
            public void mouseEntered(MouseEvent e) {
                startLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
            }

            // unbold on exit
            @Override
            public void mouseExited(MouseEvent e) {
                startLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 40));
            }

            // start on click
            @Override
            public void mouseClicked(MouseEvent e) {
                // if game not started (vs RESUME GAME)
                if (startLabel.getText().equals("START GAME")) {
                    setupCharacters();
                }
                // start/resume movement & timers
                addListeners();
                running = true;
                paused = false;
                runGameLoop();

                mainPanel.remove(startPanel);
                mainPanel.validate();
                mainPanel.repaint();
            }
        });
        startPanel.add(startLabel, c);

        highScoreLabel = new JLabel("HIGH SCORE: " + highScore);
        highScoreLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        highScoreLabel.setForeground(new Color(1f, 1f, 1f, 0.6f));
        c.gridy = 1;
        c.gridwidth = 1;
        c.ipadx = 20;
        startPanel.add(highScoreLabel, c);

        howToPlayLabel = new JLabel("HOW TO PLAY");
        howToPlayLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        howToPlayLabel.setForeground(new Color(1f, 1f, 1f, 0.6f));
        c.gridx = 1;
        howToPlayLabel.addMouseListener(new MouseAdapter() {
            // bold on hover
            @Override
            public void mouseEntered(MouseEvent e) {
                howToPlayLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
            }

            // unbold on exit
            @Override
            public void mouseExited(MouseEvent e) {
                howToPlayLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
            }

            // change labels on click
            @Override
            public void mouseClicked(MouseEvent e) {
                startPanel.removeAll();

                c.gridx = 0;
                c.gridy = 0;
                c.ipadx = 0;
                c.ipady = 0;
                startPanel.add(backLabel, c);

                JComponent blankTop = new JComponent() {
                    private static final long serialVersionUID = 1L;

                    public Dimension getMinimumSize() {
                        return new Dimension(400, 1);
                    }
                };
                c.gridx = 1;
                c.gridwidth = 9;
                startPanel.add(blankTop, c);

                JComponent blankRight = new JComponent() {
                    private static final long serialVersionUID = 1L;

                    public Dimension getMinimumSize() {
                        return new Dimension(1, 300);
                    }
                };
                c.gridx = 9;
                c.gridwidth = 1;
                c.gridheight = 2;
                startPanel.add(blankRight, c);

                c.gridx = 0;
                c.gridy = 1;
                c.gridwidth = 10;
                c.gridheight = 1;
                c.fill = GridBagConstraints.BOTH;
                startPanel.add(howToPlayScroll, c);

                howToPlayLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
                howToPlayScroll.getVerticalScrollBar().setValue(0);

                startPanel.validate();
                startPanel.repaint();
            }
        });
        startPanel.add(howToPlayLabel, c);

        backLabel = new JLabel("←");
        backLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 40));
        backLabel.setForeground(new Color(1f, 1f, 1f, 0.6f));
        backLabel.addMouseListener(new MouseAdapter() {
            // bold on hover
            @Override
            public void mouseEntered(MouseEvent e) {
                backLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
            }

            // unbold on exit
            @Override
            public void mouseExited(MouseEvent e) {
                backLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 40));
            }

            // change labels on click
            @Override
            public void mouseClicked(MouseEvent e) {
                startPanel.removeAll();

                c.gridy = 0;
                c.gridwidth = 2;
                c.ipady = 10;
                c.fill = GridBagConstraints.NONE;
                startPanel.add(startLabel, c);
                c.gridy = 1;
                c.gridwidth = 1;
                c.ipadx = 20;
                startPanel.add(highScoreLabel, c);
                c.gridx = 1;
                startPanel.add(howToPlayLabel, c);
                backLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 40));

                startPanel.validate();
                startPanel.repaint();
            }
        });

        howToPlayArea = new JTextArea();
        howToPlayArea.setLineWrap(true);
        howToPlayArea.setWrapStyleWord(true);
        howToPlayArea.setSize(1, 1); // will expand to size
        howToPlayArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        howToPlayArea.setForeground(new Color(1f, 1f, 1f, 0.8f));
        howToPlayArea.setBackground(new Color(56, 49, 44));
        howToPlayArea.setText("PROFISCENDUM\n" + "\n"
                + "You are in charge of protecting the small fortified settlement of Oppidum "
                + "(well, not as much in charge of as the person who was roped into the position on account of their debt). "
                + "However, two large armies are fighting each other near you, which ends up in them both fighting to control your settlement. "
                + "You are an inferior, unexperienced fighter who can only reliably use a bow and arrow. "
                + "Your settlement is guaranteed to go down in this battle... but little known to the innocent civilians around you, "
                + "you have an escape dragon that can take you and whoever else you want to safety. "
                + "If only you were a good enough fighter to save them all, you wouldn’t have had to leave so many of them behind to die. "
                + "But alas... when you think you’re gaining their trust, you might accidentally set off a bomb, or pop out a baby, or "
                + "call the wrong dragon to save you. Is it even worth saving anyone else? How badly will things go before you escape?\n"
                + "\n" + "\n" + "\n" + "CONTROLS\n" + "\n" + "← → : Move in the left or right direction.\n"
                + "↑ : Jump or climb a ladder.\n" + "↓ : Crouch or descend a ladder.\n"
                + "SPACE: Shoot your bow and arrow in whatever direction you're facing.\n"
                + "SHIFT: Open or close a door. (Must be standing at the door itself to use)\n"
                + "ENTER: Call your escape dragon, which will arrive one minute after you call them and depart 30 seconds after that.\n"
                + "\n"
                + "As for the rest of your abilities, each one can be called by a different key on your keyboard (A-Z). "
                + "However, they change positions between every game. You have no idea which one you're using when you press a key. "
                + "A short description will only show up once you press the key. For your sake, here are longer descriptions of each.\n"
                + "\n"
                + "Open and close doors: This toggles the state of random doors. Weaker doors are more likely to flip than stronger doors. "
                + "(The probability of a door flipping is 1/its current strength.)\n"
                + "\n"
                + "Throw bomb:\n"
                + "\n"
                + "Pick people up:\n"
                + "\n"
                + "Set people down:\n"
                + "\n"
                + "Nearby people fight:\n"
                + "\n"
                + "Certain groups of people fight:\n"
                + "\n"
                + "Calling the wrong dragon:\n"
                + "\n"
                + "Aura of trust:\n"
                + "\n"
                + "Aura of mistrust:\n"
                + "\n"
                + "Pop out a baby:\n"
                + "\n"
                + "Burst from jetpack: Allows you to fly through the air using propulsion.\n"
                + "\n"
                + "Instant fame:\n"
                + "\n"
                + "Temporary paralysis:\n"
                + "\n"
                + "Coated in butter:\n"
                + "\n"
                + "Tempt people to push you off a ledge:\n"
                + "\n"
                + "Human bodyguard:\n"
                + "\n"
                + "Pop out a cobblestone:\n"
                + "\n"
                + "Distribute poison:\n"
                + "\n"
                + "Ghost mode:\n"
                + "\n"
                + "Fire alarm:\n"
                + "\n"
                + "Throw boomerang:\n"
                + "\n"
                + "Random character death:\n"
                + "\n"
                + "Cry uncontrollably:\n"
                + "\n"
                + "Instant kill arrow:\n"
                + "\n"
                + "Turn into a butterfly:\n"
                + "\n"
                + "Turn into a sponge:\n");

        howToPlayScroll = new JScrollPane(howToPlayArea);
        howToPlayScroll.setBorder(null);
        howToPlayScroll.setBackground(new Color(56, 49, 44));
        howToPlayScroll.getVerticalScrollBar().setUnitIncrement(3);
        howToPlayScroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(125, 122, 120);
                this.thumbHighlightColor = new Color(0, 0, 0, 0);
                this.thumbDarkShadowColor = thumbColor;
                this.thumbLightShadowColor = thumbColor;
                this.trackColor = new Color(56, 49, 44);
            }
            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton button = new JButton();
                Dimension nothing = new Dimension(0,0);
                button.setPreferredSize(nothing);
                button.setMinimumSize(nothing);
                button.setMaximumSize(nothing);
                return button;
            }
            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton button = new JButton();
                Dimension nothing = new Dimension(0,0);
                button.setPreferredSize(nothing);
                button.setMinimumSize(nothing);
                button.setMaximumSize(nothing);
                return button;
            }
        });

        mainPanel.add(startPanel, JLayeredPane.MODAL_LAYER, Integer.valueOf(0));
    }

    private void setupCharacters() {
        //TODO: setup characters for new game
        MC = new MainCharacter();
        MC.setBounds(MC.x, MC.y, MC.width, MC.height);
        characterPanel.add(MC, Integer.valueOf(0));
    }

    private void addListeners() {
        // Input setup
        addKeyListener(this);
        shuffleArray(alphabet);
    }

    // The entry main() method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Profiscendum();
            }
        });
    }

    //MARK: - Implemented methods

    @Override
    public void keyPressed(KeyEvent e) {
        input = KeyEvent.getKeyText(e.getKeyCode()).toUpperCase();
        switch (input) {
            case "␣":
            case "SPACE":
                break;
            case "⇧":
            case "SHIFT":
                Rectangle selfRect = Profiscendum.getRectangleRelativeTo(MC, mainPanel);
                selfRect.x = MC.x; //corrections for incorrect getLocation()
                selfRect.y = MC.y;
                for(int i = 0; i < mainPanel.getComponentCount(); i++) {
                    if (selfRect.intersects(mainPanel.getComponent(i).getBounds()) && (mainPanel.getComponent(i).getClass() == Tower.class || mainPanel.getComponent(i).getClass() == Building.class)) {
                        Container comp = (Container) mainPanel.getComponent(i);
                        for(int j = 0; j < comp.getComponentCount(); j++) {
                            //if intersects
                            Rectangle otherRect = Profiscendum.getRectangleRelativeTo((Container) comp.getComponent(j), mainPanel);
                            if (selfRect.intersects(otherRect)) {
                                //if door
                                if (comp.getComponent(j).getClass() == Door.class) {
                                    //toggle
                                    Door door = (Door) comp.getComponent(j);
                                    door.setDoor(!door.isOpen());
                                    Rectangle bounds = getRectangleRelativeTo(door, mainPanel);
                                    bounds.width = Math.max(door.maxStrength, 15);
                                    mainPanel.repaint(bounds);
                                    return;
                                }
                            }
                        }
                    }
                }
                break;
            case "↑":
            case "UP":
                if (MC.onSolidGround) {
                    MC.dy = 16;
                    MC.onSolidGround = false;
                }
                if (MC.imageChoice.value.equals("LADDER")) {
                    MC.dy = 2;
                }
                MC.verticalDirection = Direction.UP;
                break;
            case "↓":
            case "DOWN":
                if (MC.imageChoice.value.equals("LADDER")) {
                    MC.dy = -2;
                }
                else if (!MC.onSolidGround) {
                    MC.dy--;
                }
                else if (MC.dx > 2) { MC.dx = 2; }
                else if (MC.dx < -2) { MC.dx = -2; }
                MC.verticalDirection = Direction.DOWN;
                break;
            case "→":
            case "RIGHT":
                if (MC.verticalDirection == Direction.DOWN) {
                    MC.dx = 2;
                } else {
                    MC.dx = 5;
                }
                MC.horizontalDirection = Direction.RIGHT;
                break;
            case "←":
            case "LEFT":
                if (MC.verticalDirection == Direction.DOWN) {
                    MC.dx = -2;
                } else {
                    MC.dx = -5;
                }
                MC.horizontalDirection = Direction.LEFT;
                break;
            default:
            //if-else instead switch for keyboard
            if (input.equals(alphabet[0])) { //open random doors
                randomLetterLabel.setText("Key " + input + " has caused some doors to open and close.");

                Door door;
                for(int i = 0; i < mainPanel.getComponentCount(); i++) {
                    for(int j = 0; j < ((Container) mainPanel.getComponent(i)).getComponentCount(); j++) {
                        if (((Container) mainPanel.getComponent(i)).getComponent(j).getClass() == Door.class) {
                            //randomly open or close based on strength
                            door = (Door) ((Container) mainPanel.getComponent(i)).getComponent(j);
                            if (rand.nextDouble() < 1d/door.getStrength()) {
                                //toggle
                                door.setDoor(!door.isOpen());
                                Rectangle bounds = getRectangleRelativeTo(door, mainPanel);
                                bounds.width = Math.max(door.maxStrength, 15);
                                mainPanel.repaint(bounds);
                            }
                        }
                    }
                }
            } else if (input.equals(alphabet[1])) { //throw bomb
                //
            } else if (input.equals(alphabet[2])) { //pick up people
                //
            } else if (input.equals(alphabet[3])) { //set down people
                //
            } else if (input.equals(alphabet[4])) { //nearby people fight
                //
            } else if (input.equals(alphabet[5])) { //certain class of people fight
                //
            } else if (input.equals(alphabet[6])) { //dragon swoops down and eliminates a building
                //
            } else if (input.equals(alphabet[7])) { //aura of trust - follow, shot at less
                //
            } else if (input.equals(alphabet[8])) { //aura of mistrust - avoid, shot at more
                //
            } else if (input.equals(alphabet[9])) { //pop out a baby (not citizen, but 1 citizen will try to rescue it, and enemies will go uwu over it)
                //
            } else if (input.equals(alphabet[10])) { //burst from jetpack
                randomLetterLabel.setText("Key " + input + " has caused your jetpack to sputter to life.");
                MC.ay = 1;
                if (MC.onSolidGround) {
                    MC.dy++;
                }
                MC.onSolidGround = false;
            } else if (input.equals(alphabet[11])) { //instant fame with the boys/girls/enbies (w/ chance of losing fame and not being able to regain it for a time) You have tried to game instant fame and failed miserably.
                //
            } else if (input.equals(alphabet[12])) { //temporary leg-down paralysis
                //
            } else if (input.equals(alphabet[13])) { //coated you in butter - slide past closed doors, can’t climb ladders
                //
            } else if (input.equals(alphabet[14])) { //tempt people to push you off a ledge
                //
            } else if (input.equals(alphabet[15])) { //people form human bodyguard
                //
            } else if (input.equals(alphabet[16])) { //pop out a cobblestone
                //
            } else if (input.equals(alphabet[17])) { //give out poison
                //
            } else if (input.equals(alphabet[18])) { //ghost mode - walk through horizontal barriers
                //
            } else if (input.equals(alphabet[19])) { //pull fire alarm in random building, cause panic
                //
            } else if (input.equals(alphabet[20])) { //throw boomerang
                //
            } else if (input.equals(alphabet[21])) { //random character death
                //
            } else if (input.equals(alphabet[22])) { //start crying uncontrollably (stops when find random shoulder to cry on)
                //
            } else if (input.equals(alphabet[23])) { //next arrow is instant kill arrow
                //
            } else if (input.equals(alphabet[24])) { //turn into a butterfly
                //
            } else if (input.equals(alphabet[25])) { //turn into a sponge
                //
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        input = KeyEvent.getKeyText(e.getKeyCode()).toUpperCase();
        switch (input) {
            case "↑":
            case "UP":
                MC.verticalDirection = Direction.NONE;
                if (MC.imageChoice.value.equals("LADDER")) {
                    MC.dy = 0;
                }
                break;
            case "↓":
            case "DOWN":
                MC.verticalDirection = Direction.NONE;
                if (MC.imageChoice.value.equals("LADDER")) {
                    MC.dy = 0;
                }
                break;
            case "→":
            case "RIGHT":
                MC.dx = 0;
                MC.horizontalDirection = Direction.NONE;
                break;
            case "←":
            case "LEFT":
                MC.dx = 0;
                MC.horizontalDirection = Direction.NONE;
                break;
            default:
            if (input.equals(alphabet[10])) {
                MC.ay = -2; //reset gravity
            }
        }
    }

    //MARK: - Game loop (credits to Eli Delventhal on java-gaming.org)

    //Starts a new thread and runs the game loop in it.
    public void runGameLoop() {
        Thread loop = new Thread() {
            public void run() {
                gameLoop();
            }
        };
        loop.start();
    }

    private void gameLoop() {

        final double GAME_HERTZ = 30.0; //game updates/sec
        //Calculate how many ns each frame should take for our target game hertz.
        final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
        //At the very most we will update the game this many times before a new render.
        final int MAX_UPDATES_BEFORE_RENDER = 5;

        double lastUpdateTime = System.nanoTime();
        double lastRenderTime = System.nanoTime();

        //If we are able to get as high as this FPS, don't render again.
        final double TARGET_FPS = 60;
        final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

        //Simple way of finding FPS.
        int lastSecondTime = (int) (lastUpdateTime / 1000000000);

        while (running) {
            double now = System.nanoTime();
            int updateCount = 0;

            if (!paused) {
                //Do as many game updates as we need to, potentially playing catchup.
                while(now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER) {
                    updateGame();
                    lastUpdateTime += TIME_BETWEEN_UPDATES;
                    updateCount++;
                }

                //If for some reason an update takes forever, we don't want to do an insane number of catchups.
                //If you were doing some sort of game that needed to keep EXACT time, you would get rid of this.
                if ( now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
                    lastUpdateTime = now - TIME_BETWEEN_UPDATES;
                }

                //Render. To do so, we need to calculate interpolation for a smooth render.
                float interpolation = Math.min(1.0f, (float) ((now - lastUpdateTime) / TIME_BETWEEN_UPDATES) );
                drawGame(interpolation);
                lastRenderTime = now;

                //Update the frames we got.
                int thisSecond = (int) (lastUpdateTime / 1000000000);
                if (thisSecond > lastSecondTime) {
                    System.out.println("NEW SECOND " + thisSecond + " " + frameCount);
                    fps = frameCount;
                    frameCount = 0;
                    lastSecondTime = thisSecond;
                }

                //Yield until it has been at least the target time between renders. This saves the CPU from hogging.
                while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
                    Thread.yield();

                    try {
                        Thread.sleep(1);
                    } catch (Exception e) {}

                    now = System.nanoTime();
                }
            }
        }
    }

    /** Sets the differences in location/appearance/etc to later be rendered. */
    private void updateGame() {
        MC.move(mainPanel);
    }

    /** Renders each difference made. */
    private void drawGame(float interpolation) {

        //MC
        int drawX = (int) ((MC.x - MC.lastX) * interpolation + MC.lastX - MC.width/2);
        int drawY = (int) ((MC.y - MC.lastY) * interpolation + MC.lastY - MC.height/2);
        MC.setBounds(drawX, drawY, MC.getWidth(), MC.getHeight());
        MC.lastX = drawX;
        MC.lastY = drawY;

        characterPanel.repaint();
        frameCount++;
    }

    //not used
    @Override public void keyTyped(KeyEvent e) {}

    //MARK: - Helper methods

    private void shuffleArray(String[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            // Simple swap
            String a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
    }

    /**
     * Gets the bounds of a container relative to another.
     */
    public static Rectangle getRectangleRelativeTo(Container container, Container relativeTo) {
        Point smallP = container.getLocationOnScreen();
        Point largeP = relativeTo.getLocationOnScreen();
        return new Rectangle(smallP.x - largeP.x, smallP.y - largeP.y, container.getWidth(), container.getHeight());
    }
}