package game;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;

import game.dungeon.Dungeon;
import game.dungeon.UILayer;
import game.game_components.GameComponent;
import game.game_components.GamePanel;
import game.game_components.UIButton;
import game.game_panel.Menu;

public class Game extends JFrame implements Runnable {
    private static final int FPS = 60;
    private static final int UPS = 60;

    public static final boolean DEBUG = false;

    private GamePanel gamePanel;
    private Dungeon dungeon;
    private Menu menu;
    
    private static UILayer UI;

    private Thread gameThread;

    final Action changePanel = new AbstractAction() {
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
        dungeon = new Dungeon(changePanel);
        menu = new Menu(changePanel);
        UI = new UILayer();
        gamePanel = menu;
        add(UI);
        add(gamePanel);
    }

    public static UILayer getUI() {
        return UI;
    }
    public static void addUI(GameComponent gameComponent) {
        UI.add(gameComponent);
    }

    public static void removeUI(GameComponent gameComponent) {
        UI.remove(gameComponent);
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