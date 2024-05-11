package core.window;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JComponent;

import core.utilities.Drawable;
import core.utilities.Updatable;

public abstract class SubScreen extends JComponent {
    private final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private final ArrayList<Drawable> drawables = new ArrayList<>();
    private final ArrayList<Updatable> updatables = new ArrayList<>();
    private boolean draw;
    private boolean update;

    public SubScreen(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        show();
        unpause();
    }

    public void update() {
        if (!update) {
            return;
        }
        for (Updatable updatable : updatables) {
            updatable.update();
        }
    }
    
    public void draw(Graphics2D g2d) {
        if (!draw) {
            return;
        }
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);

        // will cause lag
        // g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        // RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        // g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
        // RenderingHints.VALUE_RENDER_QUALITY);

        BufferedImage image = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(),
                Transparency.OPAQUE);
                // System.out.println(getWidth() + " " + getHeight());
        Graphics2D g = image.createGraphics();
        // draw(g);
        for (Drawable drawable : drawables) {
            drawable.draw(g);
        }
        g.dispose();
        g2d.drawImage(image, 0, 0, null);
    }

    public void addObject(Object object) {
        if (object instanceof Updatable) {
            updatables.add((Updatable) object);
        }
        if (object instanceof Drawable) {
            drawables.add((Drawable) object);
        }
    }

    public void show() {
        draw = true;
    }

    public void stop() {
        draw = false;
    }

    public void pause() {
        update = false;
    }

    public void unpause() {
        update = true;
    }
}