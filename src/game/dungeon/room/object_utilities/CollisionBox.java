package game.dungeon.room.object_utilities;

import java.awt.Rectangle;

import game.dungeon.Dungeon;

public class CollisionBox extends Rectangle {
    public CollisionBox(double x, double y, double width, double height) {
        super((int) (x * Dungeon.TILESIZE), (int) (y * Dungeon.TILESIZE),
                (int) (width * Dungeon.TILESIZE), (int) (height * Dungeon.TILESIZE));
    }

}
