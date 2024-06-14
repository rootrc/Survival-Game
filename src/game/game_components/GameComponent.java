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
    public GameComponent(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setSize(getPreferredSize());
        setLayout(null);
    }

    public void updateComponent() {
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

    public final void add(GameComponent gameComponent) {
        add(gameComponent, 0);
    }

    protected final boolean isMouseWithinComponent(int widthAllowance, int heightAllowance) {
        Point mousePos = MouseInfo.getPointerInfo().getLocation();
        Rectangle bounds = getBounds();
        bounds.setLocation(getLocationOnScreen());
        bounds.grow(widthAllowance, heightAllowance);
        return bounds.contains(mousePos);
    }

    protected final boolean isMouseWithinComponent() {
        return isMouseWithinComponent(0, 0);
    }

    public final void setX(double x) {
        setX((int) x);
    }

    public final void setX(int x) {
        setLocation(x, getY());
    }

    public final void setY(double y) {
        setY((int) y);
    }

    public final void setY(int y) {
        setLocation(getX(), y);
    }

    public final void moveX(double delta) {
        moveX((int) delta);
    }

    public final void moveX(int delta) {
        setLocation(getX() + delta, getY());
    }

    public final void moveY(double delta) {
        moveY((int) delta);
    }

    public final void moveY(int delta) {
        setLocation(getX(), getY() + delta);
    }

    public final void setLocation(double x, double y) {
        setLocation((int) x, (int) y);
    }
}