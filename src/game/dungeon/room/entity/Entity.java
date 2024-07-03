package game.dungeon.room.entity;

import java.awt.image.BufferedImage;

import game.dungeon.mechanics.CollisionChecker;
import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.DirectionUtilities;
import game.dungeon.room.object_utilities.RoomObject;
import game.utilities.ImageUtilities;

public abstract class Entity extends RoomObject {
    protected static final double a = Math.sqrt(2) / 2;
    private BufferedImage tileset;
    private int lightStrength;
    private int maxSpeed;
    private double speedX;
    private double speedY;
    private double accFrames;
    private double deaccFrames;
    private CollisionChecker collision;
    private int direction;
    private int animationFrame;

    public Entity(String tilesetName, int r, int c, CollisionBox hitbox, CollisionBox interactbox, int maxSpeed,
            CollisionChecker collision, double accFrames, double deaccFrames) {
        super(ImageUtilities.getImage("entities", tilesetName, 0, 0, 3, 2), r, c, hitbox, interactbox);
        tileset = ImageUtilities.getImage("entities", tilesetName);
        this.maxSpeed = maxSpeed;
        this.collision = collision;
        this.accFrames = accFrames;
        this.deaccFrames = deaccFrames;
        lightStrength = 300;
    }

    public Entity(String tilesetName, CollisionBox hitbox, CollisionBox interactbox, int maxSpeed,
            CollisionChecker collision, double accFrames, double deaccFrames) {
        this(tilesetName, 0, 0, hitbox, interactbox, maxSpeed, collision, accFrames, deaccFrames);
    }

    public Entity(String tilesetName, CollisionBox hitbox, int maxSpeed, CollisionChecker collision, double accFrames,
            double deaccFrames) {
        this(tilesetName, 0, 0, hitbox, maxSpeed, collision, accFrames, deaccFrames);
    }

    public Entity(String tilesetName, int r, int c, CollisionBox hitbox, int maxSpeed, CollisionChecker collision,
            double accFrames, double deaccFrames) {
        this(tilesetName, r, c, hitbox, null, maxSpeed, collision, accFrames, deaccFrames);
    }

    public void update() {
        move();
        updateSprite();
    }

    protected void move() {
        if (!isMoving()) {
            return;
        }
        if (collision.canMove(this, getSpeedX(), 0)) {
            if (isMovingY()) {
                moveX(getSpeedX() * a);
            } else {
                moveX(getSpeedX());
            }
        }
        if (collision.canMove(this, 0, getSpeedY())) {
            if (isMovingX()) {
                moveY(getSpeedY() * a);
            } else {
                moveY(getSpeedY());
            }
        }
    }

    private void updateSprite() {
        if (isMoving()) {
            animationFrame++;
            animationFrame %= 2 * 4 * maxSpeed;
        } else {
            animationFrame = 2 * maxSpeed - 1;
        }
        direction = DirectionUtilities.getDirection(this);
        setImage(ImageUtilities.getImage(tileset, animationFrame / maxSpeed / 2, direction, 3, 2));
    }

    public int getLayer() {
        return collision.getLayer(this);
    }

    public void setCollision(CollisionChecker collision) {
        this.collision = collision;
    }

    public final void changeLightStrength(int delta) {
        lightStrength += delta;
    }

    public final int getLightStrength() {
        return lightStrength;
    }

    public final int getDirection() {
        return direction;
    }

    public final void setDirection(int direction) {
        this.direction = direction;
    }

    public final boolean isMoving() {
        return isMovingX() || isMovingY();
    }

    public final boolean isMovingX() {
        return speedX != 0;
    }

    public final boolean isMovingY() {
        return speedY != 0;
    }

    public final void changeMaxSpeed(int delta) {
        maxSpeed += delta;
    }

    public final int getMaxSpeed() {
        return maxSpeed;
    }

    public final double getSpeedX() {
        return speedX;
    }

    public final void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public final void addSpeedX(double delta) {
        speedX += delta;
    }

    public final double getSpeedY() {
        return speedY;
    }

    public final void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public final void addSpeedY(double delta) {
        speedY += delta;
    }

    public final double getAcc() {
        return maxSpeed / accFrames;
    }

    public final double getDeacc() {
        return maxSpeed / deaccFrames;
    }

}
