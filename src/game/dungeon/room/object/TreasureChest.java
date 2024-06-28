package game.dungeon.room.object;

import java.awt.image.BufferedImage;

import game.dungeon.room.entity.Player;
import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.RoomObject;
import game.utilities.ImageUtilities;

public class TreasureChest extends RoomObject {
    private boolean isClosed;
    private BufferedImage chestClosed;

    public TreasureChest(int x, int y, CollisionBox hitbox, CollisionBox interactbox, int id) {
        super(ImageUtilities.getImage("objects", "chests", 0, id, 2), x, y, hitbox, interactbox);
        chestClosed = ImageUtilities.getImage("objects", "chests", 1, id, 2);
    }

    public void update() {
    }

    public void interaction(Player player) {
        if (isClosed) {
            return;
        }
        isClosed = true;
        setImage(chestClosed);
        player.getInventory().openChest(this);
    }
}
