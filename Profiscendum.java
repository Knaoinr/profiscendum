import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import components.*;

public class Profiscendum extends JFrame implements KeyListener {

    private static final long serialVersionUID = 1L; // version 1

    // MARK: - Objects

    private String[] alphabet = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q",
            "r", "s", "t", "u", "v", "w", "x", "y", "z" };
    private String input;
    private Random rand;

    private JLayeredPane mainPanel;

    //Labels & buttons
    private JPanel startPanel;
    private JLabel startLabel;
    private JLabel highScoreLabel;
    private int highScore;
    private JLabel howToPlayLabel;
    private JLabel backLabel;
    private JScrollPane howToPlayScroll;
    private JTextArea howToPlayArea;
    private JLabel randomLetterLabel;

    //Game loop variables
    private boolean running = false;
    private boolean paused = false;
    private int fps = 30;
    private int frameCount = 0;

    private Tower leftTower, rightTower;

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
        } catch (IOException e) {}

        //Add elements
        setupComponents();

        //see!
        cp.validate();
        setVisible(true);
    }

    // MARK: - Adding elements

    private void setupComponents() {
        //background
        JComponent background = new JComponent() {
            private static final long serialVersionUID = 1L;

            //paint background color
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

        //towers
        leftTower = new Tower();
        rightTower = new Tower();
        leftTower.setLocation(50, 155);
        rightTower.setLocation(730, 155);
        leftTower.setSize(leftTower.getPreferredSize());
        rightTower.setSize(rightTower.getPreferredSize());
        mainPanel.add(leftTower, Integer.valueOf(10));
        mainPanel.add(rightTower, Integer.valueOf(10));

        //labels
        randomLetterLabel = new JLabel();
        randomLetterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        randomLetterLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        randomLetterLabel.setForeground(new Color(1f, 1f, 1f, 0.6f));
        randomLetterLabel.setBounds(0, 380, 840, 40);
        mainPanel.add(randomLetterLabel, JLayeredPane.PALETTE_LAYER, Integer.valueOf(0));

        //start panel
        startPanel = new JPanel(new GridBagLayout()) {
            private static final long serialVersionUID = 1L;
            //grey background
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
            //bold on hover
            @Override
            public void mouseEntered(MouseEvent e) {
                startLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
            }
            //unbold on exit
            @Override
            public void mouseExited(MouseEvent e) {
                startLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 40));
            }
            //start on click
            @Override
            public void mouseClicked(MouseEvent e) {
                //if game not started (vs RESUME GAME)
                if (startLabel.getText().equals("START GAME")) {
                    setupCharacters();
                }
                //start/resume movement & timers
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
            //bold on hover
            @Override
            public void mouseEntered(MouseEvent e) {
                howToPlayLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
            }
            //unbold on exit
            @Override
            public void mouseExited(MouseEvent e) {
                howToPlayLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
            }
            //change labels on click
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
            //bold on hover
            @Override
            public void mouseEntered(MouseEvent e) {
                backLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
            }
            //unbold on exit
            @Override
            public void mouseExited(MouseEvent e) {
                backLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 40));
            }
            //change labels on click
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
        howToPlayArea.setSize(1, 1); //will expand to size
        howToPlayArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        howToPlayArea.setForeground(new Color(1f, 1f, 1f, 0.8f));
        howToPlayArea.setBackground(new Color(56, 49, 44));
        howToPlayArea.setText(
        "PROFISCENDUM\n" +
        "\n" +
        "You are in charge of protecting the small fortified settlement of Oppidum " +
        "(well, not as much in charge of as the person who was roped into the position on account of their debt). " +
        "However, two large armies are fighting each other near you, which ends up in them both fighting to control your settlement. " +
        "You are an inferior, unexperienced fighter who can only reliably use a bow and arrow. " +
        "Your settlement is guaranteed to go down in this battle... but little known to the innocent civilians around you, " +
        "you have an escape dragon that can take you and whoever else you want to safety. " +
        "If only you were a good enough fighter to save them all, you wouldn’t have had to leave so many of them behind to die. " +
        "But alas... when you think you’re gaining their trust, you might accidentally set off a bomb, or pop out a baby, or " +
        "call the wrong dragon to save you. Is it even worth saving anyone else? How badly will things go before you escape?\n" +
        "\n" +
        "\n" +
        "\n" +
        "CONTROLS\n" +
        "\n" +
        "← → : Move in the left or right direction.\n" +
        "↑ : Jump or climb a ladder.\n" +
        "↓ : Crouch or descend a ladder.\n" +
        "SPACE: Shoot your bow and arrow in whatever direction you're facing.\n" +
        "ENTER: Call your escape dragon, which will arrive one minute after you call them and depart 30 seconds after that.\n" +
        "\n" +
        "As for the rest of your abilities, each one can be called by a different key on your keyboard (A-Z). " +
        "However, they change positions between every game. You have no idea which one you're using when you press a key. " +
        "A short description will only show up once you press the key. For your sake, here are longer descriptions of each.\n" +
        "\n" +
        "Open and close doors: This toggles the state of random doors. Weaker doors are more likely to flip than stronger doors. " +
        "(The probability of a door flipping is 1/its current strength.)\n" +
        "\n");

        howToPlayScroll = new JScrollPane(howToPlayArea);
        howToPlayScroll.setBorder(null);
        howToPlayScroll.setBackground(new Color(56, 49, 44));
        howToPlayScroll.getVerticalScrollBar().setUnitIncrement(3);
        //TODO: Make scroll bar dark

        mainPanel.add(startPanel, JLayeredPane.MODAL_LAYER, Integer.valueOf(0));
    }

    private void setupCharacters() {
        //TODO: setup characters for new game
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
        input = KeyEvent.getKeyText(e.getKeyCode()).toLowerCase();
        switch (input) {
            case "␣":
            case "space":
                break;
            case "↑":
            case "up":
                break;
            case "↓":
            case "down":
                break;
            case "→":
            case "right":
                break;
            case "←":
            case "left":
                break;
            default:
            //if-else instead switch for keyboard
            if (input.equals(alphabet[0])) { //open random doors
                randomLetterLabel.setText("Key " + input.toUpperCase() + " has caused some doors to open and close.");

                Door door;
                for(int i = 0; i < mainPanel.getComponentCount(); i++) {
                    for(int j = 0; j < ((Container) mainPanel.getComponent(i)).getComponentCount(); j++) {
                        if (((Container) mainPanel.getComponent(i)).getComponent(j).getClass() == Door.class) {
                            //randomly open or close based on strength
                            door = (Door) ((Container) mainPanel.getComponent(i)).getComponent(j);
                            if (rand.nextDouble() < 1d/door.getStrength()) {
                                //toggle
                                door.setDoor(!leftTower.rightDoor.isOpen());
                                Rectangle bounds = getRectangleRelativeTo(door, mainPanel);
                                bounds.width = Math.max(door.maxStrength, 15);
                                mainPanel.repaint(bounds);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

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

        final double GAME_HERTZ = 15.0; //game updates/sec
        //Calculate how many ns each frame should take for our target game hertz.
        final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
        //At the very most we will update the game this many times before a new render.
        final int MAX_UPDATES_BEFORE_RENDER = 5;

        double lastUpdateTime = System.nanoTime();
        double lastRenderTime = System.nanoTime();

        //If we are able to get as high as this FPS, don't render again.
        final double TARGET_FPS = 30;
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
        //
    }

    /** Renders each difference made. */
    private void drawGame(float interpolation) {
        // gamePanel.setInterpolation(interpolation);
        // gamePanel.repaint();
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
    private Rectangle getRectangleRelativeTo(Container container, Container relativeTo) {
        Point smallP = container.getLocationOnScreen();
        Point largeP = relativeTo.getLocationOnScreen();
        return new Rectangle(smallP.x - largeP.x, smallP.y - largeP.y, container.getWidth(), container.getHeight());
    }
}