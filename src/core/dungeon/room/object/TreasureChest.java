package core.dungeon.room.object;

import core.dungeon.room.entity.Player;
import core.dungeon.room.object_utilities.CollisionBox;
import core.dungeon.room.object_utilities.RoomObject;
import core.dungeon.room.object_utilities.SpriteSheet;
import core.utilities.ImageUtilities;

public class TreasureChest extends RoomObject {
    public TreasureChest(SpriteSheet spriteSheet, int r, int c, CollisionBox hitbox, CollisionBox interactbox, int id) {
        super(spriteSheet, r, c, hitbox, interactbox);
    }

    public void update() {
    }
   
    public void collides(Player player) {
        
    }
    
    public void interaction(Player player) {
        if (getSpriteSheet().getFrame() == 1) {
            return;
        }
        getSpriteSheet().nextFrame();
        player.getInventory().openChest(this);
    }

    public static TreasureChest getTreasureChest(RoomObjectData data) {
        switch (data.id) {
            case RoomObjectData.TREASURE_CHEST_0:
                return new TreasureChest(new SpriteSheet(ImageUtilities.getImage("objects", "chest0"), 2), data.r,
                        data.c,
                        CollisionBox.getCollisionBox(0.375, 1, 1.25, 0.8125),
                        CollisionBox.getCollisionBox(0.375, 1.5, 1.25, 1.0625), 0);
            case RoomObjectData.TREASURE_CHEST_1:
                return new TreasureChest(new SpriteSheet(ImageUtilities.getImage("objects", "chest1"), 2), data.r,
                        data.c,
                        CollisionBox.getCollisionBox(0.3125, 0.8125, 1.375, 1),
                        CollisionBox.getCollisionBox(0.3125, 1.3125, 1.375, 1.25), 1);
            case RoomObjectData.TREASURE_CHEST_2:
                return new TreasureChest(new SpriteSheet(ImageUtilities.getImage("objects", "chest2"), 2), data.r,
                        data.c,
                        CollisionBox.getCollisionBox(0.3125, 0.8125, 1.375, 1),
                        CollisionBox.getCollisionBox(0.3125, 1.3125, 1.375, 1.25), 2);
            case RoomObjectData.TREASURE_CHEST_3:
                return new TreasureChest(new SpriteSheet(ImageUtilities.getImage("objects", "chest3"), 2), data.r,
                        data.c,
                        CollisionBox.getCollisionBox(0.125, 0.6875, 1.75, 1.125),
                        CollisionBox.getCollisionBox(0.125, 1.1875, 1.75, 1.375), 3);
            case RoomObjectData.TREASURE_CHEST_4:
                return new TreasureChest(new SpriteSheet(ImageUtilities.getImage("objects", "chest4"), 2), data.r,
                        data.c,
                        CollisionBox.getCollisionBox(0.3125, 0.8125, 1.375, 1),
                        CollisionBox.getCollisionBox(0.3125, 1.3125, 1.375, 1.25), 4);
            default:
                return null;
        }
    }
}