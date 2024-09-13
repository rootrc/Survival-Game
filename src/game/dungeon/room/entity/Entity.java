package game.dungeon.room.entity;

import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.RoomObject;
import game.dungeon.room.object_utilities.SpriteSheet;

public abstract class Entity extends RoomObject {
    private int maxSpeed;
    private double speedX;
    private double speedY;
    private double accSpeed;
    private double deaccSpeed;
    private int direction;

    public Entity(SpriteSheet spriteSheet, int r, int c, CollisionBox hitbox, CollisionBox interactbox, int maxSpeed,
            double accSpeed, double deaccSpeed) {
        super(spriteSheet, r, c, hitbox, interactbox);
        this.maxSpeed = maxSpeed;
        this.accSpeed = accSpeed;
        this.deaccSpeed = deaccSpeed;
    }

    public Entity(SpriteSheet spriteSheet, CollisionBox hitbox, CollisionBox interactbox, int maxSpeed,
            double accSpeed, double deaccSpeed) {
        this(spriteSheet, 0, 0, hitbox, interactbox, maxSpeed, accSpeed, deaccSpeed);
    }

    public Entity(SpriteSheet spriteSheet, CollisionBox hitbox, int maxSpeed, double accSpeed,
            double deaccSpeed) {
        this(spriteSheet, 0, 0, hitbox, maxSpeed, accSpeed, deaccSpeed);
    }

    public Entity(SpriteSheet spriteSheet, int r, int c, CollisionBox hitbox, int maxSpeed,
            double accSpeed, double deaccSpeed) {
        this(spriteSheet, r, c, hitbox, null, maxSpeed, accSpeed, deaccSpeed);
    }

    public void update() {
        move();
    }

    public void move() {
        moveX(speedX);
        moveY(speedY);
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

    public final double getSpeedY() {
        return speedY;
    }

    public final void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public final double getAccSpeed() {
        return accSpeed;
    }

    public final double getDeaccSpeed() {
        return deaccSpeed;
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
