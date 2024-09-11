package game.dungeon.room.object;

import game.dungeon.room.entity.Player;
import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.RoomObject;
import game.dungeon.room.object_utilities.SpriteSheet;
import game.utilities.ImageUtilities;

public class Trap extends RoomObject {
    public Trap(SpriteSheet spriteSheet, int r, int c, CollisionBox hitbox) {
        super(spriteSheet, r, c, hitbox);
    }

    public void update() {
        getSpriteSheet().next();
    }

    public void collides(Player player) {
        System.out.println("ow");
    }

    public void interaction(Player player) {
    }

    public static Trap getTrap(RoomObjectData data) {
        switch (data.id) {
            case RoomObjectData.saw0:
                return new Saw(new SpriteSheet(ImageUtilities.getImage("objects", "saw0"), 8, 3),
                        data.r, data.c, new CollisionBox(0.375, 0.3125, 1.25, 1));
            case RoomObjectData.saw1:
                return new Saw(new SpriteSheet(ImageUtilities.getImage("objects", "saw1"), 8, 3),
                        data.r, data.c, new CollisionBox(0.625, 0.3125, 2.75, 2.5));
            default:
                return null;
        }
    }

}

class Saw extends Trap {
    public Saw(SpriteSheet spriteSheet, int r, int c, CollisionBox hitbox) {
        super(spriteSheet, r, c, hitbox);
    }

    public void update() {
        getSpriteSheet().next();
    }
}