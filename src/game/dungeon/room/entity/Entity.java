package game.dungeon.room.entity;

import java.awt.image.BufferedImage;

import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.DirectionUtilities;
import game.dungeon.room.object_utilities.RoomObject;
import game.utilities.ImageUtilities;

public abstract class Entity extends RoomObject {
    protected static final double a = Math.sqrt(2) / 2;
    private BufferedImage tileset;
    private int maxSpeed;
    private double speedX;
    private double speedY;
    private double accFrameCnt;
    private double deaccFrameCnt;
    private int direction;
    private int animationFrame;

    public Entity(String tilesetName, int r, int c, CollisionBox hitbox, CollisionBox interactbox, int maxSpeed,
        double accFrames, double deaccFrames) {
        super(ImageUtilities.getImage("entities", tilesetName, 0, 0, 3, 2), r, c, hitbox, interactbox);
        tileset = ImageUtilities.getImage("entities", tilesetName);
        this.maxSpeed = maxSpeed;
        this.accFrameCnt = accFrames;
        this.deaccFrameCnt = deaccFrames;
    }

    public Entity(String tilesetName, CollisionBox hitbox, CollisionBox interactbox, int maxSpeed,
            double accFrames, double deaccFrames) {
        this(tilesetName, 0, 0, hitbox, interactbox, maxSpeed, accFrames, deaccFrames);
    }

    public Entity(String tilesetName, CollisionBox hitbox, int maxSpeed, double accFrames,
            double deaccFrames) {
        this(tilesetName, 0, 0, hitbox, maxSpeed, accFrames, deaccFrames);
    }

    public Entity(String tilesetName, int r, int c, CollisionBox hitbox, int maxSpeed,
            double accFrames, double deaccFrames) {
        this(tilesetName, r, c, hitbox, null, maxSpeed, accFrames, deaccFrames);
    }

    public void update() {
        move();
        updateSprite();
    }

    public void move() {
        if (!isMoving()) {
            return;
        }
        if (isMovingY()) {
            moveX(speedX * a);
        } else {
            moveX(speedX);
        }
        if (isMovingX()) {
            moveY(speedY * a);
        } else {
            moveY(speedY);
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
        setImage(ImageUtilities.getImage(tileset, animationFrame / (2 * maxSpeed), direction, 3, 2));
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
        return maxSpeed / accFrameCnt;
    }

    public final double getDeacc() {
        return maxSpeed / deaccFrameCnt;
    }

    public final int getDirection() {
        return direction;
    }

    public final void setDirection(int direction) {
        this.direction = direction;
    }

}
