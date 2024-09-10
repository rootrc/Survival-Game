package game.dungeon.room.entity;

import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.RoomObject;
import game.dungeon.room.object_utilities.SpriteSheet;

public abstract class Entity extends RoomObject {
    private int maxSpeed;
    private double speedX;
    private double speedY;
    private double accFrameCnt;
    private double deaccFrameCnt;
    private int direction;

    public Entity(SpriteSheet spriteSheet, int r, int c, CollisionBox hitbox, CollisionBox interactbox, int maxSpeed,
            double accFrames, double deaccFrames) {
        super(spriteSheet, r, c, hitbox, interactbox);
        this.maxSpeed = maxSpeed;
        this.accFrameCnt = accFrames;
        this.deaccFrameCnt = deaccFrames;
    }

    public Entity(SpriteSheet spriteSheet, CollisionBox hitbox, CollisionBox interactbox, int maxSpeed,
            double accFrames, double deaccFrames) {
        this(spriteSheet, 0, 0, hitbox, interactbox, maxSpeed, accFrames, deaccFrames);
    }

    public Entity(SpriteSheet spriteSheet, CollisionBox hitbox, int maxSpeed, double accFrames,
            double deaccFrames) {
        this(spriteSheet, 0, 0, hitbox, maxSpeed, accFrames, deaccFrames);
    }

    public Entity(SpriteSheet spriteSheet, int r, int c, CollisionBox hitbox, int maxSpeed,
            double accFrames, double deaccFrames) {
        this(spriteSheet, r, c, hitbox, null, maxSpeed, accFrames, deaccFrames);
    }

    public void update() {
        move();
    }

    public void move() {
        if (!isMoving()) {
            return;
        }
        double a = Math.min(1, maxSpeed / Math.sqrt(speedX * speedX + speedY * speedY));
        moveX(a * speedX);
        moveY(a * speedY);
    }

    public final boolean isMoving() {
        return speedX != 0 || speedY != 0;
    }

    public final int getMaxSpeed() {
        return maxSpeed;
    }

    public final void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
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
        if (direction == -1) {
            return;
        }
        this.direction = direction;
        getSpriteSheet().setDirection(direction);
    }

}
