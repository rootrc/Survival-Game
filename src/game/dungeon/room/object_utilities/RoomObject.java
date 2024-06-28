package game.dungeon.room.object_utilities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.Game;
import game.dungeon.Dungeon;
import game.dungeon.room.entity.Player;
import game.game_components.GameComponent;

public abstract class RoomObject extends GameComponent {
    private BufferedImage image;
    private CollisionBox interactbox;
    private CollisionBox hitbox;

    public RoomObject(BufferedImage image, int r, int c, CollisionBox hitbox, CollisionBox interactbox) {
        this(image, r, c, hitbox);
        this.interactbox = interactbox;
    }

    public RoomObject(BufferedImage image, int r, int c, CollisionBox hitbox) {
        super(image.getWidth(), image.getHeight());
        this.image = image;
        setLocation(c * Dungeon.TILESIZE, r * Dungeon.TILESIZE);
        this.hitbox = hitbox;
    }

    public void drawComponent(Graphics2D g2d) {
        g2d.drawImage(image, 0, 0, null);
        if (!Game.DEBUG) {
            return;
        }
        if (interactbox != null) {
            g2d.setColor(Color.blue);
            g2d.drawRect((int) (interactbox.getX()), (int) (interactbox.getY()),
                    (int) interactbox.getWidth(), (int) interactbox.getHeight());
        }
        g2d.setColor(Color.red);
        g2d.drawRect((int) (hitbox.getX()), (int) (hitbox.getY()),
                (int) hitbox.getWidth(), (int) hitbox.getHeight());
    }

    public final boolean interacts(RoomObject object) {
        CollisionBox h1 = interactbox;
        CollisionBox h2 = object.getInteractbox();
        if (h1 == null || h2 == null) {
            return false;
        }
        if (h1.getMinX() + getX() < h2.getMaxX() + object.getX()
                && h1.getMaxX() + getX() > h2.getMinX() + object.getX()
                && h1.getMinY() + getY() < h2.getMaxY() + object.getY()
                && h1.getMaxY() + getY() > h2.getMinY() + object.getY()) {
            return true;
        }
        return false;
    }

    public final boolean collides(RoomObject object) {
        CollisionBox h1 = hitbox;
        CollisionBox h2 = object.getHitBox();
        if (h1 == null || h2 == null) {
            return false;
        }
        if (h1.getMinX() + getX() < h2.getMaxX() + object.getX()
                && h1.getMaxX() + getX() > h2.getMinX() + object.getX()
                && h1.getMinY() + getY() < h2.getMaxY() + object.getY()
                && h1.getMaxY() + getY() > h2.getMinY() + object.getY()) {
            return true;
        }
        return false;
    }

    public abstract void interaction(Player player);

    public final void setImage(BufferedImage image) {
        this.image = image;
    }

    public final CollisionBox getInteractbox() {
        return interactbox;
    }

    public final CollisionBox getHitBox() {
        return hitbox;
    }

}
