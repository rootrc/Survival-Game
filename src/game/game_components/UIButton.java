package game.game_components;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.Action;
import javax.swing.ImageIcon;

import game.utilities.ImageUtilities;

public class UIButton extends GameButton {

    public UIButton(Action action, String actionCommand, Rectangle rect, BufferedImage image) {
        super(action, actionCommand, rect);
        setIcon(createImageIcon(image, 0));
        setRolloverIcon(createImageIcon(image, 1));
        setPressedIcon(createImageIcon(image, 2));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        }); 
    }

    private ImageIcon createImageIcon(BufferedImage image, int id) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        BufferedImage output = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(),
                Transparency.BITMASK);
        Graphics2D g2d = output.createGraphics();
        g2d.drawImage(buildBackgroundImage(id), 0, 0, null);
        // TEMP
        if (image == null) {
            return new ImageIcon(output);
        }
        if (id != 2) {
            g2d.drawImage(image, (getWidth() - image.getWidth()) / 2, (getHeight() - image.getHeight()) / 2, null);
        } else {
            g2d.drawImage(image, (getWidth() - image.getWidth()) / 2, (getHeight() - image.getHeight()) / 2 + 1,
                    null);
        }
        return new ImageIcon(output);
    }

    private BufferedImage buildBackgroundImage(int id) {
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
                drawButtonTile(g2d, r, c + 4 * id, i, j);
            }
        }
        return image;
    }

    private void drawButtonTile(Graphics2D g2d, int r, int c, int i, int j) {
        g2d.drawImage(ImageUtilities.getImage("UI", "ButtonTileSet", r, c, 2), 32 * i, 32 * j, null);
    }

}
