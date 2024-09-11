package game.dungeon.room.object_utilities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.Game;
import game.dungeon.Dungeon;
import game.dungeon.room.entity.Player;
import game.dungeon.room.object.Ladder;
import game.dungeon.room.object.LightRock;
import game.dungeon.room.object.Trap;
import game.dungeon.room.object.TreasureChest;
import game.game_components.GameComponent;

public abstract class RoomObject extends GameComponent {
    private SpriteSheet spriteSheet;
    private CollisionBox interactbox;
    private CollisionBox hitbox;

    private double lightRadius;

    protected RoomObject(SpriteSheet spriteSheet, int r, int c, CollisionBox hitbox, CollisionBox interactbox) {
        super(spriteSheet.getWidth(), spriteSheet.getHeight());
        this.spriteSheet = spriteSheet;
        setLocation(c * Dungeon.TILESIZE, r * Dungeon.TILESIZE);
        this.hitbox = hitbox;
        this.interactbox = interactbox;
    }

    protected RoomObject(BufferedImage image, int r, int c, CollisionBox hitbox, CollisionBox interactbox) {
        this(new SpriteSheet(image), r, c, hitbox, interactbox);
    }

    protected RoomObject(SpriteSheet spriteSheet, int r, int c, CollisionBox hitbox) {
        this(spriteSheet, r, c, hitbox, hitbox);
    }

    protected RoomObject(BufferedImage image, int r, int c, CollisionBox hitbox) {
        this(new SpriteSheet(image), r, c, hitbox, hitbox);
    }

    public void drawComponent(Graphics2D g2d) {
        g2d.drawImage(spriteSheet.getImage(), 0, 0, null);
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

    public abstract void collides(Player player);

    public abstract void interaction(Player player);

    public final boolean interacts(RoomObject object) {
        CollisionBox h1 = interactbox;
        CollisionBox h2 = object.getInteractbox();
        if (h1 == null || h2 == null) {
            return false;
        }
        if (h1.getX() + getX() < h2.getMaxX() + object.getX()
                && h1.getMaxX() + getX() > h2.getX() + object.getX()
                && h1.getY() + getY() < h2.getMaxY() + object.getY()
                && h1.getMaxY() + getY() > h2.getY() + object.getY()) {
            return true;
        }
        return false;
    }

    public final SpriteSheet getSpriteSheet() {
        return spriteSheet;
    }

    public final CollisionBox getInteractbox() {
        return interactbox;
    }

    public final CollisionBox getHitBox() {
        return hitbox;
    }

    public final int getMinRow() {
        return (int) ((getY() + hitbox.getY()) / Dungeon.TILESIZE);
    }

    public final int getMinCol() {
        return (int) ((getX() + hitbox.getX()) / Dungeon.TILESIZE);
    }

    public final int getMaxRow() {
        return (int) ((getY() + hitbox.getMaxY() - 1) / Dungeon.TILESIZE);
    }

    public final int getMaxCol() {
        return (int) ((getX() + hitbox.getMaxX() - 1) / Dungeon.TILESIZE);
    }

    public final double getLightRadius() {
        return lightRadius;
    }

    public final void setLightRadius(double lightRadius) {
        this.lightRadius = lightRadius;
    }

    public final void addLightRadius(double delta) {
        lightRadius += delta;
    }

    public final double getDistanceFromRoomObject(RoomObject o) {
        return Math.sqrt((getX() + getHitBox().getCenterX() - o.getX() - o.getHitBox().getCenterX())
                * (getX() + getHitBox().getCenterX() - o.getX() - o.getHitBox().getCenterX())
                + (getY() + getHitBox().getCenterY() - o.getY() - o.getHitBox().getCenterY())
                        * (getY() + getHitBox().getCenterY() - o.getY() - o.getHitBox().getCenterY()));
    }

    public static RoomObject getRoomObject(RoomObjectData data) {
        if (data.id <= RoomObjectData.ladderDown) {
            return Ladder.getLadder(data);
        } else if (RoomObjectData.treasureChest0 <= data.id && data.id <= RoomObjectData.treasureChest4) {
            return TreasureChest.getTreasureChest(data);
        } else if (RoomObjectData.smallLightRock <= data.id && data.id <= RoomObjectData.largeLightRock) {
            return LightRock.getLightRock(data);
        } else if (RoomObjectData.saw0 <= data.id) {
            return Trap.getTrap(data);
        }
        return null;
    }

    public static class RoomObjectData {
        public static final int ladderUp = 0;
        public static final int ladderDown = 1;
        public static final int treasureChest0 = 10;
        public static final int treasureChest1 = 11;
        public static final int treasureChest2 = 12;
        public static final int treasureChest3 = 13;
        public static final int treasureChest4 = 14;
        public static final int smallLightRock = 15;
        public static final int mediumLightRock = 16;
        public static final int largeLightRock = 17;
        public static final int saw0 = 20;
        public static final int saw1 = 21;

        public int id;
        public int r;
        public int c;

        public RoomObjectData(int id, int r, int c) {
            this.id = id;
            this.r = r;
            this.c = c;
        }
    }

}
