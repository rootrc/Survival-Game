package core;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.ToolTipManager;

import core.dungeon.Dungeon;
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

    public static final boolean TEST = false;
    public static boolean LIGHTING = true;
    public static boolean HITBOXES = false;

    public static FloatControl audioVolume;
    public static double screenShakeDurationMulti;
    public static double screenShakeStrengthMulti;

    private GamePanel gamePanel;
    private Dungeon dungeon;
    private Menu menu;
    private Options options;
    private Rules rules;
    private Credits credits;

    private UILayer UILayer;

    private Thread gameThread;

    public Game() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Survival Game");
        initMusic();
        initPanels();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        ToolTipManager.sharedInstance().setInitialDelay(250);
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void initMusic() {
        try {
            AudioInputStream sound = AudioSystem.getAudioInputStream(new File("res/audio/bgm.wav"));
            Clip background = AudioSystem.getClip();
            background.open(sound);
            audioVolume = (FloatControl) background.getControl(FloatControl.Type.MASTER_GAIN);
            background.setFramePosition(0);
            background.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    private void initPanels() {
        UILayer = new UILayer();
        dungeon = new Dungeon(this, UILayer);
        menu = new Menu(this, UILayer);
        options = new Options(this, UILayer);
        rules = new Rules(this, UILayer);
        credits = new Credits(this, UILayer);
        gamePanel = menu;
        if (Game.TEST) {
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