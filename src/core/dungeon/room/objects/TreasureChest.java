package core.dungeon.room.objects;

import java.awt.image.BufferedImage;

import core.dungeon.room.objects.entity.Player;
import core.dungeon.room.objects.objectUtilities.CollisionBox;
import core.dungeon.room.objects.objectUtilities.RoomObject;
import core.utilities.ImageUtilities;

class TreasureChest extends RoomObject {
    private int id;
    private BufferedImage chestClosed;

    TreasureChest(int x, int y, CollisionBox hitbox, CollisionBox interactbox, int id) {
        super(ImageUtilities.getImage("objects", "chests", 0, 2 * id, 2, 2), x, y, hitbox, interactbox);
        this.id = id;
        chestClosed = ImageUtilities.getImage("objects", "chests", 2, 2 * id, 2, 2);
    }

    public void update() {
    }

    public void interaction(Player player) {
        setImage(chestClosed);
        // TODO
        // Add loot tables and giving player items
        id = id - 0;
    }
}
