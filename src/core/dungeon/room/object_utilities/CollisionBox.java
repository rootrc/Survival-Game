package core.dungeon.room.object_utilities;

import java.awt.Rectangle;
import java.util.HashMap;

import core.dungeon.room.tile.Tile;

public class CollisionBox extends Rectangle {
    private static final HashMap<java.lang.Double, CollisionBox> map = new HashMap<>();
    
    public static CollisionBox getCollisionBox(double x, double y, double width, double height) {
        double hash = getHash(x, y, width, height);
        if (map.containsKey(hash)) {
            return map.get(hash);
        }
        map.put(hash, new CollisionBox(x, y, width, height));
        return map.get(hash);
    }

    private static double getHash(double x, double y, double width, double height) {
        return 16 * (x * 1000000 + y * 10000 + width * 100 + height);
    }

    private CollisionBox(double x, double y, double width, double height) {
        super((int) (x * Tile.SIZE), (int) (y * Tile.SIZE),
                (int) (width * Tile.SIZE), (int) (height * Tile.SIZE));
    }
}