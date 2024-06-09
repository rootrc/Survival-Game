package game.dungeon.room.entity;

import java.awt.image.BufferedImage;

import game.dungeon.mechanics.CollisionChecker;
import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.RoomObject;

public abstract class Entity extends RoomObject {
    private int lightStrength;
    protected int maxSpeed;
    protected double speedX;
    protected double speedY;

    protected CollisionChecker collision;

    public Entity(BufferedImage image, CollisionBox hitbox, CollisionBox interactbox, int maxSpeed, CollisionChecker collision) {
        this(image, 0, 0, hitbox, interactbox, maxSpeed, collision);
    }

    public Entity(BufferedImage image, int r, int c, CollisionBox hitbox, CollisionBox interactbox, int maxSpeed, CollisionChecker collision) {
        super(image, r, c, hitbox, interactbox);
        this.maxSpeed = maxSpeed;
        this.collision = collision;
        // TEMP
        lightStrength = 400;
    }

    public Entity(BufferedImage image, CollisionBox hitbox, int maxSpeed, CollisionChecker collision) {
        this(image, 0, 0, hitbox, maxSpeed, collision);
    }

    public Entity(BufferedImage image, int r, int c, CollisionBox hitbox, int maxSpeed, CollisionChecker collision) {
        this(image, r, c, hitbox, null, maxSpeed, collision);
    }

    public int getLightStrength() {
        return lightStrength;
    }
}
