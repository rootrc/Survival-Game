package game.dungeon.room.object;

import game.dungeon.room.entity.Player;
import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.RoomObject;
import game.dungeon.room.object_utilities.SpriteSheet;

public class LightRock extends RoomObject {
    private int lightAmount;

    public LightRock(SpriteSheet spriteSheet, int r, int c, CollisionBox hitbox, CollisionBox interactbox, int lightAmount) {
        super(spriteSheet, r, c, hitbox, interactbox);
        this.lightAmount = lightAmount;
    }

    public void update() {
    }

    public void interaction(Player player) {
        if (getSpriteSheet().getFrame() == 1) {
            return;
        }
        player.addLightAmount(lightAmount);
        getSpriteSheet().nextFrame();
        getSpriteSheet().getImage();
        setLightRadius(0);
    }
}
