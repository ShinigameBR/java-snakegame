import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int applesEaten;
    int appleX;
    int appleY;
    int snakeBodyParts = 6;
    char snakeDirection = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            // Draw grid
            // for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            // g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            // g.drawLine(0, i * UNIT_SIZE, SCREEN_HEIGHT, i * UNIT_SIZE);
            // }

            // Draw apple
            g.setColor(Color.RED);
            g.fillRect(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // Draw snake
            for (int i = 0; i < snakeBodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            // Draw score
            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free", Font.BOLD, UNIT_SIZE));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString(Integer.toString(applesEaten),
                    SCREEN_WIDTH - (UNIT_SIZE - metrics.stringWidth(Integer.toString(applesEaten))
                            + (UNIT_SIZE - metrics.stringWidth(Integer.toString(applesEaten)))),
                    (metrics.getHeight() / 2) + UNIT_SIZE / 4);

        } else {
            gameOver(g);
        }

    }

    public void move() {
        for (int i = snakeBodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (snakeDirection) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            snakeBodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        // Check if self collided
        for (int i = snakeBodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        // Check if left border collided
        if (x[0] < 0) {
            running = false;
        }
        // Check if right border collided
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        // Check if top border collided
        if (y[0] < 0) {
            running = false;
        }
        // Check if bottom border collided
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("You Died", (SCREEN_WIDTH - metrics.stringWidth("You Died")) / 2, SCREEN_HEIGHT / 2);
        g.drawString(Integer.toString(applesEaten),
                (SCREEN_WIDTH - metrics.stringWidth(Integer.toString(applesEaten))) / 2,
                (SCREEN_HEIGHT / 2) + 75);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (snakeDirection != 'R') {
                        snakeDirection = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (snakeDirection != 'L') {
                        snakeDirection = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (snakeDirection != 'D') {
                        snakeDirection = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (snakeDirection != 'U') {
                        snakeDirection = 'D';
                    }
                    break;
            }
        }
    }

}
