package game.game_components;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import game.Game;
import game.utilities.ActionUtilities;
import game.utilities.ImageUtilities;

public abstract class PopupUI extends GameComponent {
    private BufferedImage image;
    private int framesToEnter;
    private boolean moving;

    public PopupUI(int width, int height, int framesToEnter, String tileSet) {
        super(width, height);
        this.framesToEnter = framesToEnter;
        setLocation((Game.screenWidth - getWidth()) / 2, (Game.screenHeight - getHeight()) / 2);
        buildImage(tileSet);
        getInputMap(2).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close");
        getInputMap(2).put(KeyStroke.getKeyStroke("pressed Z"), "close");
        setEscapeExits(true);
    }

    public PopupUI(int width, int height, int framesToEnter) {
        this(width, height, framesToEnter, "Notebook");
    }

    public void drawComponent(Graphics2D g2d) {
        g2d.drawImage(image, 0, 0, null);
    }

    public void update() {
        if (!moving) {
            return;
        }
        moveX((Game.screenWidth + getWidth()) / (2 * framesToEnter));
        if (getX() == (Game.screenWidth - getWidth()) / 2) {
            moving = false;
        }
        if (getX() == Game.screenWidth) {
            moving = false;
            remove();
        }
    }

    public void buildImage(String tileSet) {
        image = ImageUtilities.getImageFrom3x3Tileset("UI", new StringBuilder(tileSet).append("Tileset").toString(), getWidth(), getHeight());
    }

    public void enterPanel() {
        moving = true;
        setLocation(-getWidth(), getY());
    }

    public void exitPanel() {
        moving = true;
    }

    protected void remove() {
        ActionUtilities.closePopupUI(this);
    }

    public final Action close = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            exitPanel();
            ActionUtilities.removeConfirmUI();
        }
    };

    public void setEscapeExits(boolean bool) {
        if (bool) {
            getActionMap().put("close", close);
        } else {
            getActionMap().clear();
        }
    }

    protected BufferedImage getImage() {
        return image;
    }
}
