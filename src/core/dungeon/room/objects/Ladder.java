package core.dungeon.room.objects;

import java.awt.image.BufferedImage;

import core.dungeon.room.objects.entity.Player;
import core.dungeon.room.objects.objectUtilities.CollisionBox;
import core.dungeon.room.objects.objectUtilities.RoomObject;
import core.window.GamePanel;

public class Ladder extends RoomObject {
    private int direction;
    private int playerPlacementX;
    private int playerPlacementY;

    Ladder(BufferedImage image, int x, int y, int playerPlacementX, int playerPlacementY, CollisionBox hitbox, CollisionBox interactbox, int direction) {
        super(image, x, y, hitbox, interactbox);
        this.direction = direction;
        this.playerPlacementX = playerPlacementX;
        this.playerPlacementY = playerPlacementY;
    }

    public void update() {
    }

    public void interaction(Player player) {
        // if (GamePanel.keyH.keyPressed("e")) {
        //     player.setLadder(this);
        // }
    }

    public int getPlayerPlacementX() {
        return (int) getX() + playerPlacementX;
    }

    public int getPlayerPlacementY() {
        return (int) getY() + playerPlacementY;
    }

    public int getDirection() {
        return direction;
    }
}
