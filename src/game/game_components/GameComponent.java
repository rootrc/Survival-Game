package game.game_components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JComponent;

public abstract class GameComponent extends JComponent {
    private boolean update;

    public GameComponent(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setSize(getPreferredSize());
        setLayout(null);
        unpause();
    }

    public void updateComponent() {
        if (!update) {
            return;
        }
        updateChildren();
        update();
    }

    private void updateChildren() {
        for (Component component : getComponents()) {
            if (component instanceof GameComponent) {
                ((GameComponent) component).updateComponent();
            }
        }
    }

    public abstract void update();

    @Override
    public final void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        drawComponent(g2d);
    }

    public abstract void drawComponent(Graphics2D g2d);

    public void add(GameComponent gameComponent) {
        add(gameComponent, 0);
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

    public void setX(double x) {
        setX((int) x);
    }

    public void setX(int x) {
        setLocation(x, getY());
    }

    public void setY(double y) {
        setY((int) y);
    }

    public void setY(int y) {
        setLocation(getX(), y);
    }

    public void moveX(double delta) {
        moveX((int) delta);
    }

    public void moveX(int delta) {
        setLocation(getX() + delta, getY());
    }

    public void moveY(double delta) {
        moveY((int) delta);
    }

    public void moveY(int delta) {
        setLocation(getX(), getY() + delta);
    }

    public void setLocation(double x, double y) {
        setLocation((int) x, (int) y);
    }

    public void pause() {
        update = false;
    }

    public void unpause() {
        update = true;
    }
}