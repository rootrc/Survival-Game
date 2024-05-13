package core.dungeon.room.objects.entity;

import java.awt.image.BufferedImage;

import core.dungeon.mechanics.collision.CollisionChecker;
import core.dungeon.room.objects.object_utilities.CollisionBox;
import core.dungeon.room.objects.object_utilities.RoomObject;

public abstract class Entity extends RoomObject {
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
    }

    public Entity(BufferedImage image, CollisionBox hitbox, int maxSpeed, CollisionChecker collision) {
        this(image, 0, 0, hitbox, maxSpeed, collision);
    }

    public Entity(BufferedImage image, int r, int c, CollisionBox hitbox, int maxSpeed, CollisionChecker collision) {
        this(image, r, c, hitbox, null, maxSpeed, collision);
    }

    public void setCollision(CollisionChecker collision) {
        this.collision = collision;
    }
}
