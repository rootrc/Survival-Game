package game.utilities.game_components;

import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.Action;

import game.utilities.ImageUtilities;

public abstract class PopupUI extends GameComponent {
    private Action removeRoomUI;
    private BufferedImage image;
    private int framesToEnter;
    private boolean moving;

    public PopupUI(int width, int height, Action removeRoomUI, int framesToEnter) {
        super(width, height);
        this.removeRoomUI = removeRoomUI;
        this.framesToEnter = framesToEnter;
        setLocation((GamePanel.screenWidth - getWidth()) / 2, (GamePanel.screenHeight - getHeight()) / 2);
        image = getNotebookBackground();
    }

    public void drawComponent(Graphics2D g2d) {
        g2d.drawImage(image, 0, 0, null);
    }

    private BufferedImage getNotebookBackground() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        BufferedImage image = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(),
                Transparency.BITMASK);
        Graphics2D g2d = image.createGraphics();
        final int rows = getWidth() / 32;
        final int cols = getHeight() / 32;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int r = 1;
                int c = 1;
                if (i == 0) {
                    c--;
                } else if (i == rows - 1) {
                    c++;
                }
                if (j == 0) {
                    r--;
                } else if (j == cols - 1) {
                    r++;
                }
                g2d.drawImage(ImageUtilities.getImage("UI", "NotebookTileset", r, c, 2), 32 * i, 32 * j, null);
            }
        }
        return image;
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
            close();
        }
    }

    public void enter() {
        moving = true;
        setLocation(-getWidth(), getY());
    }

    public void exit() {
        moving = true;
    }

    protected void close() {
        removeRoomUI.actionPerformed(
                new ActionEvent(this, 0, getClass().toString().substring(getClass().toString().lastIndexOf('.') + 1)));
    }
}
