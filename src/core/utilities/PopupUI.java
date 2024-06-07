package core.utilities;

import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import core.window.GameComponent;

public abstract class PopupUI extends GameComponent{
    BufferedImage image;
    public PopupUI(int width, int height) {
        super(width, height);
        image = getNotebookBackground();
    }
    
    public void drawComponent(Graphics2D g2d) {
        g2d.drawImage(image, 0, 0, null);
    }

    public BufferedImage getNotebookBackground () {
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
}
