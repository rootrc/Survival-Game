package game.game_components;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Action;

import game.Game;
import game.dungeon.settings.KeyBinds;
import game.utilities.AnimationUtilities;
import game.utilities.ImageUtilities;

// A UI panel that appears and disappears
public abstract class PopupUI extends GameComponent {
    private UILayer UILayer;
    private BufferedImage backgroundImage;
    private int framesToEnter;
    private boolean moving;
    private int timer;

    public PopupUI(UILayer UILayer, int width, int height, int framesToEnter, String tileSet) {
        super(width, height);
        this.UILayer = UILayer;
        this.framesToEnter = framesToEnter;
        setLocation((Game.screenWidth - getWidth()) / 2, (Game.screenHeight - getHeight()) / 2);
        buildImage(tileSet);
        getInputMap(2).put(KeyBinds.escape, "close");
        getActionMap().put("close", close);
    }

    public PopupUI(UILayer UILayer, int width, int height, int framesToEnter) {
        this(UILayer, width, height, framesToEnter, "Notebook");
    }

    public void update() {
        if (!moving) {
            return;
        }
        if (getX() < (Game.screenWidth - getWidth()) / 2) {
            setX(-getWidth() + (Game.screenWidth + getWidth()) / 2 * AnimationUtilities.easeOutQuad((double) timer / framesToEnter));
            timer++;
        } else {
            setX((Game.screenWidth - getWidth()) / 2
                    + (Game.screenWidth + getWidth()) / 2 * AnimationUtilities.easeInQuad((double) timer / framesToEnter));
            timer++;
        }
        if (getX() == (Game.screenWidth - getWidth()) / 2) {
            moving = false;
            timer = 1;
        } else if (getX() == Game.screenWidth) {
            UILayer.remove(this);
        }
    }

    public void drawComponent(Graphics2D g2d) {
        g2d.drawImage(backgroundImage, 0, 0, null);
    }

    private void buildImage(String tileSet) {
        backgroundImage = ImageUtilities.getImageFrom3x3Tileset("UI", new StringBuilder(tileSet).append("Tileset").toString(),
                getWidth(), getHeight());
    }

    public void enterPanel() {
        UILayer.add(this);
        moving = true;
        timer = 1;
        setLocation(-getWidth(), getY());
    }

    public void exitPanel() {
        moving = true;
    }

    protected final Action close = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            exitPanel();
        }
    };
}
