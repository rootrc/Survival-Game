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
        player.takeDamage();
    }

    public void interaction(Player player) {
    }

    public static Trap getTrap(RoomObjectData data) {
        Trap trap;
        switch (data.id) {
            case RoomObjectData.saw0:
                trap = new Saw(new SpriteSheet(ImageUtilities.getImage("objects", "saw0"), 8, 3),
                        data.r, data.c, new CollisionBox(0.375, 0.3125, 1.25, 1));
                trap.setLightRadius(20);
                trap.setLightVisibility(150);
                return trap;
            case RoomObjectData.saw1:
                trap = new Saw(new SpriteSheet(ImageUtilities.getImage("objects", "saw1"), 8, 3),
                        data.r, data.c, new CollisionBox(0.625, 0.3125, 2.75, 2.5));
                trap.setLightRadius(40);
                trap.setLightVisibility(200);
                return trap;
            case RoomObjectData.movingVerticalSaw:
                return new MovingSaw(new SpriteSheet(ImageUtilities.getImage("objects", "saw0"), 8, 3),
                        data.r, data.c, 0, data.a, data.b, new CollisionBox(0.375, 0.3125, 1.25, 1));
            case RoomObjectData.movingHorizontalSaw:
                return new MovingSaw(new SpriteSheet(ImageUtilities.getImage("objects", "saw0"), 8, 3),
                        data.r, data.c, 1, data.a, data.b, new CollisionBox(0.375, 0.3125, 1.25, 1));
            default:
                return null;
        }
    }

}

class Saw extends Trap {
    public Saw(SpriteSheet spriteSheet, int r, int c, CollisionBox hitbox) {
        super(spriteSheet, r, c, hitbox);
        if (spriteSheet.getHeight() / 16 == 1) {
            moveY((32 - spriteSheet.getHeight()) / 2);
        } else if (spriteSheet.getHeight() / 16 == 3) {
            moveY((64 - spriteSheet.getHeight()) / 2);
        }
        setOnFloor(true);
    }

    public void update() {
        getSpriteSheet().next();
    }
}

class MovingSaw extends Trap {
    private Saw saw;
    private int direction;
    private int speed;
    private int movementDirection;
    @Override
    public void drawComponent(Graphics2D g2d) {
        super.drawComponent(g2d);
        g2d.drawRect(0, 0, getWidth(), getHeight());
    }
    public MovingSaw(SpriteSheet sawSpriteSheet, int r, int c, int direction, int length, int speed,
            CollisionBox hitbox) {
        super(getBase(sawSpriteSheet, direction, length), r, c, null);
        this.direction = direction;
        this.speed = speed;
        setOnFloor(true);
        saw = new Saw(sawSpriteSheet, 0, 0, hitbox);
        saw.moveY((sawSpriteSheet.getHeight() - getHeight()) / 2);
        saw.setLightRadius(20);
        saw.setLightVisibility(200);
        add(saw);
        if (direction == 0) {
            int startingSawLocation = RNGUtilities.getInt(1, getHeight() - saw.getHeight());
            saw.setY(startingSawLocation);
        } else if (direction == 1) {
            int startingSawLocation = RNGUtilities.getInt(1, getWidth() - saw.getWidth());
            saw.setX(startingSawLocation);
            moveY((getHeight() - sawSpriteSheet.getHeight()) /2);
        }
        if (RNGUtilities.getBoolean()) {
            movementDirection = speed;
        } else {
            movementDirection = -speed;
        }
    }

    public void update() {
        if (direction == 0) {
            saw.moveY(movementDirection);
            if (saw.getY() < speed || saw.getY() + saw.getHeight() + speed > getHeight()) {
                movementDirection *= -1;
            }
        } else if (direction == 1) {
            saw.moveX(movementDirection);
            if (saw.getX() < speed || saw.getX() + saw.getWidth() + speed > getWidth()) {
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
                    length * spriteSheet.getWidth() + 2 * x0, sawSpriteSheet.getWidth(), Transparency.TRANSLUCENT);
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