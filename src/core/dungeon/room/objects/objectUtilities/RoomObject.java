package core.dungeon.room.objects.objectUtilities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import core.dungeon.room.objects.entity.Player;
import core.game.Game;
import core.utilities.Drawable;
import core.utilities.Updatable;
import core.window.GamePanel;

public abstract class RoomObject implements Drawable, Updatable {
    private BufferedImage image;
    private double x, y;
    private CollisionBox interactbox;
    private CollisionBox hitbox;

    public RoomObject(BufferedImage image, int r, int c, CollisionBox hitbox, CollisionBox interactbox) {
        this(image, r, c, hitbox);
        this.interactbox = interactbox;
    }

    public RoomObject(BufferedImage image, int r, int c, CollisionBox hitbox) {
        this.image = image;
        x = c * GamePanel.TILESIZE;
        y = r * GamePanel.TILESIZE;
        this.hitbox = hitbox;
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(image, (int) Math.round(x), (int) Math.round(y), null);
        if (Game.DEBUG) {
            if (interactbox != null) {
                g2d.setColor(Color.blue);
                g2d.drawRect((int) Math.round(x + interactbox.getX()), (int) Math.round(y + interactbox.getY()),
                        (int) interactbox.getWidth(), (int) interactbox.getHeight());
            }
            g2d.setColor(Color.red);
            g2d.drawRect((int) Math.round(x + hitbox.getX()), (int) Math.round(y + hitbox.getY()),
                    (int) hitbox.getWidth(), (int) hitbox.getHeight());
        }
    }

    public boolean interacts(RoomObject object) {
        CollisionBox h1 = interactbox;
        CollisionBox h2 = object.getinteractbox();
        if (h1 == null || h2 == null) {
            return false;
        }
        if (h1.getMinX() + x < h2.getMaxX() + object.getX() && h1.getMaxX() + x > h2.getMinX() + object.getX()
                && h1.getMinY() + y < h2.getMaxY() + object.getY() && h1.getMaxY() + y > h2.getMinY() + object.getY()) {
            return true;
        }
        return false;
    }

    public boolean collides(RoomObject object) {
        CollisionBox h1 = hitbox;
        CollisionBox h2 = object.getHitBox();
        if (h1 == null || h2 == null) {
            return false;
        }
        if (h1.getMinX() + x < h2.getMaxX() + object.getX() && h1.getMaxX() + x > h2.getMinX() + object.getX()
                && h1.getMinY() + y < h2.getMaxY() + object.getY() && h1.getMaxY() + y > h2.getMinY() + object.getY()) {
            return true;
        }
        return false;
    }

    public abstract void interaction(Player player);

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void moveX(double delta) {
        x += delta;
    }

    public void moveY(double delta) {
        y += delta;
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public CollisionBox getinteractbox() {
        return interactbox;
    }

    public CollisionBox getHitBox() {
        return hitbox;
    }

}
