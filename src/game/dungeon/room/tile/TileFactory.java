package game.dungeon.room.tile;

import java.util.HashMap;

import game.dungeon.room.object_utilities.CollisionBox;
import game.game_components.Factory;
import game.utilities.ImageUtilities;

public class TileFactory extends Factory<Tile> {
    private static final String name = "dungeonTileSet";
    private static final HashMap<Integer, CollisionBox> map = new HashMap<>() {
        {
            put(0, new CollisionBox(0, 0, 1, 1));
            put(1, new CollisionBox(0, 0, 1, 1));
            put(2, new CollisionBox(0, 0, 1, 1));
            put(3, new CollisionBox(0, 0, 1, 1));
            put(4, new CollisionBox(0, 0, 1, 1));
            put(18, new CollisionBox(0, 0, 1, 1));
            put(19, new CollisionBox(0, 0, 1, 1));
            put(20, new CollisionBox(0, 0, 1, 1));
            put(21, new CollisionBox(0.25, 0, 0.75, 0.25));
            put(22, new CollisionBox(0, 0, 0.75, 0.25));
            put(36, new CollisionBox(0, 0, 1, 1));
            put(37, new CollisionBox(0, 0, 1, 1));
            put(38, new CollisionBox(0, 0, 1, 1));
            put(54, new CollisionBox(0, 0, 0.75, 1));
            put(55, new CollisionBox(0, 0, 1, 0.25));
            put(56, new CollisionBox(0.25, 0, 0.75, 1));
            put(57, new CollisionBox(0.25, 0.25, 0.75, 0.75));
            put(58, new CollisionBox(0, 0.25, 0.75, 0.75));
            put(72, new CollisionBox(0, 0, 0.75, 1));
            put(74, new CollisionBox(0.25, 0, 0.75, 1));
            put(75, new CollisionBox(0.25, 0, 0.75, 1));
            put(76, new CollisionBox(0, 0, 0.75, 1));
            put(90, new CollisionBox(0, 0, 0.75, 0.75));
            put(91, new CollisionBox(0, 0.25, 1, 0.75));
            put(92, new CollisionBox(0.25, 0.25, 0.75, 0.75));
            put(93, new CollisionBox(0.25, 0.25, 0.75, 0.75));
            put(94, new CollisionBox(0, 0, 0.75, 0.75));
            put(108, new CollisionBox(0, 0, 1, 1));
            put(109, new CollisionBox(0, 0, 1, 1));
            put(110, new CollisionBox(0, 0, 1, 1));

            put(126, new CollisionBox(0, 0, 1, 1));
            put(127, new CollisionBox(0, 0, 1, 1));
            put(128, new CollisionBox(0, 0, 1, 1));
            put(129, new CollisionBox(0, 0, 1, 1));
            put(130, new CollisionBox(0, 0, 1, 1));
            put(144, new CollisionBox(0, 0, 1, 1));
            put(145, new CollisionBox(0, 0, 1, 1));
            put(146, new CollisionBox(0, 0, 1, 1));
            put(147, new CollisionBox(0, 0, 1, 1));
            put(148, new CollisionBox(0, 0, 1, 1));
            put(162, new CollisionBox(0, 0, 1, 1));
            put(163, new CollisionBox(0, 0, 1, 1));
            put(164, new CollisionBox(0, 0, 1, 1));

            put(140, new CollisionBox(0, 0, 0.25, 1));
            put(142, new CollisionBox(0.75, 0, 0.25, 1));
            put(158, new CollisionBox(0, 0, 0.25, 0.25));
            put(160, new CollisionBox(0.75, 0, 0.25, 0.25));
            put(176, new CollisionBox(0, 0.25, 0.25, 0.75));
            put(178, new CollisionBox(0.75, 0.25, 0.25, 0.75));
            put(194, new CollisionBox(0, 0, 0.25, 1));
            put(196, new CollisionBox(0.75, 0, 0.25, 1));
            put(214, new CollisionBox(0.25, 0, 0.75, 1));
            put(215, new CollisionBox(0, 0, 0.75, 1));
            put(268, new CollisionBox(0.25, 0.5, 0.75, 0.5));
            put(269, new CollisionBox(0, 0.5, 0.75, 0.5));
            put(286, new CollisionBox(0.25, 0, 0.75, 1));
            put(287, new CollisionBox(0, 0, 0.75, 1));
        }
    };

    public Tile getTile(int r, int c) {
        return new Tile(18 * c + r, ImageUtilities.getImage("tiles", name, c, r), map.get(18 * c + r));
    }
}
