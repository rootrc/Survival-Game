package core.dungeon.room.tile;

import java.awt.image.BufferedImage;

public class Tile {
    private BufferedImage image;
    private boolean collision;
    private boolean opacity;

    public Tile(BufferedImage image, boolean collision, boolean opacity) {
        this.image = image;
        this.collision = collision;
        this.opacity = opacity;
    }

    public BufferedImage getImage() {
        return image;
    }

    public boolean getCollision() {
        return collision;
    }

    public boolean getOpacity() {
        return opacity;
    }

}
