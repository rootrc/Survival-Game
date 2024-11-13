package core.game_components;

import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.Action;
import javax.swing.ImageIcon;

import core.utilities.ImageUtilities;

// Custom UIButton that is used for UI
public class UIButton extends GameButton {

    public UIButton(Action action, Rectangle rect, BufferedImage text) {
        super(action, rect);
        setIcon(createImageIcon(text, 0));
        setRolloverIcon(createImageIcon(text, 1));
        setPressedIcon(createImageIcon(text, 2));
    }

    // creates ImageIcons for various button states with background and text
    private ImageIcon createImageIcon(BufferedImage text, int id) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        BufferedImage image = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(),
                Transparency.BITMASK);
        if (text == null) {
            return new ImageIcon(image);
        }
        Graphics2D g2d = image.createGraphics();
        g2d.drawImage(buildBackgroundImage(id), 0, 0, null);
        if (id != 2) {
            g2d.drawImage(text, (getWidth() - text.getWidth()) / 2, (getHeight() - text.getHeight()) / 2, null);
        } else {
            g2d.drawImage(text, (getWidth() - text.getWidth()) / 2, (getHeight() - text.getHeight()) / 2 + 2, null);
        }
        return new ImageIcon(image);
    }

    // Builds background with tileset
    private BufferedImage buildBackgroundImage(int id) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        BufferedImage image = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(),
                Transparency.BITMASK);
        Graphics2D g2d = image.createGraphics();
        int rows = getWidth() / ImageUtilities.default3x3TilesetScale;
        int cols = getHeight() / ImageUtilities.default3x3TilesetScale;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int r = 1;
                int c = 1;
                if (cols == 1) {
                    r = 4;
                }
                if (rows == 1) {
                    c = 4;
                }
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
                g2d.drawImage(ImageUtilities.getImage("UI", "ButtonTileSet", r, c + 4 * id, 32, 32),
                        ImageUtilities.default3x3TilesetScale * i, ImageUtilities.default3x3TilesetScale * j, null);
            }
        }
        return image;
    }
}