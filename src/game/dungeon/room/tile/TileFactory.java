package game.dungeon.room.tile;

import java.util.HashMap;

import game.dungeon.room.object_utilities.CollisionBox;
import game.game_components.Factory;
import game.utilities.ImageUtilities;

public class TileFactory extends Factory<Tile> {
    private static final String name = "dungeonTileSet";
    private static final HashMap<Integer, CollisionBox> collision = new HashMap<>() {
        {
            put(0, new CollisionBox(0, 0, 1, 1));
            put(1, new CollisionBox(0, 0, 1, 1));
            put(2, new CollisionBox(0, 0, 1, 1));
            put(3, new CollisionBox(0, 0, 1, 1));
            put(4, new CollisionBox(0, 0, 1, 1));
            put(18, new CollisionBox(0, 0, 1, 1));
            put(19, new CollisionBox(0, 0, 1, 1));
            put(20, new CollisionBox(0, 0, 1, 1));
            put(21, new CollisionBox(0, 0, 1, 1));
            put(22, new CollisionBox(0, 0, 1, 1));
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
            put(90, new CollisionBox(0, 0.25, 0.75, 0.75));
            put(91, new CollisionBox(0, 0.25, 1, 0.75));
            put(92, new CollisionBox(0.25, 0.25, 0.75, 0.75));
            put(93, new CollisionBox(0.25, 0.25, 0.75, 0.75));
            put(94, new CollisionBox(0, 0, 0.75, 0.75));
            put(108, new CollisionBox(0, 0, 1, 1));
            put(109, new CollisionBox(0, 0, 1, 1));
            put(110, new CollisionBox(0, 0, 1, 1));
            put(111, new CollisionBox(0.25, 0, 0.75, 0.25));
            put(112, new CollisionBox(0, 0, 0.75, 0.25));

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

            put(137, new CollisionBox(0, 0.25, 0.75, 0.75));
            put(139, new CollisionBox(0.25, 0.25, 0.75, 0.75));
            put(155, new CollisionBox(0, 0, 0.75, 1));
            put(157, new CollisionBox(0.25, 0, 0.75, 1));

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

    private static final HashMap<Integer, Integer> height = new HashMap<>() {
        {
            put(0, Tile.SOLID);
            put(1, Tile.SOLID);
            put(2, Tile.SOLID);
            put(3, Tile.SOLID);
            put(4, Tile.SOLID);
            put(18, Tile.SOLID);
            put(19, Tile.SOLID);
            put(20, Tile.SOLID);
            put(21, Tile.SOLID);
            put(22, Tile.SOLID);
            put(36, Tile.SOLID);
            put(37, Tile.SOLID);
            put(38, Tile.SOLID);
            put(54, Tile.WALL);
            put(55, Tile.WALL);
            put(56, Tile.WALL);
            put(57, Tile.WALL);
            put(58, Tile.WALL);
            put(72, Tile.WALL);
            put(73, Tile.CLEAR);
            put(74, Tile.WALL);
            put(75, Tile.WALL);
            put(76, Tile.WALL);
            put(90, Tile.WALL);
            put(91, Tile.WALL);
            put(92, Tile.WALL);
            put(93, Tile.WALL);
            put(94, Tile.WALL);
            put(108, Tile.WALL);
            put(109, Tile.WALL);
            put(110, Tile.WALL);
            put(111, Tile.WALL);
            put(112, Tile.WALL);

            put(126, Tile.SOLID);
            put(127, Tile.SOLID);
            put(128, Tile.SOLID);
            put(129, Tile.SOLID);
            put(130, Tile.SOLID);
            put(144, Tile.SOLID);
            put(145, Tile.SOLID);
            put(146, Tile.SOLID);
            put(147, Tile.SOLID);
            put(148, Tile.SOLID);
            put(162, Tile.SOLID);
            put(163, Tile.SOLID);
            put(164, Tile.SOLID);

            put(198, Tile.CLEAR);
            put(199, Tile.CLEAR);
            put(200, Tile.CLEAR);
            put(201, Tile.CLEAR);
            put(202, Tile.CLEAR);
            put(216, Tile.CLEAR);
            put(217, Tile.CLEAR);
            put(218, Tile.CLEAR);
            put(219, Tile.CLEAR);
            put(220, Tile.CLEAR);
            put(234, Tile.CLEAR);
            put(235, Tile.CLEAR);
            put(236, Tile.CLEAR);
            put(252, Tile.CLEAR);
            put(253, Tile.CLEAR);
            put(254, Tile.CLEAR);
            put(255, Tile.CLEAR);
            put(270, Tile.CLEAR);
            put(271, Tile.CLEAR);
            put(272, Tile.CLEAR);
            put(273, Tile.CLEAR);

            put(137, Tile.WALL);
            put(138, Tile.WALL);
            put(139, Tile.WALL);
            put(155, Tile.WALL);
            put(156, Tile.WALL);
            put(157, Tile.WALL);
            put(140, Tile.WALL);
            put(141, Tile.WALL);
            put(142, Tile.WALL);
            put(158, Tile.WALL);
            put(159, Tile.WALL);
            put(160, Tile.WALL);
            put(176, Tile.WALL);
            put(177, Tile.WALL);
            put(178, Tile.WALL);
            put(194, Tile.WALL);
            put(195, Tile.WALL);
            put(196, Tile.WALL);
            put(214, Tile.WALL);
            put(215, Tile.WALL);
            put(232, Tile.WALL);
            put(233, Tile.WALL);
            put(250, Tile.WALL);
            put(251, Tile.WALL);
            put(268, Tile.WALL);
            put(269, Tile.WALL);
            put(286, Tile.WALL);
            put(287, Tile.WALL);
        }
    };

    public Tile getTile(int r, int c) {
        if (!height.containsKey(18 * c + r)) {
            return new Tile(18 * c + r, ImageUtilities.getImage("tiles", name, c, r), collision.get(18 * c + r),
                Tile.CLEAR);
        }
        return new Tile(18 * c + r, ImageUtilities.getImage("tiles", name, c, r), collision.get(18 * c + r),
                height.get(18 * c + r));
    }
}
