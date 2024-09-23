package game.dungeon.room.object;

import java.awt.image.BufferedImage;

import game.dungeon.Dungeon;
import game.dungeon.room.entity.Player;
import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.RoomObject;
import game.utilities.ImageUtilities;

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
            case RoomObjectData.ladderUp:
                return new Ladder(ImageUtilities.getImage("objects", "ladderup"), data.r, data.c,
                        -Dungeon.TILESIZE / 2, Dungeon.TILESIZE,
                        CollisionBox.getCollisionBox(0, 1.25, 1, 0.75),
                        CollisionBox.getCollisionBox(-0.25, 1, 1.5, 1.75), 1);
            case RoomObjectData.ladderDown:
                return new Ladder(ImageUtilities.getImage("objects", "ladderdown"), data.r, data.c,
                        0, -Dungeon.TILESIZE * 3,
                        CollisionBox.getCollisionBox(0.25, 0.125, 1.5, 1.625),
                        CollisionBox.getCollisionBox(0, -0.5, 2, 2.5), -1);
            default:
                return null;
        }
    }
}
