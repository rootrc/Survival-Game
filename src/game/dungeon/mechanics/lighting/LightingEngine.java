package game.dungeon.mechanics.lighting;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RadialGradientPaint;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import game.Game;
import game.dungeon.mechanics.CollisionHandler;
import game.dungeon.mechanics.HeightHandler;
import game.dungeon.room.entity.Player;
import game.dungeon.room.object_utilities.RoomObject;
import game.dungeon.room.object_utilities.RoomObjectManager;
import game.game_components.GameComponent;
import game.utilities.RNGUtilities;

public class LightingEngine extends GameComponent {
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
    private static final double flickerDegree = 0.01;
    private static final double flickerSize = 0.1;

    private BufferedImage image;
    private double randomFactor;

    private HeightHandler heightHandler;
    private RoomObjectManager roomObjectManager;
    private ShadowCasting shadowCasting;

    public LightingEngine(int width, int height, Player player, CollisionHandler collisionHandler, HeightHandler HeightHandler,
            RoomObjectManager roomObjectManager) {
        super(width, height);
        this.heightHandler = HeightHandler;
        this.roomObjectManager = roomObjectManager;
        this.shadowCasting = new ShadowCasting(player, collisionHandler, heightHandler);
        randomFactor = -0.99;
    }

    public void update() {
        randomFactor += RNGUtilities.getDouble(2 * flickerDegree) - flickerDegree - randomFactor * flickerSize;
    }

    public void drawComponent(Graphics2D g2d) {
        if (Game.DEBUG) {
            return;
        }
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        image = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(), Transparency.TRANSLUCENT);
        Graphics2D gl = (Graphics2D) image.getGraphics();
        gl.setColor(Color.black);
        gl.fillRect(0, 0, getWidth(), getHeight());
        gl.setComposite(AlphaComposite.DstIn);
        for (RoomObject roomObject : roomObjectManager.getRoomObjects()) {
            int lightRadius = getEffectiveLightRadius(roomObject);
            if (lightRadius <= 1) {
                continue;
            }
            if (!shadowCasting.isVisible(roomObject.getRow(), roomObject.getCol())) {
                continue;
            }
            BufferedImage image = getDarknessFilter(lightRadius);
            int x = roomObject.getX() + (roomObject.getWidth() - image.getWidth()) / 2;
            int y = roomObject.getY() + (roomObject.getHeight() - image.getHeight()) / 2;
            gl.drawImage(image, x, y, null);
        }
        gl.dispose();
        g2d.drawImage(image, 0, 0, null);
    }

    private int getEffectiveLightRadius(RoomObject roomObject) {
            return (int) (roomObject.getLightRadius() * (1 + randomFactor));
    }

    private BufferedImage getDarknessFilter(int i) {
        if (i <= 1) {
            return null;
        }
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
}
