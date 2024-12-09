package core.dungeon.room.object_utilities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;

import core.Game;
import core.dungeon.room.entity.Player;
import core.dungeon.room.object.Ladder;
import core.dungeon.room.object.LightRock;
import core.dungeon.room.object.Trap;
import core.dungeon.room.object.TreasureChest;
import core.dungeon.room.tile.Tile;
import core.dungeon.room.tile.TileGrid;
import core.game_components.GameComponent;

public abstract class RoomObject extends GameComponent implements Comparable<RoomObject> {
    private SpriteSheet spriteSheet;
    private CollisionBox interactbox;
    private CollisionBox hitbox;
    private boolean onFloor;

    private double lightRadius;
    private double lightVisibility;

    protected RoomObject(int width, int height, int r, int c, CollisionBox hitbox, CollisionBox interactbox) {
        super(width, height);
        setLocation(c * Tile.SIZE, r * Tile.SIZE);
        this.hitbox = hitbox;
        this.interactbox = interactbox;
        setLightVisibility(100);
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
            g2d.setColor(Color.BLUE);
            g2d.drawRect((int) (interactbox.getX()), (int) (interactbox.getY()),
                    (int) interactbox.getWidth(), (int) interactbox.getHeight());
        }
        g2d.setColor(Color.RED);
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
        replaceSpriteSheet(new SpriteSheet(bufferedImage, this.spriteSheet.getFrameCnt(),
                this.spriteSheet.getTypeCnt(), this.spriteSheet.getFrameLength()));
    }

    public final void replaceSpriteSheet(SpriteSheet spriteSheet) {
        spriteSheet.set(this.spriteSheet);
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
            return (int) (getY() / Tile.SIZE);
        }
        return (int) ((getY() + hitbox.getY()) / Tile.SIZE);
    }

    public final int getMinCol() {
        if (hitbox == null) {
            return (int) (getX() / Tile.SIZE);
        }
        return (int) ((getX() + hitbox.getX()) / Tile.SIZE);
    }

    public final int getMaxRow() {
        if (hitbox == null) {
            return (int) ((getY() - 1) / Tile.SIZE);
        }
        return (int) ((getY() + hitbox.getMaxY() - 1) / Tile.SIZE);
    }

    public final int getMaxCol() {
        if (hitbox == null) {
            return (int) ((getX() - 1) / Tile.SIZE);
        }
        return (int) ((getX() + hitbox.getMaxX() - 1) / Tile.SIZE);
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

    @Override
    public int compareTo(RoomObject o) {
        if (isOnFloor() && !o.isOnFloor()) {
            return 1;
        } else if (!isOnFloor() && o.isOnFloor()) {
            return -1;
        }
        double ay = getHeight() / 2;
        double by = o.getHeight() / 2;
        if (getHitbox() != null) {
            ay = getHitbox().getY();
        }
        if (o.getHitbox() != null) {
            by = o.getHitbox().getY();
        }
        if (getY() + ay < o.getY() + by) {
            return 1;
        }
        if (getY() + ay > o.getY() + by) {
            return -1;
        }
        return 0;
    }

    public static RoomObject getRoomObject(int depth, RoomObjectData data, Player player, TileGrid tileGrid) {
        if (data.id <= RoomObjectData.LADDER_DOWN) {
            return Ladder.getLadder(data);
        } else if (RoomObjectData.TREASURE_CHEST_0 <= data.id && data.id <= RoomObjectData.TREASURE_CHEST_4) {
            return TreasureChest.getTreasureChest(depth, data);
        } else if (RoomObjectData.SMALL_LIGHTROCK <= data.id && data.id <= RoomObjectData.LARGE_LIGHTROCK) {
            return LightRock.getLightRock(depth, data);
        } else if (RoomObjectData.SAW_0 <= data.id) {
            return Trap.getTrap(depth, data, player, tileGrid);
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