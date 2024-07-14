package game.dungeon.room.tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.Game;
import game.dungeon.Dungeon;
import game.dungeon.room.object_utilities.CollisionBox;

public class Tile {
    private int id;
    private BufferedImage image;
    private CollisionBox hitbox;
    
    public static final int CLEAR = 0;
    public static final int SOLID = 1;
    public static final int WALL = 2;
    private int opacity;

    public Tile(int id, BufferedImage image, CollisionBox hitbox, int opacity) {
        this.id = id;
        this.image = image;
        this.hitbox = hitbox;
        this.opacity = opacity;
        
    }

    public void draw(Graphics2D g2d, int r, int c) {
        g2d.drawImage(image, Dungeon.TILESIZE * c, Dungeon.TILESIZE * r, null);
        if (Game.DEBUG) {
            if (hitbox != null) {
                g2d.setColor(Color.red);
                g2d.drawRect(Dungeon.TILESIZE * c + (int) hitbox.getX(), Dungeon.TILESIZE * r + (int) hitbox.getY(),
                        (int) hitbox.getWidth(), (int) hitbox.getHeight());
            }
        }
    }

    public int getId() {
        return id;
    }

    public BufferedImage getImage() {
        return image;
    }

    public CollisionBox getHitBox() {
        return hitbox;
    }

    public int getOpacity() {
        return opacity;
    }

}
