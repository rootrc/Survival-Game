package core.game_components;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Action;

import core.Game;
import core.dungeon.settings.KeyBinds;
import core.utilities.Easing;
import core.utilities.ImageUtilities;

// A UI panel that appears and disappears
public abstract class PopupUI extends GameComponent {
    private UILayer UILayer;
    private BufferedImage image;

    private int framesToEnter;
    private boolean moving;
    private int timer;

    public PopupUI(UILayer UILayer, int width, int height, int framesToEnter, String tileSet) {
        super(width, height);
        this.UILayer = UILayer;
        this.framesToEnter = framesToEnter;
        setLocation((Game.SCREEN_WIDTH - getWidth()) / 2, (Game.SCREEN_HEIGHT - getHeight()) / 2);
        buildImage(tileSet);
        getInputMap(2).put(KeyBinds.ESC, "close");
        getActionMap().put("close", close);
    }

    public PopupUI(UILayer UILayer, int width, int height, int framesToEnter) {
        this(UILayer, width, height, framesToEnter, "Notebook");
    }

    public void update() {
        if (!moving) {
            return;
        }
        if (getX() < (Game.SCREEN_WIDTH - getWidth()) / 2) {
            setX(-getWidth()
                    + (Game.SCREEN_WIDTH + getWidth()) / 2 * Easing.easeInOutQuad((double) timer / framesToEnter));
            timer++;
        } else {
            setX((Game.SCREEN_WIDTH - getWidth()) / 2
                    + (Game.SCREEN_WIDTH + getWidth()) / 2 * Easing.easeInOutQuad((double) timer / framesToEnter));
            timer++;
        }
        if (getX() == (Game.SCREEN_WIDTH - getWidth()) / 2) {
            moving = false;
            timer = 1;
        } else if (getX() == Game.SCREEN_WIDTH) {
            UILayer.remove(this);
        }
    }

    public void drawComponent(Graphics2D g2d) {
        g2d.drawImage(image, 0, 0, null);
    }

    private void buildImage(String tileSet) {
        image = ImageUtilities.getImageFrom3x3Tileset("UI", new StringBuilder(tileSet).append("Tileset").toString(),
                getWidth(), getHeight());
    }

    public void setImage(BufferedImage image) {
        this.image = image;
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
