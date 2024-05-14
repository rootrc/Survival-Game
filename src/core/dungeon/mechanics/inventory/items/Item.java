package core.dungeon.mechanics.inventory.items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import core.utilities.Drawable;

public class Item implements Drawable {
    private BufferedImage image;
    private String name;
    private String description;
    private int inventoryPriority;

    public Item(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void draw(Graphics2D g2d) {
    }
}
