package game.dungeon.mechanics;

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

import game.Game;
import game.dungeon.room.entity.Entity;
import game.game_components.GameComponent;

public class LightingEngine extends GameComponent {
    private BufferedImage image;
    private static final Color color[] = new Color[] {
            new Color(0, 0, 0, 0.1f), new Color(0, 0, 0, 0.42f),
            new Color(0, 0, 0, 0.52f), new Color(0, 0, 0, 0.57f),
            new Color(0, 0, 0, 0.61f), new Color(0, 0, 0, 0.66f),
            new Color(0, 0, 0, 0.71f), new Color(0, 0, 0, 0.76f),
            new Color(0, 0, 0, 0.82f), new Color(0, 0, 0, 0.87f),
            new Color(0, 0, 0, 0.91f), new Color(0, 0, 0, 0.94f),
            new Color(0, 0, 0, 0.96f), new Color(0, 0, 0, 0.98f) };
    private static final float fraction[] = new float[] { 0f, 0.3f, 0.4f, 0.45f, 0.5f, 0.55f, 0.6f, 0.65f, 0.7f, 0.75f,
            0.8f, 0.85f, 0.9f, 0.95f };
    private static final HashMap<Integer, BufferedImage> darknessFilter = new HashMap<>();
    private Entity entity;

    public LightingEngine(Entity entity) {
        super(4 * Game.screenWidth, 4 * Game.screenHeight);
        this.entity = entity;
        randomFactor = -0.99;
    }

    public BufferedImage getDarknessFilter(int i) {
        if (darknessFilter.get(i) != null) {
            return darknessFilter.get(i);
        }
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        BufferedImage image = gd.getDefaultConfiguration().createCompatibleImage(i,
                i, Transparency.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        RadialGradientPaint gPaint = new RadialGradientPaint(i / 2, i / 2, i / 2, fraction, color);
        g2d.setPaint(gPaint);
        g2d.fillRect(0, 0, i, i);
        g2d.dispose();
        darknessFilter.put(i, image);
        return image;
    }

    private Area a;

    public void update() {
        randomFactor += 2 * flickerDegree * Math.random() - flickerDegree - randomFactor * flickerSize;
    }

    private double randomFactor;
    private double flickerDegree = 0.01;
    private double flickerSize = 0.1;
    private static final int buffer = 1024;

    public void drawComponent(Graphics2D g2d) {
        if (Game.DEBUG) {
            return;
        }
        image = getDarknessFilter((int) (entity.getLightStrength() * (1 + randomFactor)));
        int x = (int) entity.getX() + entity.getWidth() / 2 - image.getWidth() / 2;
        int y = (int) entity.getY() + entity.getHeight() / 2 - image.getHeight() / 2;
        a = new Area(new Rectangle(-Game.screenWidth / 2 - buffer, -Game.screenHeight / 2 - buffer,
                Game.screenWidth + 2 * buffer, Game.screenHeight + 2 * buffer));
        a.subtract(new Area(new Rectangle(x, y, image.getWidth(), image.getHeight())));
        g2d.setColor(Color.black);
        g2d.fill(a);
        g2d.drawImage(image, x, y, null);
    }
}
