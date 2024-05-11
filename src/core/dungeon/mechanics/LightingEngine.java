package core.dungeon.mechanics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RadialGradientPaint;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import core.dungeon.room.objects.entity.Player;
import core.utilities.Drawable;
import core.utilities.Updatable;
import core.window.GamePanel;

public class LightingEngine implements Drawable, Updatable {
    private final Color color[] = new Color[] {
            new Color(0, 0, 0, 0.1f), new Color(0, 0, 0, 0.42f),
            new Color(0, 0, 0, 0.52f), new Color(0, 0, 0, 0.61f),
            new Color(0, 0, 0, 0.69f), new Color(0, 0, 0, 0.76f),
            new Color(0, 0, 0, 0.82f), new Color(0, 0, 0, 0.87f),
            new Color(0, 0, 0, 0.91f), new Color(0, 0, 0, 0.94f),
            new Color(0, 0, 0, 0.96f), new Color(0, 0, 0, 0.98f) };
    private final float fraction[] = new float[] { 0f, 0.4f, 0.5f, 0.6f, 0.65f, 0.7f, 0.75f, 0.8f, 0.85f, 0.9f, 0.95f,
            1f };
    private final HashMap<Integer, BufferedImage> darknessFilter = new HashMap<>();
    private Player player;

    public LightingEngine(Player player) {
        this.player = player;
    }

    public BufferedImage getDarknessFilter(int i) {
        if (darknessFilter.get(i) != null) {
            return darknessFilter.get(i);
        }
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        BufferedImage image = gd.getDefaultConfiguration().createCompatibleImage(GamePanel.screenWidth, GamePanel.screenHeight, Transparency.TRANSLUCENT);
        darknessFilter.put(i, image);
        Graphics2D g2 = (Graphics2D) darknessFilter.get(i).getGraphics();
        RadialGradientPaint gPaint = new RadialGradientPaint(GamePanel.screenWidth / 2, GamePanel.screenHeight / 2,
                (i / 2), fraction, color);
        g2.setPaint(gPaint);
        g2.fillRect(0, 0, GamePanel.screenWidth, GamePanel.screenHeight);
        g2.dispose();
        return darknessFilter.get(i);
    }

    public void update() {
    }

    public void draw(Graphics2D g2d) {
        BufferedImage image = getDarknessFilter(player.getLightStrength());
        g2d.drawImage(image, (int) player.getX() - image.getWidth() / 2, (int) player.getY() - image.getHeight() / 2, null);
    }
}
