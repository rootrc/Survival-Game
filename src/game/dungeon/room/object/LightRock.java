package game.dungeon.room.object;

import java.awt.image.BufferedImage;

import game.dungeon.room.entity.Player;
import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.RoomObject;

public class LightRock extends RoomObject {
    private boolean isUsed;
    private BufferedImage usedRock;

    public LightRock(BufferedImage unusedRock, BufferedImage usedRock, int r, int c, CollisionBox hitbox, CollisionBox interactbox) {
        super(unusedRock, r, c, hitbox, interactbox);
        this.usedRock = usedRock;
    }

    public void update() {
    }

    public void interaction(Player player) {
        if (isUsed) {
            return;
        }
        isUsed = true;
        setImage(usedRock);
    }
}
