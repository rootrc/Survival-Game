package core.dungeon.room.tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import core.Game;
import core.dungeon.room.object_utilities.CollisionBox;
import core.utilities.ImageUtilities;

public class Tile {
    private static final String tileSetName = "dungeonTileSet";
    public static final int SIZE = 16;

    public static final int FLOOR = 0;
    public static final int WALL = 1;
    public static final int CEILING = 2;

    private BufferedImage image;
    private CollisionBox hitbox;
    private int height;

    private Tile(BufferedImage image, CollisionBox hitbox, int height) {
        this.image = image;
        this.hitbox = hitbox;
        this.height = height;
    }

    public void draw(Graphics2D g2d, int r, int c) {
        g2d.drawImage(image, Tile.SIZE * c, Tile.SIZE * r, null);
        if (Game.DEBUG) {
            if (hitbox != null) {
                g2d.setColor(Color.RED);
                g2d.drawRect(Tile.SIZE * c + (int) hitbox.getX(), Tile.SIZE * r
                        + (int) hitbox.getY(),
                        (int) hitbox.getWidth(), (int) hitbox.getHeight());
            }
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public CollisionBox getHitbox() {
        return hitbox;
    }

    public int getHeight() {
        return height;
    }

    private static final HashMap<Integer, CollisionBox> collisionSet = new HashMap<>() {
        {
            put(0, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(1, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(2, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(3, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(4, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(18, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(19, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(20, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(21, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(22, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(36, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(37, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(38, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(54, CollisionBox.getCollisionBox(0, 0, 0.75, 1));
            put(55, CollisionBox.getCollisionBox(0, 0, 1, 0.25));
            put(56, CollisionBox.getCollisionBox(0.25, 0, 0.75, 1));
            put(57, CollisionBox.getCollisionBox(0.25, 0.25, 0.75, 0.75));
            put(58, CollisionBox.getCollisionBox(0, 0.25, 0.75, 0.75));
            put(72, CollisionBox.getCollisionBox(0, 0, 0.75, 1));
            put(74, CollisionBox.getCollisionBox(0.25, 0, 0.75, 1));
            put(75, CollisionBox.getCollisionBox(0.25, 0, 0.75, 1));
            put(76, CollisionBox.getCollisionBox(0, 0, 0.75, 1));
            put(90, CollisionBox.getCollisionBox(0, 0.25, 0.75, 0.75));
            put(91, CollisionBox.getCollisionBox(0, 0.25, 1, 0.75));
            put(92, CollisionBox.getCollisionBox(0.25, 0.25, 0.75, 0.75));
            put(93, CollisionBox.getCollisionBox(0.25, 0.25, 0.75, 0.75));
            put(94, CollisionBox.getCollisionBox(0, 0, 0.75, 0.75));
            put(108, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(109, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(110, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(111, CollisionBox.getCollisionBox(0.25, 0, 0.75, 0.25));
            put(112, CollisionBox.getCollisionBox(0, 0, 0.75, 0.25));

            put(126, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(127, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(128, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(129, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(130, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(144, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(145, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(146, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(147, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(148, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(162, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(163, CollisionBox.getCollisionBox(0, 0, 1, 1));
            put(164, CollisionBox.getCollisionBox(0, 0, 1, 1));

            put(137, CollisionBox.getCollisionBox(0, 0.25, 0.75, 0.75));
            put(139, CollisionBox.getCollisionBox(0.25, 0.25, 0.75, 0.75));
            put(155, CollisionBox.getCollisionBox(0, 0, 0.75, 1));
            put(157, CollisionBox.getCollisionBox(0.25, 0, 0.75, 1));

            put(140, CollisionBox.getCollisionBox(0, 0, 0.25, 1));
            put(142, CollisionBox.getCollisionBox(0.75, 0, 0.25, 1));
            put(158, CollisionBox.getCollisionBox(0, 0, 0.25, 0.25));
            put(160, CollisionBox.getCollisionBox(0.75, 0, 0.25, 0.25));
            put(176, CollisionBox.getCollisionBox(0, 0.25, 0.25, 0.75));
            put(178, CollisionBox.getCollisionBox(0.75, 0.25, 0.25, 0.75));
            put(194, CollisionBox.getCollisionBox(0, 0, 0.25, 1));
            put(196, CollisionBox.getCollisionBox(0.75, 0, 0.25, 1));
            put(214, CollisionBox.getCollisionBox(0.25, 0, 0.75, 1));
            put(215, CollisionBox.getCollisionBox(0, 0, 0.75, 1));
            put(268, CollisionBox.getCollisionBox(0.25, 0.5, 0.75, 0.5));
            put(269, CollisionBox.getCollisionBox(0, 0.5, 0.75, 0.5));
            put(286, CollisionBox.getCollisionBox(0.25, 0, 0.75, 1));
            put(287, CollisionBox.getCollisionBox(0, 0, 0.75, 1));
        }
    };

    private static final HashMap<Integer, Integer> heightSet = new HashMap<>() {
        {
            put(0, Tile.CEILING);
            put(1, Tile.CEILING);
            put(2, Tile.CEILING);
            put(3, Tile.CEILING);
            put(4, Tile.CEILING);
            put(18, Tile.CEILING);
            put(19, Tile.CEILING);
            put(20, Tile.CEILING);
            put(21, Tile.CEILING);
            put(22, Tile.CEILING);
            put(36, Tile.CEILING);
            put(37, Tile.CEILING);
            put(38, Tile.CEILING);
            put(54, Tile.WALL);
            put(55, Tile.WALL);
            put(56, Tile.WALL);
            put(57, Tile.WALL);
            put(58, Tile.WALL);
            put(72, Tile.WALL);
            put(73, Tile.FLOOR);
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

            put(126, Tile.CEILING);
            put(127, Tile.CEILING);
            put(128, Tile.CEILING);
            put(129, Tile.CEILING);
            put(130, Tile.CEILING);
            put(144, Tile.CEILING);
            put(145, Tile.CEILING);
            put(146, Tile.CEILING);
            put(147, Tile.CEILING);
            put(148, Tile.CEILING);
            put(162, Tile.CEILING);
            put(163, Tile.CEILING);
            put(164, Tile.CEILING);

            put(198, Tile.FLOOR);
            put(199, Tile.FLOOR);
            put(200, Tile.FLOOR);
            put(201, Tile.FLOOR);
            put(202, Tile.FLOOR);
            put(216, Tile.FLOOR);
            put(217, Tile.FLOOR);
            put(218, Tile.FLOOR);
            put(219, Tile.FLOOR);
            put(220, Tile.FLOOR);
            put(234, Tile.FLOOR);
            put(235, Tile.FLOOR);
            put(236, Tile.FLOOR);
            put(252, Tile.FLOOR);
            put(253, Tile.FLOOR);
            put(254, Tile.FLOOR);
            put(255, Tile.FLOOR);
            put(270, Tile.FLOOR);
            put(271, Tile.FLOOR);
            put(272, Tile.FLOOR);
            put(273, Tile.FLOOR);

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

    public static Tile getTile(int r, int c) {
        int hash = 18 * c + r;
        if (!heightSet.containsKey(hash)) {
            return new Tile(ImageUtilities.getImage("tiles", tileSetName, c, r, Tile.SIZE, Tile.SIZE), collisionSet.get(hash),
                Tile.FLOOR);
        }
        return new Tile(ImageUtilities.getImage("tiles", tileSetName, c, r, Tile.SIZE, Tile.SIZE), collisionSet.get(hash),
                heightSet.get(hash));
    }
}