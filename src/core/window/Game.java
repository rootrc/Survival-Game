package core.window;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;

import core.game_panels.Menu;
import core.game_panels.dungeon.Dungeon;

public class Game extends JFrame implements Runnable {
    private static final int FPS = 60;
    private static final int UPS = 60;

    public static final boolean DEBUG = false;

    private GamePanel gamePanel;
    private Dungeon dungeon;
    private Menu menu;

    private Thread gameThread;

    public Game() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Game");
        initPanel();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        startGameThread();
    }

    private void initPanel() {

        Action changePanel = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("dungeon")) {
                    changePanel(dungeon);
                } else if (e.getActionCommand().equals("options")) {
                    // TODO
                } else if (e.getActionCommand().equals("mainMenu")) {
                    changePanel(menu);
                    dungeon.restart();
                } else if (e.getActionCommand().equals("mainMenu")) {
                    // TODO
                }
            }
        };

        dungeon = new Dungeon(changePanel);
        menu = new Menu(dungeon, changePanel);
        gamePanel = menu;
        add(gamePanel);
    }

    private void changePanel(GamePanel gamePanel) {
        remove(this.gamePanel);
        this.gamePanel = gamePanel;
        add(gamePanel);
        revalidate();
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
                gamePanel.updateComponent();
                delta2--;
            }
        }
    }
}