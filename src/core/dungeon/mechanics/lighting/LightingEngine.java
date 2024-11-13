package core.dungeon.mechanics.lighting;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RadialGradientPaint;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;

import core.Game;
import core.dungeon.room.entity.Player;
import core.dungeon.room.object_utilities.RoomObject;
import core.dungeon.room.object_utilities.RoomObjectManager;
import core.dungeon.room.tile.TileGrid;
import core.game_components.GameComponent;
import core.utilities.Easing;
import core.utilities.RNGUtilities;

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
    private static final Color color2[] = new Color[] {
            new Color(0, 0, 0, (float) Math.pow(0.10, 0.01)), new Color(0, 0, 0, (float) Math.pow(0.42, 0.01)),
            new Color(0, 0, 0, (float) Math.pow(0.52, 0.01)), new Color(0, 0, 0, (float) Math.pow(0.57, 0.01)),
            new Color(0, 0, 0, (float) Math.pow(0.61, 0.01)), new Color(0, 0, 0, (float) Math.pow(0.66, 0.01)),
            new Color(0, 0, 0, (float) Math.pow(0.71, 0.01)), new Color(0, 0, 0, (float) Math.pow(0.76, 0.01)),
            new Color(0, 0, 0, (float) Math.pow(0.82, 0.01)), new Color(0, 0, 0, (float) Math.pow(0.87, 0.01)),
            new Color(0, 0, 0, (float) Math.pow(0.91, 0.01)), new Color(0, 0, 0, (float) Math.pow(0.94, 0.01)),
            new Color(0, 0, 0, (float) Math.pow(0.96, 0.01)), new Color(0, 0, 0, (float) Math.pow(0.98, 0.01)),
            new Color(0, 0, 0, (float) Math.pow(1.00, 0.01)) };
    private static final float fraction[] = new float[] { 0f, 0.3f, 0.4f, 0.45f, 0.5f, 0.55f, 0.6f, 0.65f, 0.7f, 0.75f,
            0.8f, 0.85f, 0.9f, 0.95f, 1f };
    private static final HashMap<Integer, BufferedImage> darknessFilter = new HashMap<>();
    private static final HashMap<Integer, BufferedImage> darknessFilter2 = new HashMap<>();

    private BufferedImage fogOfWar;
    private BufferedImage image;
    private HashSet<Integer> fogOfWarPoints = new HashSet<>();
    private Player player;
    private HashMap<RoomObject, Light> lights = new HashMap<>();
    private ShadowCasting shadowCasting;
    private boolean isPlayerPresent;

    public LightingEngine(Player player, TileGrid tileGrid, RoomObjectManager roomObjectManager) {
        super(tileGrid.getWidth(), tileGrid.getHeight());
        this.player = player;
        shadowCasting = new ShadowCasting(player, tileGrid);
        lights.put(player, new Light());
        lights.get(player).visibilityFactor = 1;
        for (RoomObject roomObject : roomObjectManager.getRoomObjects()) {
            updateLight(roomObject);
        }
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        fogOfWar = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(),
                Transparency.TRANSLUCENT);
        image = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(),
                Transparency.TRANSLUCENT);
        Graphics2D gl = fogOfWar.createGraphics();
        gl.setColor(Color.black);
        gl.fillRect(0, 0, getWidth(), getHeight());
        isPlayerPresent = true;
    }

    public void update() {
        for (Light light : lights.values()) {
            light.update();
        }
        for (RoomObject roomObject : lights.keySet()) {
            if (roomObject.getLightRadius() <= 1) {
                continue;
            }
            updateLight(roomObject);
        }
    }

    public void drawComponent(Graphics2D g2d) {
        if (Game.DEBUG) {
            return;
        }
        if (!Game.LIGHTING) {
            return;
        }
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        image = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(),
                Transparency.TRANSLUCENT);
        if (!isPlayerPresent) {
            Graphics2D gl = image.createGraphics();
            gl.drawImage(fogOfWar, 0, 0, null);
            gl.setComposite(AlphaComposite.DstIn);

            for (RoomObject roomObject : lights.keySet()) {
                if (roomObject.getLightRadius() <= 1) {
                    continue;
                }
                int lightRadius = getEffectiveLightRadius(roomObject);
                if (lightRadius <= 1) {
                    continue;
                }
                if (roomObject.getClass() == Player.class) {
                    gl.drawImage(getDarknessFilter(lightRadius), lastPlayerX +
                            roomObject.getWidth() / 2 - lightRadius,
                            lastPlayerY + roomObject.getHeight() / 2 - lightRadius, null);
                    lights.get(player).decreaseVisibilityValue();
                    continue;
                }
                if (roomObject.getParent() instanceof RoomObject) {
                    gl.drawImage(getDarknessFilter(lightRadius), roomObject.getParent().getX() + roomObject.getX() +
                            roomObject.getWidth() / 2 - lightRadius,
                            roomObject.getParent().getY() + roomObject.getY() + roomObject.getHeight() / 2
                                    - lightRadius,
                            null);
                } else {
                    gl.drawImage(getDarknessFilter(lightRadius), roomObject.getX() +
                            roomObject.getWidth() / 2 - lightRadius,
                            roomObject.getY() + roomObject.getHeight() / 2 - lightRadius, null);
                }
            }
            gl.dispose();
        } else {
            if (!fogOfWarPoints.contains(10000 * player.getX() + player.getY())) {
                fogOfWarPoints.add(10000 * player.getX() + player.getY());
                int lightRadius = getEffectiveLightRadius(player) / 2;
                Graphics2D gl = fogOfWar.createGraphics();
                gl.setComposite(AlphaComposite.DstIn);
                int size = (int) (lightRadius * 0.75 * player.getStats().getFogOfWarMulti());
                gl.drawImage(getDarknessFilter2(size), player.getX() + player.getWidth() / 2 - size,
                        player.getY() + player.getHeight() / 2 - size, null);
                gl.dispose();
            }
            Graphics2D gl = image.createGraphics();
            gl.drawImage(fogOfWar, 0, 0, null);
            gl.setComposite(AlphaComposite.DstIn);
            for (RoomObject roomObject : lights.keySet()) {
                if (roomObject.getLightRadius() <= 1) {
                    continue;
                }
                int lightRadius = getEffectiveLightRadius(roomObject);
                if (lightRadius <= 1) {
                    continue;
                }
                if (roomObject.getParent() instanceof RoomObject) {
                    gl.drawImage(getDarknessFilter(lightRadius), roomObject.getParent().getX() + roomObject.getX() +
                            roomObject.getWidth() / 2 - lightRadius,
                            roomObject.getParent().getY() + roomObject.getY() + roomObject.getHeight() / 2
                                    - lightRadius,
                            null);
                } else {
                    gl.drawImage(getDarknessFilter(lightRadius), roomObject.getX() +
                            roomObject.getWidth() / 2 - lightRadius,
                            roomObject.getY() + roomObject.getHeight() / 2 - lightRadius, null);
                }
            }
            gl.dispose();
        }
        g2d.drawImage(image, 0, 0, null);
    }

    private void updateLight(RoomObject roomObject) {
        for (Component component : roomObject.getComponents()) {
            if (component instanceof RoomObject) {
                updateLight((RoomObject) component);
            }
        }
        if (!lights.keySet().contains(roomObject)) {
            attachLight(roomObject);
        }
        if (roomObject.getParent() instanceof RoomObject) {
            roomObject.moveX(roomObject.getParent().getX());
            roomObject.moveY(roomObject.getParent().getY());
            if ((shadowCasting.isVisible(roomObject)
            && player.getDistanceFromRoomObject(roomObject) < roomObject.getLightVisibility())
            || (player.getDistanceFromRoomObject(roomObject) < getEffectiveLightRadius(player))) {
                lights.get(roomObject).increaseVisibilityValue();
            } else {
                lights.get(roomObject).decreaseVisibilityValue();
            }
            roomObject.moveX(-roomObject.getParent().getX());
            roomObject.moveY(-roomObject.getParent().getY());
        } else {
            if ((shadowCasting.isVisible(roomObject)
                    && player.getDistanceFromRoomObject(roomObject) < roomObject.getLightVisibility() * player.getLightRadius()  * player.getStats().getVision())
                    || (player.getDistanceFromRoomObject(roomObject) < getEffectiveLightRadius(player))) {
                lights.get(roomObject).increaseVisibilityValue();
            } else {
                lights.get(roomObject).decreaseVisibilityValue();
            }
        }
    }

    private int getEffectiveLightRadius(RoomObject roomObject) {
        return (int) (lights.get(roomObject).getFactor(roomObject));
    }

    private void attachLight(RoomObject roomObject) {
        lights.put(roomObject, new Light());
        for (int i = 0; i < 35; i++) {
            updateLight(roomObject);
        }
    }

    public static BufferedImage getDarknessFilter(int radius) {
        if (radius == 0) {
            return null;
        }
        if (darknessFilter.get(radius) != null) {
            return darknessFilter.get(radius);
        }
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        BufferedImage image = gd.getDefaultConfiguration().createCompatibleImage(2 * radius,
                2 * radius, Transparency.TRANSLUCENT);
        Graphics2D g2d = image.createGraphics();
        RadialGradientPaint gPaint = new RadialGradientPaint(radius, radius, radius, fraction, color);
        g2d.setPaint(gPaint);
        g2d.fillRect(0, 0, 2 * radius, 2 * radius);
        g2d.dispose();
        darknessFilter.put(radius, image);
        return image;
    }

    private BufferedImage getDarknessFilter2(int radius) {
        if (radius == 0) {
            return null;
        }
        if (darknessFilter2.get(radius) != null) {
            return darknessFilter2.get(radius);
        }
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        BufferedImage image = gd.getDefaultConfiguration().createCompatibleImage(2 * radius,
                2 * radius, Transparency.TRANSLUCENT);
        Graphics2D g2d = image.createGraphics();
        RadialGradientPaint gPaint = new RadialGradientPaint(radius, radius, radius, fraction, color2);
        g2d.setPaint(gPaint);
        g2d.fillRect(0, 0, 2 * radius, 2 * radius);
        g2d.dispose();
        darknessFilter2.put(radius, image);
        return image;
    }

    private static class Light {
        private static final double flickerDegree = 0.01;
        private static final double flickerSize = 0.1;
        private static final double lightRadiusChangeSpeed = 0.03;
        private double prevLightRadius;
        private double randomFactor;
        private double visibilityFactor;

        void update() {
            randomFactor += RNGUtilities.getDouble(2 * flickerDegree) - flickerDegree - randomFactor * flickerSize;
        }

        double getFactor(RoomObject roomObject) {
            if (prevLightRadius == 0) {
                prevLightRadius = roomObject.getLightRadius();
            }
            if (visibilityFactor == 0) {
                return visibilityFactor;
            }
            if (roomObject.getLightRadius() > prevLightRadius) {
                visibilityFactor = 1
                        - Math.sqrt(roomObject.getLightRadius() * (roomObject.getLightRadius() - prevLightRadius))
                                / roomObject.getLightRadius();
            }
            prevLightRadius = roomObject.getLightRadius();
            return roomObject.getLightRadius() * (Easing.easeOutQuad(visibilityFactor) + randomFactor);
        }

        void increaseVisibilityValue() {
            visibilityFactor = Math.min(visibilityFactor + lightRadiusChangeSpeed, 1);
        }

        void decreaseVisibilityValue() {
            visibilityFactor = Math.max(visibilityFactor - lightRadiusChangeSpeed, 0);
        }
    }

    private int lastPlayerX;
    private int lastPlayerY;

    public void setPlayerPresent(boolean isPlayerPresent) {
        this.isPlayerPresent = isPlayerPresent;
        lastPlayerX = player.getX();
        lastPlayerY = player.getY();
        if (isPlayerPresent) {
            lights.get(player).visibilityFactor = 1;
        }
    }
}