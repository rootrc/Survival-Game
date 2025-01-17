package core;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.ToolTipManager;

import core.dungeon.Dungeon;
import core.dungeon.settings.DiffSettings;
import core.game_components.GamePanel;
import core.game_components.UILayer;
import core.game_panel.Credits;
import core.game_panel.Menu;
import core.game_panel.Options;
import core.game_panel.Rules;

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
    private Rules rules;
    private Credits credits;

    private UILayer UILayer;

    private Thread gameThread;

    public Game() {
        DiffSettings.setEasy();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Survival Game");
        initPanels();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        ToolTipManager.sharedInstance().setInitialDelay(250);
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void initPanels() {
        UILayer = new UILayer();
        dungeon = new Dungeon(this, UILayer);
        menu = new Menu(this, UILayer);
        options = new Options(this, UILayer);
        rules = new Rules(this, UILayer);
        credits = new Credits(this, UILayer);
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
                // this is absolutely horrible design but I just can't be bothered anymore
                gamePanel.fadeOut(16);
                new Thread(new Runnable() {
                    public void run() {
                        long startTime = System.nanoTime();
                        while (true) {
                            long currentTime = System.nanoTime();
                            if ((currentTime - startTime) / 1000000000.0 < 0.25) {
                                continue;
                            }
                            remove(gamePanel);
                            if (str.equals("dungeon")) {
                                gamePanel = dungeon;
                                dungeon.reset();
                            } else if (str.equals("mainMenu")) {
                                gamePanel = menu;
                            } else if (str.equals("options")) {
                                gamePanel = options;
                            } else if (str.equals("rules")) {
                                gamePanel = rules;
                            } else if (str.equals("credits")) {
                                gamePanel = credits;
                            }
                            add(gamePanel);
                            gamePanel.fadeIn();
                            gamePanel.add(UILayer);
                            revalidate();
                            return;
                        }
                    }
                }).start();
            }
        };
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