package core.dungeon.room.objects;

import java.awt.image.BufferedImage;

import core.dungeon.room.objects.entity.Player;
import core.dungeon.room.objects.object_utilities.CollisionBox;
import core.dungeon.room.objects.object_utilities.RoomObject;

public class Ladder extends RoomObject {
    private int direction;
    private int playerPlacementX;
    private int playerPlacementY;

    public Ladder(BufferedImage image, int x, int y, int playerPlacementX, int playerPlacementY, CollisionBox hitbox,
            CollisionBox interactbox, int direction) {
        super(image, x, y, hitbox, interactbox);
        this.direction = direction;
        this.playerPlacementX = playerPlacementX;
        this.playerPlacementY = playerPlacementY;
    }

    public void update() {
    }

    public void interaction(Player player) {
        player.setLadder(this);
    }

    public double getPlayerPlacementX() {
        return getX() + playerPlacementX;
    }

    public double getPlayerPlacementY() {
        return getY() + playerPlacementY;
    }

    public int getDirection() {
        return direction;
    }
}
