package core.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JComponent;

import core.utilities.Drawable;
import core.utilities.Updatable;

public abstract class GameComponent extends JComponent {
    private final ArrayList<Drawable> drawables = new ArrayList<>();
    private final ArrayList<Updatable> updatables = new ArrayList<>();
    private boolean draw;
    private boolean update;

    public GameComponent(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setSize(getPreferredSize());
        show();
        unpause();
    }

    public void updateComponent() {
        if (!update) {
            return;
        }
        for (Updatable updatable : updatables) {
            updatable.update();
        }
    }

    @Override
    public final void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!draw) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);

        // will cause lag
        // g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        // RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        // g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
        // RenderingHints.VALUE_RENDER_QUALITY);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        BufferedImage image = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(),
                Transparency.OPAQUE);
        Graphics2D g2d2 = image.createGraphics();
        g2d2.setColor(Color.black);
        g2d2.fillRect(0, 0, getWidth(), getHeight());
        for (Drawable drawable : drawables) {
            drawable.draw(g2d2);
        }
        g2d2.dispose();
        g2d.drawImage(image, 0, 0, null);
        drawComponent(g2d);
    }

    public abstract void drawComponent(Graphics2D g2d);

    public void addObject(Object object) {
        if (object instanceof Updatable) {
            updatables.add((Updatable) object);
        }
        if (object instanceof Drawable) {
            drawables.add((Drawable) object);
        }
    }

    public boolean isMouseWithinComponent(int widthAllowance, int heightAllowance) {
        if (!update) {
            return false;
        }
        Point mousePos = MouseInfo.getPointerInfo().getLocation();
        Rectangle bounds = getBounds();
        bounds.setLocation(getLocationOnScreen());
        bounds.grow(widthAllowance, heightAllowance);
        return bounds.contains(mousePos);
    }

    public boolean isMouseWithinComponent() {
        return isMouseWithinComponent(0, 0);
    }

    public void show() {
        draw = true;
    }

    public void stop() {
        draw = false;
        update = false;
    }

    public void pause() {
        update = false;
    }

    public void unpause() {
        update = true;
    }
}