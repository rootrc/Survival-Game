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
    private static final Color COLORS[] = new Color[] {
            new Color(0, 0, 0, 0.10f), new Color(0, 0, 0, 0.42f),
            new Color(0, 0, 0, 0.52f), new Color(0, 0, 0, 0.57f),
            new Color(0, 0, 0, 0.61f), new Color(0, 0, 0, 0.66f),
            new Color(0, 0, 0, 0.71f), new Color(0, 0, 0, 0.76f),
            new Color(0, 0, 0, 0.82f), new Color(0, 0, 0, 0.87f),
            new Color(0, 0, 0, 0.91f), new Color(0, 0, 0, 0.94f),
            new Color(0, 0, 0, 0.96f), new Color(0, 0, 0, 0.98f),
            new Color(0, 0, 0, 1.00f) };
    private static final Color COLOR2[] = new Color[] {
            new Color(0, 0, 0, (float) Math.pow(0.10, 0.01)), new Color(0, 0, 0, (float) Math.pow(0.42, 0.01)),
            new Color(0, 0, 0, (float) Math.pow(0.52, 0.01)), new Color(0, 0, 0, (float) Math.pow(0.57, 0.01)),
            new Color(0, 0, 0, (float) Math.pow(0.61, 0.01)), new Color(0, 0, 0, (float) Math.pow(0.66, 0.01)),
            new Color(0, 0, 0, (float) Math.pow(0.71, 0.01)), new Color(0, 0, 0, (float) Math.pow(0.76, 0.01)),
            new Color(0, 0, 0, (float) Math.pow(0.82, 0.01)), new Color(0, 0, 0, (float) Math.pow(0.87, 0.01)),
            new Color(0, 0, 0, (float) Math.pow(0.91, 0.01)), new Color(0, 0, 0, (float) Math.pow(0.94, 0.01)),
            new Color(0, 0, 0, (float) Math.pow(0.96, 0.01)), new Color(0, 0, 0, (float) Math.pow(0.98, 0.01)),
            new Color(0, 0, 0, (float) Math.pow(1.00, 0.01)) };
    private static final float FRACTIONS[] = new float[] { 0f, 0.3f, 0.4f, 0.45f, 0.5f, 0.55f, 0.6f, 0.65f, 0.7f, 0.75f,
            0.8f, 0.85f, 0.9f, 0.95f, 1f };
    private static final HashMap<Integer, BufferedImage> DARKNESS_FILTER = new HashMap<>();
    private static final HashMap<Integer, BufferedImage> DARKNESS_FILTER2 = new HashMap<>();

    private BufferedImage fogOfWar;
    private BufferedImage image;
    private HashSet<Integer> fog = new HashSet<>();
    private Player player;
    private HashMap<RoomObject, Light> lights = new HashMap<>();
    private ShadowCasting shadowCasting;
    private boolean isPlayerPresent;
    private int lastPlayerX;
    private int lastPlayerY;

    // Description: The constructor of the class
    // Parameters: The player, tilegrid, and room object manager
    // Return: Nothing
    public LightingEngine(Player player, TileGrid tileGrid, RoomObjectManager roomObjectManager) {
        super(tileGrid.getWidth(), tileGrid.getHeight());
        this.player = player;
        shadowCasting = new ShadowCasting(player, tileGrid);
        // Adds lights based on the room objects in room object manager
        lights.put(player, new Light());
        lights.get(player).visibilityFactor = 1;
        for (RoomObject roomObject : roomObjectManager.getRoomObjects()) {
            updateLight(roomObject);
        }
        // Sets buffered images (doing it this way is so much faster)
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        fogOfWar = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(),
                Transparency.TRANSLUCENT);
        image = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(),
                Transparency.TRANSLUCENT);
        Graphics2D gl = fogOfWar.createGraphics();
        gl.setColor(Color.BLACK);
        gl.fillRect(0, 0, getWidth(), getHeight());
        isPlayerPresent = true;
    }

    // Description: Sets if the player is present
    // Parameters: A boolean
    // Return: Nothing
    public void setPlayerPresent(boolean isPlayerPresent) {
        this.isPlayerPresent = isPlayerPresent;
        lastPlayerX = player.getX();
        lastPlayerY = player.getY();
        if (isPlayerPresent) {
            lights.get(player).visibilityFactor = 1;
        }
    }

    // Description: Updates the lighting engine
    // Parameters: Nothing
    // Return: Nothing
    public void update() {
        for (Light light : lights.values()) {
            light.update(); // Update each light
        }
        for (RoomObject roomObject : lights.keySet()) {
            // Update each light based on it's corrsponding room object
            if (roomObject.getLightRadius() <= 1) {
                continue;
            }
            updateLight(roomObject);
        }
    }

    // Description: Draws the lighting engine
    // Parameters: Graphics2D
    // Return: Nothing
    public void drawComponent(Graphics2D g2d) {
        if (!Game.LIGHTING) {
            return;
        }
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        image = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(),
                Transparency.TRANSLUCENT);
        if (!isPlayerPresent) { // If player is not present
            Graphics2D gl = image.createGraphics();
            gl.drawImage(fogOfWar, 0, 0, null);
            gl.setComposite(AlphaComposite.DstIn);
            for (RoomObject roomObject : lights.keySet()) { // Draws each light
                int lightRadius = getEffectiveLightRadius(roomObject);
                // Checks if the light radius is too small to draw
                // The room's light radius is the max light radius
                // The light radius from geteffective light radius is current radius
                // (increases/decreases based on player distance)
                if (roomObject.getLightRadius() <= 1 || lightRadius <= 1) {
                    continue;
                }
                if (roomObject.getClass() == Player.class) { // If the roomobject is the player
                    gl.drawImage(getDarknessFilter(lightRadius), lastPlayerX +
                            roomObject.getWidth() / 2 - lightRadius,
                            lastPlayerY + roomObject.getHeight() / 2 - lightRadius, null);
                    lights.get(player).decreaseVisibilityValue();
                    continue;
                }
                // If the roomObject's parent is also a roomObject, use data from that parent
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
        } else { // If player is present
            if (!fog.contains(10000 * player.getX() + player.getY())) { // If player's location is not in fog
                // Add too fog
                fog.add(10000 * player.getX() + player.getY());
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
            for (RoomObject roomObject : lights.keySet()) { // Similar to above
                int lightRadius = getEffectiveLightRadius(roomObject);
                if (roomObject.getLightRadius() <= 1 || lightRadius <= 1) {
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

    // Description: Updates a room object's light
    // Parameters: The room object
    // Return: Nothing
    private void updateLight(RoomObject roomObject) {
        // Recursively updates the lighting inside the room object
        for (Component component : roomObject.getComponents()) {
            if (component instanceof RoomObject) {
                updateLight((RoomObject) component);
            }
        }
        // If no light is already attached to roomobject, assign one
        if (!lights.keySet().contains(roomObject)) {
            attachLight(roomObject);
        }

        if (roomObject.getParent() instanceof RoomObject) { // If parent exists, move roomobject occording to adjust
            roomObject.moveX(roomObject.getParent().getX());
            roomObject.moveY(roomObject.getParent().getY());
        }
        // If light is "visible" (visible from shadowcasing & distance OR visible from
        // light radius)
        if ((shadowCasting.isVisible(roomObject)
                && player.getDistanceFromRoomObject(roomObject) < roomObject.getLightVisibility()
                        * player.getLightRadius() * player.getStats().getVision())
                || (player.getDistanceFromRoomObject(roomObject) < getEffectiveLightRadius(player))) {
            lights.get(roomObject).increaseVisibilityValue();
        } else {
            lights.get(roomObject).decreaseVisibilityValue();
        }
        if (roomObject.getParent() instanceof RoomObject) { // If parent exists, move roomobject occording to adjust
            roomObject.moveX(-roomObject.getParent().getX());
            roomObject.moveY(-roomObject.getParent().getY());
        }
    }

    // Description: Get effective radius radius of room object
    // Parameters: The room object
    // Return: The light radius
    private int getEffectiveLightRadius(RoomObject roomObject) {
        return (int) (lights.get(roomObject).getFactor(roomObject));
    }

    // Description: Attach light to room object
    // Parameters: The room object
    // Return: The light radius
    private void attachLight(RoomObject roomObject) {
        lights.put(roomObject, new Light());
        for (int i = 0; i < 35; i++) {
            updateLight(roomObject);
        }
    }

    // Description: Get darkness filter (light radius)
    // Parameters: The radius
    // Return: The darkness filter image
    public static BufferedImage getDarknessFilter(int radius) {
        if (radius == 0) { // If too small return null
            System.out.println("hi");
            return null;
        }
        // If already cached, return cached image
        if (DARKNESS_FILTER.get(radius) != null) {
            return DARKNESS_FILTER.get(radius);
        }
        // Creates
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        BufferedImage image = gd.getDefaultConfiguration().createCompatibleImage(2 * radius,
                2 * radius, Transparency.TRANSLUCENT);
        Graphics2D g2d = image.createGraphics();
        RadialGradientPaint gPaint = new RadialGradientPaint(radius, radius, radius, FRACTIONS, COLORS);
        g2d.setPaint(gPaint);
        g2d.fillRect(0, 0, 2 * radius, 2 * radius);
        g2d.dispose();
        DARKNESS_FILTER.put(radius, image);
        return image;
    }

    // Exactly the same as getDarknessFilter() except with different light shading
    // and cache, used for fog.
    private BufferedImage getDarknessFilter2(int radius) {
        if (radius == 0) {
            return null;
        }
        if (DARKNESS_FILTER2.get(radius) != null) {
            return DARKNESS_FILTER2.get(radius);
        }
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        BufferedImage image = gd.getDefaultConfiguration().createCompatibleImage(2 * radius,
                2 * radius, Transparency.TRANSLUCENT);
        Graphics2D g2d = image.createGraphics();
        RadialGradientPaint gPaint = new RadialGradientPaint(radius, radius, radius, FRACTIONS, COLOR2);
        g2d.setPaint(gPaint);
        g2d.fillRect(0, 0, 2 * radius, 2 * radius);
        g2d.dispose();
        DARKNESS_FILTER2.put(radius, image);
        return image;
    }

    private static class Light {
        private static final double flickerDegree = 0.01;
        private static final double flickerSize = 0.1;
        private static final double lightRadiusChangeSpeed = 0.03;
        private double prevLightRadius;
        private double randomFactor;
        private double visibilityFactor;

        // Description: Updates the light by randomly flicking it
        // Parameters: Nothing
        // Return: Nothing
        void update() {
            randomFactor += RNGUtilities.getDouble(2 * flickerDegree) - flickerDegree - randomFactor * flickerSize;
        }
        
        // Description: Gets the factor that affects effective light radius
        // Parameters: The room object
        // Return: Nothing
        double getFactor(RoomObject roomObject) {
            if (prevLightRadius == 0) {
                prevLightRadius = roomObject.getLightRadius();
            }
            if (visibilityFactor == 0) {
                return 0;
            }
            if (roomObject.getLightRadius() > prevLightRadius) {
                visibilityFactor = 1
                        - Math.sqrt(roomObject.getLightRadius() * (roomObject.getLightRadius() - prevLightRadius))
                                / roomObject.getLightRadius();
            }
            prevLightRadius = roomObject.getLightRadius();
            return roomObject.getLightRadius() * (Easing.easeOutQuad(visibilityFactor) + randomFactor);
        }

        // Getters and setters

        void increaseVisibilityValue() {
            visibilityFactor = Math.min(visibilityFactor + lightRadiusChangeSpeed, 1);
        }

        void decreaseVisibilityValue() {
            visibilityFactor = Math.max(visibilityFactor - lightRadiusChangeSpeed, 0);
        }
    }
}