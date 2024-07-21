package game.dungeon.room.tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.dungeon.Dungeon;
import game.dungeon.room.object_utilities.CollisionBox;

public class Tile {
    private BufferedImage image;
    private CollisionBox hitbox;

    public static final int FLOOR = 0;
    public static final int WALL = 1;
    public static final int CEILING = 2;
    private int height;

    public Tile(BufferedImage image, CollisionBox hitbox, int height) {
        this.image = image;
        this.hitbox = hitbox;
        this.height = height;
    }

    public void draw(Graphics2D g2d, int r, int c) {
        g2d.drawImage(image, Dungeon.TILESIZE * c, Dungeon.TILESIZE * r, null);
        // if (Game.DEBUG) {
        //     if (hitbox != null) {
        //         g2d.setColor(Color.red);
        //         g2d.drawRect(Dungeon.TILESIZE * c + (int) hitbox.getX(), Dungeon.TILESIZE * r
        //                 + (int) hitbox.getY(),
        //                 (int) hitbox.getWidth(), (int) hitbox.getHeight());
        //     }
        // }
    }

    public BufferedImage getImage() {
        return image;
    }

    public CollisionBox getHitBox() {
        return hitbox;
    }

    public int getHeight() {
        return height;
    }

}
