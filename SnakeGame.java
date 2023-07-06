
                                           // Task First on SYNC Interns.

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener {
    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;
    private static final int UNIT_SIZE = 20;
    private static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private static final int DELAY = 100;
    private final int[] snakeX = new int[GAME_UNITS];
    private final int[] snakeY = new int[GAME_UNITS];
    private int snakeLength = 3;
    private int foodX;
    private int foodY;
    private char direction = 'R';
    private boolean running = false;
    private boolean gameOver = false;
    private Timer timer;
    private final Random random;
    private final JButton startButton;

    public SnakeGame() {
        random = new Random();
        setTitle("Snake Game");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());  // Use GridBagLayout for flexible positioning

        startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(90, 40));
        startButton.setBackground(Color.LIGHT_GRAY);//Set Colour on Start Button.
        startButton.addActionListener(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.CENTER;  // Center the button
        contentPanel.add(startButton, gbc);
        add(contentPanel);
        addKeyListener(new MyKeyAdapter());
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void startGame() {
        newFood();
        running = true;
        gameOver = false;
        // Set initial positions of snake segments
        snakeX[0] = UNIT_SIZE * 5; // Starting position for the head of the snake on the x-axis
        snakeY[0] = UNIT_SIZE * 5; // Starting position for the head of the snake on the y-axis
        startButton.setVisible(false); // Hide the Start button
        requestFocus();
        timer = new Timer(DELAY, this);
        timer.start();
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        // Make  a background
        g.setColor(Color.black);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        if (gameOver) {
            // Write a game over message
            g.setColor(Color.red);
            g.setFont(new Font("Times New Roman", Font.BOLD, 50));
            FontMetrics metrics = getFontMetrics(g.getFont());
            String gameOverMessage = "Game Over!";
            g.drawString(gameOverMessage, (SCREEN_WIDTH - metrics.stringWidth(gameOverMessage))/ 2, SCREEN_HEIGHT   / 2);
            return;
        }

        // Make a snake
        for (int i = 0; i < snakeLength; i++) {
            if (i == 0) {
                g.setColor(Color.white);
            } else {
                g.setColor(Color.red.darker());
            }
            g.fillRect(snakeX[i], snakeY[i], UNIT_SIZE, UNIT_SIZE);
        }

        // Make a food
        g.setColor(Color.GREEN);
        g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);
    }

    private void move() {
        for (int i = snakeLength-1; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        switch (direction) {
            case 'U' -> snakeY[0] = snakeY[0] - UNIT_SIZE;
            case 'D' -> snakeY[0] = snakeY[0] + UNIT_SIZE;
            case 'L' -> snakeX[0] = snakeX[0] - UNIT_SIZE;
            case 'R' -> snakeX[0] = snakeX[0] + UNIT_SIZE;
        }
    }

    private void checkFood() {
        if (snakeX[0] == foodX && snakeY[0] == foodY) {
            snakeLength++;
            newFood();

        }
    }

    private void checkCollision() {
        // Check if snake hits the perimeter
        if (snakeX[0] < 0 || snakeY[0] < 0 || snakeX[0] >= SCREEN_WIDTH || snakeY[0] >= SCREEN_HEIGHT) {
            running = false;
            gameOver = true;

        }

        // Check if snake hits itself
        for (int i = snakeLength - 1; i > 0; i--) {
            if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                running = false;
                gameOver = true;
                break;
            }
        }

        if (!running) {
            timer.stop();
        }
    }

    private void newFood() {
        boolean validFood = false;
        while (!validFood) {
            foodX = random.nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            foodY = random.nextInt((SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

            // new food position overlaps with the snake's body
            boolean overlap = false;
            for (int i = 0; i < snakeLength; i++) {
                if (snakeX[i] == foodX && snakeY[i] == foodY) {
                    overlap = true;
                    break;
                }
            }

            // new food position is inside the screen boundaries
            if (foodX > SCREEN_WIDTH - UNIT_SIZE || foodY > SCREEN_HEIGHT - UNIT_SIZE) {
               overlap = true;
            }

            if (!overlap) {
                validFood = true;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            startGame();
        }

        if (running) {
            move();
            checkFood();
            checkCollision();
        }
        repaint();
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP -> {
                    if (direction != 'D') {
                        direction = 'U';
                    }
                }
                case KeyEvent.VK_DOWN -> {
                    if (direction != 'U') {
                        direction = 'D';
                    }
                }
                case KeyEvent.VK_LEFT -> {
                    if (direction != 'R') {
                        direction = 'L';
                    }
                }
                case KeyEvent.VK_RIGHT -> {
                    if (direction != 'L') {
                        direction = 'R';
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new SnakeGame();
    }
}
