package game;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;

import game.dungeon.Dungeon;
import game.dungeon.settings.DiffSettings;
import game.game_components.GamePanel;
import game.game_components.UILayer;
import game.game_panel.Menu;
import game.game_panel.Options;

public class Game extends JFrame implements Runnable {
    public static final int SCREEN_WIDTH = 1024;
    public static final int SCREEN_HEIGHT = 768;

    public static final int FPS = 60;
    public static final int UPS = 60;

    public static final boolean DEBUG = false;
    public static final boolean LIGHTING = true;

    private GamePanel gamePanel;
    private Dungeon dungeon;
    private Menu menu;
    private Options options;

    private UILayer UILayer;

    private Thread gameThread;

    public Game() {
        DiffSettings.setEasy();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Survival Game");
        initPanel();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        startGameThread();
    }

    private void initPanel() {
        UILayer = new UILayer();
        dungeon = new Dungeon(this, UILayer);
        menu = new Menu(this, UILayer);
        options = new Options(this, UILayer);
        gamePanel = menu;
        if (Game.DEBUG) {
            gamePanel = dungeon;
        }
        add(gamePanel);
        gamePanel.fadeIn();
        gamePanel.add(UILayer);
    }

    public Action changePanel(String str) {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                remove(gamePanel);
                if (str.equals("dungeon")) {
                    gamePanel = dungeon;
                } else if (str.equals("options")) {
                    gamePanel = options;
                } else if (str.equals("mainMenu")) {
                    gamePanel = menu;
                } else if (str.equals("title")) {
                    // TODO
                }
                add(gamePanel);
                gamePanel.fadeIn();
                gamePanel.add(UILayer);
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
        double drawInterval = 1000000000.0 / FPS;
        double updateInterval = 1000000000.0 / UPS;
        double delta1 = 0;
        double delta2 = 0;
        long currentTime;
        long lastTime = System.nanoTime();

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta1 += (currentTime - lastTime) / drawInterval;
            delta2 += (currentTime - lastTime) / updateInterval;
            lastTime = currentTime;
            if (delta1 > 1) {
                if (freezeFrameCnt == 0) {
                    gamePanel.repaint();
                }
                delta1--;
            }
            if (delta2 > 1) {
                if (freezeFrameCnt == 0) {
                    gamePanel.updateComponent();
                } else {
                    freezeFrameCnt--;
                }
                delta2--;
            }
        }
    }

    private static int freezeFrameCnt;

    public static void setFreezeFrame(int freezeFrame) {
        freezeFrameCnt = freezeFrame;
    }
}