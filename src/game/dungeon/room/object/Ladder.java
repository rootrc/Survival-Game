package game.dungeon.room.object;

import java.awt.image.BufferedImage;

import game.dungeon.room.entity.Player;
import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.RoomObject;

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
