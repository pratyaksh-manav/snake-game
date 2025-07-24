import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private final int BOARD_WIDTH = 600;
    private final int BOARD_HEIGHT = 600;
    private final int UNIT_SIZE = 25;
    private final int GAME_UNITS = (BOARD_WIDTH * BOARD_HEIGHT) / UNIT_SIZE;
    private final int DELAY = 100;

    private ArrayList<Point> snake;
    private Point food;
    private char direction = 'R'; // R-Right, L-Left, U-Up, D-Down
    private boolean running = false;
    private Timer timer;
    private Random random;
    private int score = 0;

    public SnakeGame() {
        random = new Random();
        this.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        startGame();
    }

    public void startGame() {
        snake = new ArrayList<>();
        snake.add(new Point(0, 0));
        newFood();
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
            // Draw food
            g.setColor(Color.RED);
            g.fillOval(food.x, food.y, UNIT_SIZE, UNIT_SIZE);

            // Draw snake
            for (int i = 0; i < snake.size(); i++) {
                if (i == 0) {
                    // Head of snake
                    g.setColor(Color.GREEN);
                } else {
                    // Body of snake
                    g.setColor(new Color(45, 180, 0));
                }
                g.fillRect(snake.get(i).x, snake.get(i).y, UNIT_SIZE, UNIT_SIZE);
            }

            // Draw score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + score, (BOARD_WIDTH - metrics.stringWidth("Score: " + score)) / 2, 
                        g.getFont().getSize());

            // Draw controls
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.drawString("Controls: WASD to move", 10, BOARD_HEIGHT - 20);
        } else {
            gameOver(g);
        }
    }

    public void newFood() {
        food = new Point(random.nextInt(BOARD_WIDTH / UNIT_SIZE) * UNIT_SIZE,
                        random.nextInt(BOARD_HEIGHT / UNIT_SIZE) * UNIT_SIZE);
    }

    public void move() {
        Point newHead = new Point(snake.get(0));

        switch (direction) {
            case 'U':
                newHead.y -= UNIT_SIZE;
                break;
            case 'D':
                newHead.y += UNIT_SIZE;
                break;
            case 'L':
                newHead.x -= UNIT_SIZE;
                break;
            case 'R':
                newHead.x += UNIT_SIZE;
                break;
        }

        snake.add(0, newHead);

        // Check if food eaten
        if (newHead.equals(food)) {
            score++;
            newFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    public void checkFood() {
        // This is handled in the move() method
    }

    public void checkCollisions() {
        // Check if head collides with body
        Point head = snake.get(0);
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                running = false;
            }
        }

        // Check if head touches left border
        if (head.x < 0) {
            running = false;
        }

        // Check if head touches right border
        if (head.x >= BOARD_WIDTH) {
            running = false;
        }

        // Check if head touches top border
        if (head.y < 0) {
            running = false;
        }

        // Check if head touches bottom border
        if (head.y >= BOARD_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        // Score
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + score, (BOARD_WIDTH - metrics1.stringWidth("Score: " + score)) / 2, 
                    g.getFont().getSize());

        // Game Over text
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (BOARD_WIDTH - metrics2.stringWidth("Game Over")) / 2, 
                    BOARD_HEIGHT / 2);

        // Restart instruction
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Press SPACE to restart", (BOARD_WIDTH - metrics3.stringWidth("Press SPACE to restart")) / 2, 
                    BOARD_HEIGHT / 2 + 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFood();
            checkCollisions();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                if (direction != 'R') {
                    direction = 'L';
                }
                break;
            case KeyEvent.VK_D:
                if (direction != 'L') {
                    direction = 'R';
                }
                break;
            case KeyEvent.VK_W:
                if (direction != 'D') {
                    direction = 'U';
                }
                break;
            case KeyEvent.VK_S:
                if (direction != 'U') {
                    direction = 'D';
                }
                break;
            case KeyEvent.VK_SPACE:
                if (!running) {
                    score = 0;
                    startGame();
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game - WASD Controls");
        SnakeGame game = new SnakeGame();

        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 630);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
