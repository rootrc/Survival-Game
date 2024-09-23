package game.dungeon.room.object;

import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import game.dungeon.mechanics.collision.CollisionHandler;
import game.dungeon.room.Room;
import game.dungeon.room.entity.Player;
import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.RoomObject;
import game.dungeon.room.object_utilities.SpriteSheet;
import game.utilities.ImageUtilities;
import game.utilities.RNGUtilities;

public abstract class Trap extends RoomObject {
    public Trap(SpriteSheet spriteSheet, int r, int c, CollisionBox hitbox) {
        super(spriteSheet, r, c, hitbox, null);
    }

    public void interaction(Player player) {
    }

    public static Trap getTrap(RoomObjectData data, Player player) {
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
            case RoomObjectData.spike:
                trap = new Spike(new SpriteSheet(ImageUtilities.getImage("objects", "spike"), 12, 6),
                        data.r - 3, data.c - 3, new CollisionBox(3, 3, 2, 2), player);
                trap.setLightRadius(32);
                trap.setLightVisibility(150);
                return trap;
            case RoomObjectData.explosive:
                trap = new Explosive(new SpriteSheet(ImageUtilities.getImage("objects", "explosive"), 14, 6),
                        data.r - 3, data.c - 3, player);
                trap.setLightRadius(24);
                trap.setLightVisibility(150);
                return trap;
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

    public void collides(Player player) {
        player.takeDamage();
    }
}

class MovingSaw extends Trap {
    private Saw saw;
    private int direction;
    private int speed;
    private int movementDirection;

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
            moveY((getHeight() - sawSpriteSheet.getHeight()) / 2);
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

    public void collides(Player player) {
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

class Spike extends Trap {
    private Player player;
    private SawBlade upBlade;
    private SawBlade downBlade;
    private SawBlade leftBlade;
    private SawBlade rightBlade;
    private static final int[] upBladeLengths = { 0, 0, 2, 0, 2, 2, 2, 38, 8, 6, 2, 0 };
    private static final int[] downBladeLengths = { 0, 0, 2, 0, 2, 2, 2, 36, 6, 4, 2, 0 };
    private static final int[] sideBladeLengths = { 0, 2, 4, 2, 4, 4, 4, 44, 10, 8, 4, 0 };

    private static final int hitFrame = 7;

    public Spike(SpriteSheet spriteSheet, int r, int c, CollisionBox hitbox, Player player) {
        super(spriteSheet, r, c, hitbox);
        this.player = player;
        upBlade = new SawBlade(24, upBladeLengths[hitFrame], 0, 3, new CollisionBox(0, 0, 1.5, 0));
        upBlade.moveX(4);
        upBlade.moveY(12);
        downBlade = new SawBlade(24, downBladeLengths[hitFrame], 5, 3, new CollisionBox(0, 0, 1.5, 0));
        downBlade.moveX(4);
        leftBlade = new SawBlade(sideBladeLengths[hitFrame], 18, 3, 0, new CollisionBox(0, 0, 0, 1.125));
        leftBlade.moveX(4);
        leftBlade.moveY(6);
        rightBlade = new SawBlade(sideBladeLengths[hitFrame], 18, 3, 5, new CollisionBox(0, 0, 0, 1.125));
        rightBlade.moveY(6);
        add(upBlade);
        add(downBlade);
        add(leftBlade);
        add(rightBlade);
    }

    public void update() {
        if (canHitPlayer() || getSpriteSheet().getFrame() != 0) {
            getSpriteSheet().next();
        }
        if (!getSpriteSheet().isNewFrame()) {
            return;
        }
        downBlade.getHitBox().setBounds(0, 0, (int) downBlade.getHitBox().getWidth(),
                downBladeLengths[getSpriteSheet().getFrame()]);
        upBlade.getHitBox().setBounds(0, downBlade.getHeight() - upBladeLengths[getSpriteSheet().getFrame()],
                (int) upBlade.getHitBox().getWidth(), upBladeLengths[getSpriteSheet().getFrame()]);
        rightBlade.getHitBox().setBounds(0, 0, sideBladeLengths[getSpriteSheet().getFrame()],
                (int) rightBlade.getHitBox().getHeight());
        leftBlade.getHitBox().setBounds(leftBlade.getWidth() - sideBladeLengths[getSpriteSheet().getFrame()], 0,
                sideBladeLengths[getSpriteSheet().getFrame()], (int) leftBlade.getHitBox().getHeight());
    }

    public void collides(Player player) {
    }

    private boolean canHitPlayer() {
        int futurePlayerX = (int) (player.getX()
                + (hitFrame + 0.5) * getSpriteSheet().getFrameLength() * player.getSpeedX());
        int futurePlayerY = (int) (player.getY()
                + (hitFrame + 0.5) * getSpriteSheet().getFrameLength() * player.getSpeedY());
        if (CollisionHandler.collides(futurePlayerX, futurePlayerY, player.getHitBox(), getX() + upBlade.getX(),
                getY() + upBlade.getY(), new Rectangle(0, 0, upBlade.getWidth(), upBladeLengths[hitFrame]))) {
            return true;
        }
        if (CollisionHandler.collides(futurePlayerX, futurePlayerY, player.getHitBox(), getX() + downBlade.getX(),
                getY() + downBlade.getY(), new Rectangle(0, 0, downBlade.getWidth(), downBladeLengths[hitFrame]))) {
            return true;
        }
        if (CollisionHandler.collides(futurePlayerX, futurePlayerY, player.getHitBox(), getX() + leftBlade.getX(),
                getY() + leftBlade.getY(), new Rectangle(0, 0, sideBladeLengths[hitFrame], leftBlade.getHeight()))) {
            return true;
        }
        if (CollisionHandler.collides(futurePlayerX, futurePlayerY, player.getHitBox(), getX() + rightBlade.getX(),
                getY() + rightBlade.getY(), new Rectangle(0, 0, sideBladeLengths[hitFrame], rightBlade.getHeight()))) {
            return true;
        }

        return false;
    }

    private class SawBlade extends RoomObject {
        public SawBlade(int width, int height, int r, int c, CollisionBox hitbox) {
            super(new BufferedImage(width, height, Transparency.BITMASK), r, c, hitbox, null);
        }

        public void update() {
        }

        public void collides(Player player) {
            player.takeDamage();
        }

        public void interaction(Player player) {
        }

    }
}

class Explosive extends Trap {
    private Player player;
    private static final int hitFrame = 6;

    public Explosive(SpriteSheet spriteSheet, int r, int c, Player player) {
        super(spriteSheet, r, c, null);
        this.player = player;
    }

    public void update() {
        if ((canHitPlayer() || getSpriteSheet().getFrame() != 0)
                && getSpriteSheet().getFrame() != getSpriteSheet().getFrameCnt() - 1) {
            getSpriteSheet().next();
            if (!getSpriteSheet().isNewFrame()) {
                return;
            }
            if (getSpriteSheet().getFrame() == hitFrame) {
                setHitBox(hitbox);
                Room.setScreenShakeDuration(10);
                Room.setScreenShakeStrength(10);
            } else if (getSpriteSheet().getFrame() == hitFrame + 3) {
                setHitBox(null);
                setLightRadius(0);
            }
        }
    }

    private CollisionBox hitbox = new CollisionBox(1, 1, 4, 4);

    private boolean canHitPlayer() {
        int futurePlayerX = (int) (player.getX());
        int futurePlayerY = (int) (player.getY());
        if (CollisionHandler.collides(futurePlayerX, futurePlayerY, player.getHitBox(), getX(),
                getY(), hitbox)) {
            return true;
        }
        return false;
    }

    public void collides(Player player) {
        player.takeDamage();
    }

}