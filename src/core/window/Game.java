package core.window;

import javax.swing.JFrame;

import core.dungeon.Dungeon;

public class Game extends JFrame implements Runnable {
    public final int FPS = 60;
    public final int UPS = 60;

    public final static boolean DEBUG = false;

    private GamePanel gamePanel;
    private Thread gameThread;

    public Game() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Game");
        
        gamePanel = new Dungeon();
        add(gamePanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        startGameThread();
    }

    private void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta1 = 0;
        long lastTime1 = System.nanoTime();
        long currentTime1;

        double updateInterval = 1000000000 / UPS;
        double delta2 = 0;
        long lastTime2 = System.nanoTime();
        long currentTime2;

        while (gameThread != null) {
            currentTime1 = System.nanoTime();
            delta1 += (currentTime1 - lastTime1) / drawInterval;
            lastTime1 = currentTime1;
            if (delta1 > 1) {
                gamePanel.repaint();
                delta1--;
            }
            currentTime2 = System.nanoTime();
            delta2 += (currentTime2 - lastTime2) / updateInterval;
            lastTime2 = currentTime2;
            if (delta2 > 1) {
                gamePanel.update();
                delta2--;
            }
        }
    }
}