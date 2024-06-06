package core.dungeon.mechanics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import core.dungeon.room.objects.entity.Entity;
import core.utilities.Drawable;
import core.utilities.Updatable;
import core.window.Game;
import core.window.GamePanel;

public class LightingEngine implements Drawable, Updatable {
    private final Color color[] = new Color[] {
            new Color(0, 0, 0, 0.1f), new Color(0, 0, 0, 0.42f),
            new Color(0, 0, 0, 0.52f), new Color(0, 0, 0, 0.61f),
            new Color(0, 0, 0, 0.69f), new Color(0, 0, 0, 0.76f),
            new Color(0, 0, 0, 0.82f), new Color(0, 0, 0, 0.87f),
            new Color(0, 0, 0, 0.91f), new Color(0, 0, 0, 0.94f),
            new Color(0, 0, 0, 0.96f), new Color(0, 0, 0, 0.98f) };
    private final float fraction[] = new float[] { 0f, 0.3f, 0.4f, 0.5f, 0.6f, 0.65f, 0.7f, 0.75f, 0.8f, 0.85f, 0.9f,
            0.95f };
    private static final HashMap<Integer, BufferedImage> darknessFilter = new HashMap<>();
    private Entity entity;

    public LightingEngine(Entity entity) {
        this.entity = entity;
    }

    public BufferedImage getDarknessFilter(int i) {
        if (darknessFilter.get(i) != null) {
            return darknessFilter.get(i);
        }
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        BufferedImage image = gd.getDefaultConfiguration().createCompatibleImage(i,
                i, Transparency.TRANSLUCENT);
        darknessFilter.put(i, image);
        Graphics2D g2d = (Graphics2D) darknessFilter.get(i).getGraphics();
        RadialGradientPaint gPaint = new RadialGradientPaint(i / 2, i / 2, i / 2, fraction, color);
        g2d.setPaint(gPaint);
        g2d.fillRect(0, 0, i, i);
        g2d.dispose();
        return darknessFilter.get(i);
    }

    public void update() {
    }

    private double randomFactor;
    private int flickerDegree = 3;
    private double flickerSize = 0.1;

    public void draw(Graphics2D g2d) {
        if (Game.DEBUG) {
            return;
        }

        // Maybe move to update?
        randomFactor += 2 * flickerDegree * Math.random() - flickerDegree - randomFactor * flickerSize;

        BufferedImage image = getDarknessFilter((int) (entity.getLightStrength() * (1 + randomFactor / 600)));
        int x = (int) entity.getX() + entity.getWidth() / 2 - image.getWidth() / 2;
        int y = (int) entity.getY() + entity.getHeight() / 2 - image.getHeight() / 2;

        g2d.setColor(Color.black);
        int buffer = 640;
        Area a = new Area(new Rectangle((int) entity.getX() - GamePanel.screenWidth / 2 - buffer,
                (int) entity.getY() - GamePanel.screenHeight / 2 - buffer, GamePanel.screenWidth + 2 * buffer,
                GamePanel.screenHeight + 2 * buffer));
        a.subtract(new Area(new Rectangle(x, y, image.getWidth(), image.getHeight())));
        g2d.fill(a);

        g2d.drawImage(image, x, y, null);
    }
}
