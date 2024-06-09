package game.dungeon.room.object;

import java.awt.image.BufferedImage;

import game.dungeon.room.entity.Player;
import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.RoomObject;
import game.utilities.ImageUtilities;

public class TreasureChest extends RoomObject {
    private int id;
    private BufferedImage chestClosed;

    public TreasureChest(int x, int y, CollisionBox hitbox, CollisionBox interactbox, int id) {
        super(ImageUtilities.getImage("objects", "chests", 0, id, 2), x, y, hitbox, interactbox);
        this.id = id;
        chestClosed = ImageUtilities.getImage("objects", "chests", 1, id, 2);
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
