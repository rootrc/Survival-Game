package game.dungeon.room.object;

import game.dungeon.room.entity.Player;
import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.RoomObject;
import game.dungeon.room.object_utilities.SpriteSheet;

public class TreasureChest extends RoomObject {
    public TreasureChest(SpriteSheet spriteSheet, int r, int c, CollisionBox hitbox, CollisionBox interactbox, int id) {
        super(spriteSheet, r, c, hitbox, interactbox);
    }

    public void update() {
    }

    public void interaction(Player player) {
        if (getSpriteSheet().getFrame() == 1) {
            return;
        }
        getSpriteSheet().nextFrame();
        player.getInventory().openChest(this);
    }
}
