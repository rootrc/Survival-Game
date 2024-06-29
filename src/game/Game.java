package game;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;

import game.dungeon.Dungeon;
import game.game_components.GamePanel;
import game.game_panel.Menu;

public class Game extends JFrame implements Runnable {
    public final static int screenWidth = Dungeon.TILESIZE * Dungeon.maxScreenCol; // 1024 pixels
    public final static int screenHeight = Dungeon.TILESIZE * Dungeon.maxScreenRow; // 768 pixels

    private static final int FPS = 60;
    private static final int UPS = 60;

    public static final boolean DEBUG = false;

    private GamePanel gamePanel;
    private Dungeon dungeon;
    private Menu menu;

    private Thread gameThread;

    // private final Action changePanel = new AbstractAction() {
    // public void actionPerformed(ActionEvent e) {
    // if (e.getActionCommand().equals("dungeon")) {
    // changePanel(dungeon);
    // } else if (e.getActionCommand().equals("options")) {
    // // TODO
    // } else if (e.getActionCommand().equals("mainMenu")) {
    // changePanel(menu);
    // dungeon.reset();
    // } else if (e.getActionCommand().equals("mainMenu")) {
    // // TODO
    // }
    // }
    // };

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
        dungeon = new Dungeon(this);
        menu = new Menu(this);
        gamePanel = menu;
        add(gamePanel);
    }

    public Action changePanel(String str) {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                remove(gamePanel);
                if (str.equals("dungeon")) {
                    gamePanel = dungeon;
                } else if (str.equals("options")) {
                    // TODO
                } else if (str.equals("mainMenu")) {
                    gamePanel = menu;
                } else if (str.equals("title")) {
                    // TODO
                }
                add(gamePanel);
                gamePanel.fadeIn();
                revalidate();
            }
        };
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