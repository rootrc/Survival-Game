package core.dungeon.room.object;

import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import core.dungeon.mechanics.collision.CollisionHandler;
import core.dungeon.room.Room;
import core.dungeon.room.entity.Player;
import core.dungeon.room.object_utilities.CollisionBox;
import core.dungeon.room.object_utilities.RoomObject;
import core.dungeon.room.object_utilities.SpriteSheet;
import core.dungeon.room.tile.Tile;
import core.dungeon.room.tile.TileGrid;
import core.utilities.ImageUtilities;
import core.utilities.RNGUtilities;

public abstract class Trap extends RoomObject {
    public Trap(SpriteSheet spriteSheet, int r, int c, CollisionBox hitbox) {
        super(spriteSheet, r, c, hitbox, null);
    }

    public Trap(int width, int height, int r, int c, CollisionBox hitbox) {
        super(width, height, r, c, hitbox, null);
    }

    public final void interaction(Player player) {
    }

    public static Trap getTrap(RoomObjectData data, Player player, TileGrid tileGrid) {
        Trap trap;
        switch (data.id) {
            case RoomObjectData.SAW_0:
                trap = new Saw(new SpriteSheet(ImageUtilities.getImage("objects", "saw0"), 8, 3),
                        data.r, data.c, CollisionBox.getCollisionBox(0.375, 0.3125, 1.25, 1));
                trap.setLightRadius(20);
                trap.setLightVisibility(150);
                return trap;
            case RoomObjectData.SAW_1:
                trap = new Saw(new SpriteSheet(ImageUtilities.getImage("objects", "saw1"), 8, 3),
                        data.r, data.c, CollisionBox.getCollisionBox(0.625, 0.3125, 2.75, 2.5));
                trap.setLightRadius(40);
                trap.setLightVisibility(200);
                return trap;
            case RoomObjectData.VERTICAL_SAW:
                return new MovingSaw(new SpriteSheet(ImageUtilities.getImage("objects", "saw0"), 8, 3),
                        data.r, data.c, 0, data.a, data.b, CollisionBox.getCollisionBox(0.375, 0.3125, 1.25, 1));
            case RoomObjectData.HORIZONTAL_SAW:
                return new MovingSaw(new SpriteSheet(ImageUtilities.getImage("objects", "saw0"), 8, 3),
                        data.r, data.c, 1, data.a, data.b, CollisionBox.getCollisionBox(0.375, 0.3125, 1.25, 1));
            case RoomObjectData.SPIKE:
                trap = new Spike(new SpriteSheet(ImageUtilities.getImage("objects", "spike"), 12, 6),
                        data.r - 3, data.c - 3, CollisionBox.getCollisionBox(3, 3, 2, 2), player);
                trap.setLightRadius(32);
                trap.setLightVisibility(200);
                return trap;
            case RoomObjectData.EXPLOSIVE:
                trap = new Explosive(new SpriteSheet(ImageUtilities.getImage("objects", "explosive"), 14, 6),
                        data.r - 2, data.c - 2, CollisionBox.getCollisionBox(2.5, 2.75, 1, 1),
                        CollisionBox.getCollisionBox(1, 1, 4, 4),
                        player, true);
                trap.setLightRadius(24);
                trap.setLightVisibility(150);
                return trap;
            case RoomObjectData.EXPLOSIVE_DUD:
                trap = new Explosive(new SpriteSheet(ImageUtilities.getImage("objects", "explosive"), 14, 6),
                        data.r - 2, data.c - 2, CollisionBox.getCollisionBox(2.5, 2.75, 1, 1),
                        CollisionBox.getCollisionBox(1, 1, 4, 4),
                        player, false);
                trap.setLightRadius(24);
                trap.setLightVisibility(150);
                return trap;
            case RoomObjectData.TELEPORTATION:
                trap = new Teleportation(new SpriteSheet(ImageUtilities.getImage("objects", "teleportation"), 5, 9),
                        data.r, data.c, CollisionBox.getCollisionBox(0.375, 0.375, 1.25, 1.25), tileGrid);
                trap.setLightRadius(24);
                trap.setLightVisibility(150);
                return trap;
            default:
                return null;
        }
    }

}

class Saw extends Trap {
    Saw(SpriteSheet spriteSheet, int r, int c, CollisionBox hitbox) {
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

    MovingSaw(SpriteSheet sawSpriteSheet, int r, int c, int direction, int length, int speed,
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
            int startingSawLocation = RNGUtilities.getInt(2, getHeight() - saw.getHeight() - 2);
            saw.setY(startingSawLocation);
            moveY(Tile.SIZE - sawSpriteSheet.getHeight() / 2);
        } else if (direction == 1) {
            int startingSawLocation = RNGUtilities.getInt(2, getWidth() - saw.getWidth() - 2);
            saw.setLocation(startingSawLocation, 0);
            moveX(Tile.SIZE - sawSpriteSheet.getWidth() / 2);
            moveY((getHeight() - Tile.SIZE) / 2);
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
            Graphics2D g2d = image.createGraphics();
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
            Graphics2D g2d = image.createGraphics();
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
    private static final int[] UPBLADE_LENGTHS = { 0, 0, 2, 0, 2, 2, 2, 38, 8, 6, 2, 0 };
    private static final int[] DOWNBLADE_LENGTHS = { 0, 0, 2, 0, 2, 2, 2, 36, 6, 4, 2, 0 };
    private static final int[] SIDEBLADE_LENGTHS = { 0, 2, 4, 2, 4, 4, 4, 44, 10, 8, 4, 0 };
    private static final int HITFRAME = 7;

    private Player player;
    private SawBlade upBlade;
    private SawBlade downBlade;
    private SawBlade leftBlade;
    private SawBlade rightBlade;

    Spike(SpriteSheet spriteSheet, int r, int c, CollisionBox hitbox, Player player) {
        super(spriteSheet, r, c, hitbox);
        this.player = player;
        upBlade = new SawBlade(24, UPBLADE_LENGTHS[HITFRAME], 0, 3,
                CollisionBox.getCollisionBox(0, (double) UPBLADE_LENGTHS[HITFRAME] / Tile.SIZE, 1.5, 0));
        upBlade.moveX(4);
        upBlade.moveY(12);
        downBlade = new SawBlade(24, DOWNBLADE_LENGTHS[HITFRAME], 5, 3, CollisionBox.getCollisionBox(0, 0, 1.5, 0));
        downBlade.moveX(4);
        leftBlade = new SawBlade(SIDEBLADE_LENGTHS[HITFRAME], 18, 3, 0,
                CollisionBox.getCollisionBox((double) SIDEBLADE_LENGTHS[HITFRAME] / Tile.SIZE, 0, 0, 1.125));
        leftBlade.moveX(4);
        leftBlade.moveY(6);
        rightBlade = new SawBlade(SIDEBLADE_LENGTHS[HITFRAME], 18, 3, 5, CollisionBox.getCollisionBox(0, 0, 0, 1.125));
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
        if (getSpriteSheet().getFrame() == 0 || getSpriteSheet().getCnt() != 0) {
            return;
        }
        downBlade.setHitbox(CollisionBox.getCollisionBox(0, 0, downBlade.getHitbox().getWidth() / Tile.SIZE,
                (double) DOWNBLADE_LENGTHS[getSpriteSheet().getFrame()] / Tile.SIZE));
        upBlade.setHitbox(CollisionBox.getCollisionBox(0,
                (double) (downBlade.getHeight() - UPBLADE_LENGTHS[getSpriteSheet().getFrame()]) / Tile.SIZE,
                upBlade.getHitbox().getWidth() / Tile.SIZE,
                (double) UPBLADE_LENGTHS[getSpriteSheet().getFrame()] / Tile.SIZE));
        rightBlade.setHitbox(CollisionBox.getCollisionBox(0, 0,
                (double) SIDEBLADE_LENGTHS[getSpriteSheet().getFrame()] / Tile.SIZE,
                (double) rightBlade.getHitbox().getHeight() / Tile.SIZE));
        leftBlade.setHitbox(CollisionBox.getCollisionBox(
                (double) (leftBlade.getWidth() - SIDEBLADE_LENGTHS[getSpriteSheet().getFrame()]) / Tile.SIZE, 0,
                (double) SIDEBLADE_LENGTHS[getSpriteSheet().getFrame()] / Tile.SIZE,
                leftBlade.getHitbox().getHeight() / Tile.SIZE));
    }

    public void collides(Player player) {
    }

    private boolean canHitPlayer() {
        int futurePlayerX = (int) (player.getX()
                + (HITFRAME + 0.5) * getSpriteSheet().getFrameLength() * player.getSpeedX());
        int futurePlayerY = (int) (player.getY()
                + (HITFRAME + 0.5) * getSpriteSheet().getFrameLength() * player.getSpeedY());
        if (CollisionHandler.collides(futurePlayerX, futurePlayerY, player.getHitbox(), getX() + upBlade.getX(),
                getY() + upBlade.getY(), new Rectangle(0, 0, upBlade.getWidth(), UPBLADE_LENGTHS[HITFRAME]))) {
            return true;
        }
        if (CollisionHandler.collides(futurePlayerX, futurePlayerY, player.getHitbox(), getX() + downBlade.getX(),
                getY() + downBlade.getY(), new Rectangle(0, 0, downBlade.getWidth(), DOWNBLADE_LENGTHS[HITFRAME]))) {
            return true;
        }
        if (CollisionHandler.collides(futurePlayerX, futurePlayerY, player.getHitbox(), getX() + leftBlade.getX(),
                getY() + leftBlade.getY(), new Rectangle(0, 0, SIDEBLADE_LENGTHS[HITFRAME], leftBlade.getHeight()))) {
            return true;
        }
        if (CollisionHandler.collides(futurePlayerX, futurePlayerY, player.getHitbox(), getX() + rightBlade.getX(),
                getY() + rightBlade.getY(), new Rectangle(0, 0, SIDEBLADE_LENGTHS[HITFRAME], rightBlade.getHeight()))) {
            return true;
        }

        return false;
    }

    private static class SawBlade extends Trap {
        public SawBlade(int width, int height, int r, int c, CollisionBox hitbox) {
            super(width, height, r, c, hitbox);
        }

        public void update() {
        }

        public void collides(Player player) {
            player.takeDamage();
        }
    }
}

class Explosive extends Trap {
    private static final int HITFRAME = 6;

    private Player player;
    private Explosion explosion;
    private CollisionBox explosionHitbox;
    private CollisionBox playerDetectionHitbox;

    Explosive(SpriteSheet spriteSheet, int r, int c, CollisionBox hitbox, CollisionBox explosionHitbox,
            Player player, boolean isWorking) {
        super(spriteSheet, r, c, hitbox);
        this.explosionHitbox = explosionHitbox;
        this.player = player;
        playerDetectionHitbox = CollisionBox.getCollisionBox(explosionHitbox.getX() / Tile.SIZE - 0.5, explosionHitbox.getY() / Tile.SIZE - 0.5,
                explosionHitbox.getWidth() / Tile.SIZE + 1, explosionHitbox.getHeight() / Tile.SIZE + 1);
        if (isWorking) {
            explosion = new Explosion(getWidth(), getHeight());
            add(explosion);
        }
    }

    public void update() {
        if ((canHitPlayer() || getSpriteSheet().getFrame() != 0)
                && getSpriteSheet().getFrame() != getSpriteSheet().getFrameCnt() - 1 && explosion != null) {
            getSpriteSheet().next();
            if (getSpriteSheet().getFrame() == 0 || getSpriteSheet().getCnt() != 0) {
                return;
            }
            if (getSpriteSheet().getFrame() == HITFRAME) {
                explosion.setHitbox(explosionHitbox);
                setHitbox(null);
                Room.setScreenShakeDuration(10);
                Room.setScreenShakeStrength(10);
            } else if (getSpriteSheet().getFrame() == HITFRAME + 2) {
                explosion.setHitbox(null);
                setLightRadius(0);
            }
        }
    }

    public void collides(Player player) {
    }

    private boolean canHitPlayer() {
        return CollisionHandler.collides(player.getX(), player.getY(), player.getHitbox(), getX(), getY(),
                playerDetectionHitbox);
    }

    private static class Explosion extends Trap {
        public Explosion(int width, int height) {
            super(width, height, 0, 0, null);
        }

        public void update() {
        }

        public void collides(Player player) {
            setHitbox(null);
            player.takeDamage();
        }
    }

}

class Teleportation extends Trap {
    private TileGrid tileGrid;

    Teleportation(SpriteSheet spriteSheet, int r, int c, CollisionBox hitbox,
            TileGrid tileGrid) {
        super(spriteSheet, r, c, hitbox);
        this.tileGrid = tileGrid;
    }

    public void update() {
        if (getHitbox() != null) {
            getSpriteSheet().next();
            if (getSpriteSheet().getFrame() == getSpriteSheet().getFrameCnt() - 1) {
                getSpriteSheet().nextFrame();
            }
        }
    }

    public void collides(Player player) {
        if (getSpriteSheet().getFrame() == getSpriteSheet().getFrameCnt() - 1) {
            return;
        }
        int r, c;
        while (true) {
            r = RNGUtilities.getInt(tileGrid.getN());
            c = RNGUtilities.getInt(tileGrid.getM());
            if ((r < player.getMinRow() - tileGrid.getN() / 5 || player.getMaxRow() + tileGrid.getN() / 5 < r) &&
                    (c < player.getMinCol() - tileGrid.getM() / 5 || player.getMaxCol() + tileGrid.getM() / 5 < c) &&
                    tileGrid.getPathFinder().isReachable(player.getMinRow(), player.getMinCol(), r, c)) {
                break;
            }
        }

        player.setLocation(c * Tile.SIZE - player.getHitbox().getX(),
                r * Tile.SIZE - player.getHitbox().getY());
        Room.setScreenShakeDuration(10);
        Room.setScreenShakeStrength(10);
        setHitbox(null);
        setLightRadius(0);
        getSpriteSheet().setFrame(getSpriteSheet().getFrameCnt() - 1);
    }
}