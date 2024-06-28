package game.dungeon.room.entity;

import java.awt.image.BufferedImage;

import game.dungeon.mechanics.CollisionChecker;
import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.RoomObject;

public abstract class Entity extends RoomObject {
    private int lightStrength;
    private int maxSpeed;
    private double speedX;
    private double speedY;
    private double accFrames;
    private double deaccFrames;
    protected CollisionChecker collision;
    protected static final double a = Math.sqrt(2) / 2;

    public Entity(BufferedImage image, int r, int c, CollisionBox hitbox, CollisionBox interactbox, int maxSpeed,
            CollisionChecker collision, double accFrames, double deaccFrames) {
        super(image, r, c, hitbox, interactbox);
        this.maxSpeed = maxSpeed;
        this.collision = collision;
        this.accFrames = accFrames;
        this.deaccFrames = deaccFrames;
        lightStrength = 300;
    }

    public Entity(BufferedImage image, CollisionBox hitbox, CollisionBox interactbox, int maxSpeed,
            CollisionChecker collision, double accFrames, double deaccFrames) {
        this(image, 0, 0, hitbox, interactbox, maxSpeed, collision, accFrames, deaccFrames);
    }

    public Entity(BufferedImage image, CollisionBox hitbox, int maxSpeed, CollisionChecker collision, double accFrames,
            double deaccFrames) {
        this(image, 0, 0, hitbox, maxSpeed, collision, accFrames, deaccFrames);
    }

    public Entity(BufferedImage image, int r, int c, CollisionBox hitbox, int maxSpeed, CollisionChecker collision,
            double accFrames, double deaccFrames) {
        this(image, r, c, hitbox, null, maxSpeed, collision, accFrames, deaccFrames);
    }

    protected final void increaseLightStrength(int delta) {
        lightStrength += delta;
    }

    public final int getLightStrength() {
        return lightStrength;
    }

    protected final void increaseMaxSpeed(int delta) {
        maxSpeed += delta;
    }

    protected final int getMaxSpeed() {
        return maxSpeed;
    }

    protected final double getSpeedX() {
        return speedX;
    }

    protected final void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    protected final void addSpeedX(double delta) {
        speedX += delta;
    }

    protected final double getSpeedY() {
        return speedY;
    }

    protected final void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    protected final void addSpeedY(double delta) {
        speedY += delta;
    }

    protected final double getAcc() {
        return maxSpeed / accFrames;
    }

    protected final double getDeacc() {
        return maxSpeed / deaccFrames;
    }

}
