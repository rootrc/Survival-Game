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
import game.dungeon.Dungeon;
import game.dungeon.mechanics.CollisionHandler;
import game.dungeon.mechanics.HeightHandler;
import game.dungeon.room.entity.Player;
import game.dungeon.room.object_utilities.RoomObject;
import game.dungeon.room.object_utilities.RoomObjectManager;
import game.game_components.GameComponent;
import game.utilities.RNGUtilities;

public class LightingEngine extends GameComponent {
    private static final Color color[] = new Color[] {
            new Color(0, 0, 0, 0.10f), new Color(0, 0, 0, 0.42f),
            new Color(0, 0, 0, 0.52f), new Color(0, 0, 0, 0.57f),
            new Color(0, 0, 0, 0.61f), new Color(0, 0, 0, 0.66f),
            new Color(0, 0, 0, 0.71f), new Color(0, 0, 0, 0.76f),
            new Color(0, 0, 0, 0.82f), new Color(0, 0, 0, 0.87f),
            new Color(0, 0, 0, 0.91f), new Color(0, 0, 0, 0.94f),
            new Color(0, 0, 0, 0.96f), new Color(0, 0, 0, 0.98f),
            new Color(0, 0, 0, 1.00f) };
    private static final float fraction[] = new float[] { 0f, 0.3f, 0.4f, 0.45f, 0.5f, 0.55f, 0.6f, 0.65f, 0.7f, 0.75f,
            0.8f, 0.85f, 0.9f, 0.95f, 1f };
    private static final HashMap<Integer, BufferedImage> darknessFilter = new HashMap<>();

    private BufferedImage image;
    private Player player;
    private HeightHandler heightHandler;
    private RoomObjectManager roomObjectManager;
    private HashMap<RoomObject, Light> lights = new HashMap<>();
    private ShadowCasting shadowCasting;

    public LightingEngine(int width, int height, Player player, CollisionHandler collisionHandler,
            HeightHandler HeightHandler, RoomObjectManager roomObjectManager) {
        super(width, height);
        this.player = player;
        this.heightHandler = HeightHandler;
        this.roomObjectManager = roomObjectManager;
        this.shadowCasting = new ShadowCasting(player, collisionHandler, heightHandler);
        lights.put(player, new Light());
        lights.get(player).visibilityFactor = 1;
    }

    public void update() {
        for (Light light : lights.values()) {
            light.update();
        }
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
            if (roomObject.getLightRadius() <= 1) {
                continue;
            }
            updateLight(roomObject);
            int lightRadius = getEffectiveLightRadius(roomObject);
            if (lightRadius <= 1) {
                continue;
            }
            BufferedImage image = getDarknessFilter(lightRadius);
            gl.drawImage(image, roomObject.getX() + (roomObject.getWidth() - image.getWidth()) / 2,
                    roomObject.getY() + (roomObject.getHeight() - image.getHeight()) / 2, null);
        }
        gl.dispose();
        g2d.drawImage(image, 0, 0, null);
    }

    private void updateLight(RoomObject roomObject) {
        if (!lights.keySet().contains(roomObject)) {
            attachLight(roomObject);
        }
        if (shadowCasting.isVisible(roomObject)
                || (getDistance(roomObject) < getEffectiveLightRadius(player) - Dungeon.TILESIZE)) {
            lights.get(roomObject).increaseVisibilityValue();
        } else {
            lights.get(roomObject).decreaseVisibilityValue();
        }
    }

    private int getEffectiveLightRadius(RoomObject roomObject) {
        return (int) (roomObject.getLightRadius() * lights.get(roomObject).getFactor());
    }

    private void attachLight(RoomObject roomObject) {
        lights.put(roomObject, new Light());
        for (int i = 0; i < 35; i++) {
            updateLight(roomObject);
        }
    }

    // TEMP
    private double getDistance(RoomObject roomObject) {
        return Math.sqrt((player.getX() + player.getHitBox().getCenterX() - roomObject.getX()
                - roomObject.getHitBox().getCenterX())
                * (player.getX() + player.getHitBox().getCenterX() - roomObject.getX()
                        - roomObject.getHitBox().getCenterX())
                + (player.getY() + player.getHitBox().getCenterY() - roomObject.getY()
                        - roomObject.getHitBox().getCenterY())
                        * (player.getY() + player.getHitBox().getCenterY() - roomObject.getY()
                                - roomObject.getHitBox().getCenterY()));
    }

    private BufferedImage getDarknessFilter(int radius) {
        if (radius == 0) {
            return null;
        }
        if (darknessFilter.get(radius) != null) {
            return darknessFilter.get(radius);
        }
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        BufferedImage image = gd.getDefaultConfiguration().createCompatibleImage(2 * radius,
                2 * radius, Transparency.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        RadialGradientPaint gPaint = new RadialGradientPaint(radius, radius, radius, fraction, color);
        g2d.setPaint(gPaint);
        g2d.fillRect(0, 0, 2 * radius, 2 * radius);
        g2d.dispose();
        darknessFilter.put(radius, image);
        return image;
    }

    private class Light {
        private static final double flickerDegree = 0.01;
        private static final double flickerSize = 0.1;
        private double randomFactor;
        private double visibilityFactor;

        void update() {
            randomFactor += RNGUtilities.getDouble(2 * flickerDegree) - flickerDegree - randomFactor * flickerSize;
        }

        double getFactor() {
            if (visibilityFactor == 0) {
                return visibilityFactor;
            }
            return 1 - (1 - visibilityFactor) * (1 - visibilityFactor) + randomFactor;
        }

        void increaseVisibilityValue() {
            visibilityFactor = Math.min(visibilityFactor + 0.03, 1);
        }

        void decreaseVisibilityValue() {
            visibilityFactor = Math.max(visibilityFactor - 0.03, 0);
        }
    }
}
