package game.dungeon.room.object;

import java.awt.image.BufferedImage;

import game.dungeon.room.entity.Player;
import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.RoomObject;

public class LightRock extends RoomObject {
    private boolean isUsed;
    private BufferedImage usedRock;
    private int lightAmount;

    public LightRock(BufferedImage unusedRock, BufferedImage usedRock, int r, int c, CollisionBox hitbox, CollisionBox interactbox, int lightAmount) {
        super(unusedRock, r, c, hitbox, interactbox);
        this.usedRock = usedRock;
        this.lightAmount = lightAmount;
    }

    public void update() {
    }

    public void interaction(Player player) {
        if (isUsed) {
            return;
        }
        player.addLightAmount(lightAmount);
        isUsed = true;
        setImage(usedRock);
    }
}
