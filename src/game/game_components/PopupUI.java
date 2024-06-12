package game.game_components;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import game.utilities.ActionUtilities;
import game.utilities.ImageUtilities;

public abstract class PopupUI extends GameComponent {
    private BufferedImage image;
    private Action removeRoomUI;
    private Action removeConfirmUI;
    private int framesToEnter;
    private boolean moving;

    public PopupUI(GamePanel gamePanel, int width, int height, int framesToEnter, String tileSet) {
        super(width, height);
        removeRoomUI = ActionUtilities.closePopupUI(gamePanel, this);
        removeConfirmUI = ActionUtilities.removeConfirmUI(gamePanel);
        this.framesToEnter = framesToEnter;
        setLocation((GamePanel.screenWidth - getWidth()) / 2, (GamePanel.screenHeight - getHeight()) / 2);
        buildImage(tileSet);
        getInputMap(2).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close");
        getInputMap(2).put(KeyStroke.getKeyStroke("pressed Z"), "close");
        setEscapeExits(true);
    }

    public PopupUI(GamePanel gamePanel, int width, int height, int framesToEnter) {
        this(gamePanel, width, height, framesToEnter, "Notebook");
    }

    public void drawComponent(Graphics2D g2d) {
        g2d.drawImage(image, 0, 0, null);
    }

    public void update() {
        if (!moving) {
            return;
        }
        moveX((GamePanel.screenWidth + getWidth()) / (2 * framesToEnter));
        if (getX() == (GamePanel.screenWidth - getWidth()) / 2) {
            moving = false;
        }
        if (getX() == GamePanel.screenWidth) {
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
        removeRoomUI.actionPerformed(null);
    }

    public final Action close = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            exitPanel();
            removeConfirmUI.actionPerformed(e);
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
