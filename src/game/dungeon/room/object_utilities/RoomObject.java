package game.dungeon.room.object_utilities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;

import game.Game;
import game.dungeon.Dungeon;
import game.dungeon.room.entity.Player;
import game.dungeon.room.object.Ladder;
import game.dungeon.room.object.LightRock;
import game.dungeon.room.object.Trap;
import game.dungeon.room.object.TreasureChest;
import game.dungeon.room.tile.TileGrid;
import game.game_components.GameComponent;

public abstract class RoomObject extends GameComponent {
    private SpriteSheet spriteSheet;
    private CollisionBox interactbox;
    private CollisionBox hitbox;
    private boolean onFloor;

    private double lightRadius;
    private double lightVisibility;

    protected RoomObject(int width, int height, int r, int c, CollisionBox hitbox, CollisionBox interactbox) {
        super(width, height);
        setLocation(c * Dungeon.TILESIZE, r * Dungeon.TILESIZE);
        this.hitbox = hitbox;
        this.interactbox = interactbox;
        setLightVisibility(10000);
    }

    protected RoomObject(SpriteSheet spriteSheet, int r, int c, CollisionBox hitbox, CollisionBox interactbox) {
        this(spriteSheet.getWidth(), spriteSheet.getHeight(), r, c, hitbox, interactbox);
        this.spriteSheet = spriteSheet;
    }

    protected RoomObject(BufferedImage image, int r, int c, CollisionBox hitbox, CollisionBox interactbox) {
        this(new SpriteSheet(image), r, c, hitbox, interactbox);
    }

    public void drawComponent(Graphics2D g2d) {
        if (spriteSheet != null) {
            g2d.drawImage(spriteSheet.getImage(), 0, 0, null);
        }
        if (!Game.DEBUG) {
            return;
        }
        if (interactbox != null) {
            g2d.setColor(Color.blue);
            g2d.drawRect((int) (interactbox.getX()), (int) (interactbox.getY()),
                    (int) interactbox.getWidth(), (int) interactbox.getHeight());
        }
        g2d.setColor(Color.red);
        if (hitbox != null) {
            g2d.drawRect((int) (hitbox.getX()), (int) (hitbox.getY()),
            (int) hitbox.getWidth(), (int) hitbox.getHeight());
        }
    }

    public abstract void collides(Player player);

    public abstract void interaction(Player player);

    public final boolean interacts(RoomObject object) {
        if (object == null) {
            return false;
        }
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

    public final void replaceSpriteSheet(BufferedImage bufferedImage) {
        replaceSpriteSheet(new SpriteSheet(bufferedImage, this.spriteSheet.getFrameCnt(), this.spriteSheet.getDirectionCnt(), this.spriteSheet.getFrameLength()));
    }
    
    public final void replaceSpriteSheet(SpriteSheet spriteSheet) {
        spriteSheet.setFrame(this.spriteSheet.getFrame());
        spriteSheet.setDirection(this.spriteSheet.getDirection());
        this.spriteSheet = spriteSheet;
    }

    public final CollisionBox getInteractbox() {
        return interactbox;
    }

    public final CollisionBox getHitbox() {
        return hitbox;
    }

    public final void setHitbox(CollisionBox hitbox) {
        this.hitbox = hitbox;
    }

    public final int getMinRow() {
        if (hitbox == null) {
            return (int) (getY() / Dungeon.TILESIZE);
        }
        return (int) ((getY() + hitbox.getY()) / Dungeon.TILESIZE);
    }
    
    public final int getMinCol() {
        if (hitbox == null) {
            return (int) (getX() / Dungeon.TILESIZE);
        }
        return (int) ((getX() + hitbox.getX()) / Dungeon.TILESIZE);
    }
    
    public final int getMaxRow() {
        if (hitbox == null) {
            return (int) ((getY() - 1) / Dungeon.TILESIZE);
        }
        return (int) ((getY() + hitbox.getMaxY() - 1) / Dungeon.TILESIZE);
    }
    
    public final int getMaxCol() {
        if (hitbox == null) {
            return (int) ((getX() - 1) / Dungeon.TILESIZE);
        }
        return (int) ((getX() + hitbox.getMaxX() - 1) / Dungeon.TILESIZE);
    }

    public final boolean isOnFloor() {
        return onFloor;
    }

    public final void setOnFloor(boolean onFloor) {
        this.onFloor = onFloor;
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

    public final void setLightVisibility(double lightVisibility) {
        this.lightVisibility = lightVisibility;
    }

    public final double getLightVisibility() {
        return lightVisibility;
    }

    public final double getDistanceFromRoomObject(RoomObject o) {
        if (hitbox == null || o.getHitbox() == null) {
            return getDistance(o);
        }
        return Math.sqrt((getX() + getHitbox().getCenterX() - o.getX() - o.getHitbox().getCenterX())
                * (getX() + getHitbox().getCenterX() - o.getX() - o.getHitbox().getCenterX())
                + (getY() + getHitbox().getCenterY() - o.getY() - o.getHitbox().getCenterY())
                        * (getY() + getHitbox().getCenterY() - o.getY() - o.getHitbox().getCenterY()));
    }

    public static RoomObject getRoomObject(RoomObjectData data, Player player, TileGrid tileGrid) {
        if (data.id <= RoomObjectData.LADDER_DOWN) {
            return Ladder.getLadder(data);
        } else if (RoomObjectData.TREASURE_CHEST_0 <= data.id && data.id <= RoomObjectData.TREASURE_CHEST_4) {
            return TreasureChest.getTreasureChest(data);
        } else if (RoomObjectData.SMALL_LIGHTROCK <= data.id && data.id <= RoomObjectData.LARGE_LIGHTROCK) {
            return LightRock.getLightRock(data);
        } else if (RoomObjectData.SAW_0 <= data.id) {
            return Trap.getTrap(data, player, tileGrid);
        }
        return null;
    }

    public static class RoomObjectData {
        public static final int LADDER_UP = 0;
        public static final int LADDER_DOWN = 1;
        public static final int TREASURE_CHEST_0 = 10;
        public static final int TREASURE_CHEST_1 = 11;
        public static final int TREASURE_CHEST_2 = 12;
        public static final int TREASURE_CHEST_3 = 13;
        public static final int TREASURE_CHEST_4 = 14;
        public static final int SMALL_LIGHTROCK = 15;
        public static final int MEDIUM_LIGHTROCK = 16;
        public static final int LARGE_LIGHTROCK = 17;
        public static final int SAW_0 = 20;
        public static final int SAW_1 = 21;
        public static final int VERTICAL_SAW = 22;
        public static final int HORIZONTAL_SAW = 23;
        public static final int SPIKE = 24;
        public static final int EXPLOSIVE = 25;
        public static final int EXPLOSIVE_DUD = 26;
        public static final int TELEPORTATION = 27;

        public static final HashSet<Integer> fourSet = new HashSet<>() {
            {

            }
        };

        public static final HashSet<Integer> fiveSet = new HashSet<>() {
            {
                add(VERTICAL_SAW);
                add(HORIZONTAL_SAW);
            }
        };

        public int id;
        public int r;
        public int c;
        public int a;
        public int b;

        public RoomObjectData(int id, int r, int c, int a, int b) {
            this.id = id;
            this.r = r;
            this.c = c;
            this.a = a;
            this.b = b;
        }

        public RoomObjectData(int id, int r, int c, int a) {
            this(id, r, c, a, 0);
        }

        public RoomObjectData(int id, int r, int c) {
            this(id, r, c, 0, 0);
        }

    }

}
