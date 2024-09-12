package game.dungeon.room.object;

import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import game.dungeon.room.entity.Player;
import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.RoomObject;
import game.dungeon.room.object_utilities.SpriteSheet;
import game.utilities.ImageUtilities;
import game.utilities.RNGUtilities;

public class Trap extends RoomObject {
    public Trap(SpriteSheet spriteSheet, int r, int c, CollisionBox hitbox) {
        super(spriteSheet, r, c, hitbox, null);
    }

    public void update() {
        getSpriteSheet().next();
    }

    public void collides(Player player) {
        System.out.println("ow");
    }

    public void interaction(Player player) {
    }

    public static Trap getTrap(RoomObjectData data) {
        switch (data.id) {
            case RoomObjectData.saw0:
                return new Saw(new SpriteSheet(ImageUtilities.getImage("objects", "saw0"), 8, 3),
                        data.r, data.c, new CollisionBox(0.375, 0.3125, 1.25, 1));
            case RoomObjectData.saw1:
                return new Saw(new SpriteSheet(ImageUtilities.getImage("objects", "saw1"), 8, 3),
                        data.r, data.c, new CollisionBox(0.625, 0.3125, 2.75, 2.5));
            case RoomObjectData.movingSaw0:
                return new MovingSaw(new SpriteSheet(ImageUtilities.getImage("objects", "saw0"), 8, 3),
                        data.r, data.c, data.a, data.b, new CollisionBox(0.375, 0.3125, 1.25, 1));
            default:
                return null;
        }
    }

}

class Saw extends Trap {
    public Saw(SpriteSheet spriteSheet, int r, int c, CollisionBox hitbox) {
        super(spriteSheet, r, c, hitbox);
        setOnFloor(true);
    }

    public void update() {
        getSpriteSheet().next();
    }
}

class MovingSaw extends Trap {
    private Saw saw;
    private int direction;
    private int movementDirection;

    public MovingSaw(SpriteSheet sawSpriteSheet, int r, int c, int direction, int length,
            CollisionBox hitbox) {
        super(getBase(sawSpriteSheet, direction, length), r, c, hitbox);
        this.direction = direction;
        setOnFloor(true);
        saw = new Saw(sawSpriteSheet, 0, 0, null);
        add(saw);
        if (direction == 0) {
            int startingSawLocation = RNGUtilities.getInt(getHeight() - saw.getHeight() + 1);
            saw.setY(startingSawLocation);
            getHitBox().setLocation(getHitBox().x, getHitBox().y + startingSawLocation);
        } else if (direction == 1) {
            int startingSawLocation = RNGUtilities.getInt(getWidth() - saw.getWidth() + 1);
            saw.setX(startingSawLocation);
            getHitBox().setLocation(getHitBox().x + startingSawLocation, getHitBox().y);
        }
        if (RNGUtilities.getBoolean()) {
            movementDirection = 1;
        } else {
            movementDirection = -1;
        }
    }

    public void update() {
        if (direction == 0) {
            saw.moveY(movementDirection);
            getHitBox().setLocation(getHitBox().x, getHitBox().y + movementDirection);
            if (saw.getY() == 0 || saw.getY() + saw.getHeight() == getHeight()) {
                movementDirection *= -1;
            }
        } else if (direction == 1) {
            saw.moveX(movementDirection);
            getHitBox().setLocation(getHitBox().x + movementDirection, getHitBox().y);
            if (saw.getX() == 0 || saw.getX() + saw.getWidth() == getWidth()) {
                movementDirection *= -1;
            }
        }
    }

    private static SpriteSheet getBase(SpriteSheet sawSpriteSheet, int direction, int length) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (direction == 0) {
            SpriteSheet spriteSheet = new SpriteSheet(ImageUtilities.getImage("objects", "movingSawVerticalBase"), 3);
            int x0 = (sawSpriteSheet.getWidth() - spriteSheet.getWidth()) / 2;
            int y0 = (sawSpriteSheet.getHeight() - spriteSheet.getHeight()) / 2;
            BufferedImage image = gd.getDefaultConfiguration().createCompatibleImage(sawSpriteSheet.getWidth(),
                    length * spriteSheet.getHeight() + 2 * y0, Transparency.TRANSLUCENT);
            Graphics2D g2d = (Graphics2D) image.getGraphics();
            g2d.drawImage(spriteSheet.getImage(), x0, y0, null);
            spriteSheet.nextFrame();
            for (int i = 1; i < length - 1; i++) {
                g2d.drawImage(spriteSheet.getImage(), x0, y0 + i * spriteSheet.getHeight(), null);
            }
            spriteSheet.nextFrame();
            g2d.drawImage(spriteSheet.getImage(), x0, y0 + (length - 1) * spriteSheet.getHeight(), null);
            g2d.dispose();
            return new SpriteSheet(image);
        } else if (direction == 1) {
            SpriteSheet spriteSheet = new SpriteSheet(ImageUtilities.getImage("objects", "movingSawHorizontalBase"), 3);
            int x0 = (sawSpriteSheet.getWidth() - spriteSheet.getWidth()) / 2;
            int y0 = (sawSpriteSheet.getHeight() - spriteSheet.getHeight()) / 2;
            BufferedImage image = gd.getDefaultConfiguration().createCompatibleImage(
                    length * spriteSheet.getWidth() + 2 * x0, sawSpriteSheet.getHeight(), Transparency.TRANSLUCENT);
            Graphics2D g2d = (Graphics2D) image.getGraphics();
            g2d.drawImage(spriteSheet.getImage(), x0, y0, null);
            spriteSheet.nextFrame();
            for (int i = 1; i < length - 1; i++) {
                g2d.drawImage(spriteSheet.getImage(), x0 + i * spriteSheet.getWidth(), y0, null);
            }
            spriteSheet.nextFrame();
            g2d.drawImage(spriteSheet.getImage(), x0 + (length - 1) * spriteSheet.getWidth(), y0, null);
            g2d.dispose();
            return new SpriteSheet(image);
        }
        return null;
    }
}