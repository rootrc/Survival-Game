package game.game_components;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import game.Game;
import game.dungeon.UILayer;
import game.utilities.ImageUtilities;

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
        setLocation((Game.screenWidth - getWidth()) / 2, (Game.screenHeight - getHeight()) / 2);
        buildImage(tileSet);
        getInputMap(2).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close");
        getInputMap(2).put(KeyStroke.getKeyStroke("pressed Z"), "close");
        getActionMap().put("close", close);
    }

    public PopupUI(UILayer UILayer, int width, int height, int framesToEnter) {
        this(UILayer, width, height, framesToEnter, "Notebook");
    }

    public void drawComponent(Graphics2D g2d) {
        g2d.drawImage(image, 0, 0, null);
    }

    private void buildImage(String tileSet) {
        image = ImageUtilities.getImageFrom3x3Tileset("UI", new StringBuilder(tileSet).append("Tileset").toString(),
                getWidth(), getHeight());
    }

    public void update() {
        if (!moving) {
            return;
        }
        if (getX() < (Game.screenWidth - getWidth()) / 2) {
            setX(-getWidth() + (Game.screenWidth + getWidth()) / 2 * easeOutQuad((double) timer / framesToEnter));
            timer++;
        } else {
            setX((Game.screenWidth - getWidth()) / 2
                    + (Game.screenWidth + getWidth()) / 2 * easeInQuad((double) timer / framesToEnter));
            timer++;
        }
        if (getX() == (Game.screenWidth - getWidth()) / 2) {
            moving = false;
            timer = 1;
        } else if (getX() == Game.screenWidth) {
            moving = false;
            timer = 1;
            UILayer.remove(this);
        }
    }

    private double easeOutQuad(double x) {
        return 1 - (1 - x) * (1 - x);
    }

    private double easeInQuad(double x) {
        return x * x;
    }

    public void enterPanel() {
        moving = true;
        setLocation(-getWidth(), getY());
    }

    public void exitPanel() {
        moving = true;
    }

    protected final Action close = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            exitPanel();
            UILayer.removeConfirmUI().actionPerformed(e);;
        }
    };

    protected final BufferedImage getImage() {
        return image;
    }
}
