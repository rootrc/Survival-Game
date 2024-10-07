package core.dungeon.room.object;

import java.awt.image.BufferedImage;

import core.dungeon.room.entity.Player;
import core.dungeon.room.object_utilities.CollisionBox;
import core.dungeon.room.object_utilities.RoomObject;
import core.dungeon.room.tile.Tile;
import core.utilities.ImageUtilities;

public class Ladder extends RoomObject {
    public static final int UP_DIRECTION = 1;
    public static final int DOWN_DIRECTION = -1;

    private int direction;
    private int playerPlacementX;
    private int playerPlacementY;

    public Ladder(BufferedImage image, int r, int c, int playerPlacementX, int playerPlacementY, CollisionBox hitbox,
            CollisionBox interactbox, int direction) {
        super(image, r, c, hitbox, interactbox);
        this.direction = direction;
        this.playerPlacementX = playerPlacementX;
        this.playerPlacementY = playerPlacementY;
    }

    public void update() {
    }

    public void collides(Player player) {
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

    public static Ladder getLadder(RoomObjectData data) {
        switch (data.id) {
            case RoomObjectData.LADDER_UP:
                return new Ladder(ImageUtilities.getImage("objects", "ladderup"), data.r, data.c,
                        -Tile.SIZE / 2, Tile.SIZE,
                        CollisionBox.getCollisionBox(0, 1.25, 1, 0.75),
                        CollisionBox.getCollisionBox(-0.25, 1, 1.5, 1.75), 1);
            case RoomObjectData.LADDER_DOWN:
                return new Ladder(ImageUtilities.getImage("objects", "ladderdown"), data.r, data.c,
                        0, -Tile.SIZE * 3,
                        CollisionBox.getCollisionBox(0.25, 0.125, 1.5, 1.625),
                        CollisionBox.getCollisionBox(0, -0.5, 2, 2.5), -1);
            default:
                return null;
        }
    }
}
