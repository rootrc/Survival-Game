package game.dungeon.mechanics;

import java.util.ArrayList;

import game.dungeon.Dungeon;
import game.dungeon.room.entity.Entity;
import game.dungeon.room.entity.Player;
import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.RoomObject;
import game.dungeon.room.tile.Tile;

public class CollisionHandler {
    private Tile[][] tileGridArray;
    private boolean height[][];

    public CollisionHandler(Tile[][] tileGridArray, boolean[][] height) {
        this.tileGridArray = tileGridArray;
        this.height = height;
    }

    public static boolean collides(RoomObject a, RoomObject b) {
        return collides(a.getX(), a.getY(), a.getHitBox(), b.getX(), b.getY(), b.getHitBox());
    }

    public static boolean collides(Entity entity, Tile tile, int r, int c) {
        if (tile == null) {
            return false;
        }
        return collides(entity.getX(), entity.getY(), entity.getHitBox(), c * Dungeon.TILESIZE, r * Dungeon.TILESIZE,tile.getHitBox());
    }

    private static boolean collides(int aX, int aY, CollisionBox aH, int bX, int bY, CollisionBox bH) {
        if (aH == null || bH == null) {
            return false;
        }
        if (aH.getMinX() + aX < bH.getMaxX() + bX
                && aH.getMaxX() + aX > bH.getMinX() + bX
                && aH.getMinY() + aY < bH.getMaxY() + bY
                && aH.getMaxY() + aY > bH.getMinY() + bY) {
            return true;
        }
        return false;
    }

    public static void handleCollision(Player player, RoomObject roomObject) {
        handleCollision(player, roomObject.getX(), roomObject.getY(), roomObject.getHitBox());
    }

    public static void handleCollision(Player player, Tile tile, int r, int c) {
        if (tile == null) {
            return;
        }
        handleCollision(player, c * Dungeon.TILESIZE, r * Dungeon.TILESIZE, tile.getHitBox());
    }

    private static void handleCollision(Player player, int bX, int bY, CollisionBox bH) {
        CollisionBox aH = player.getHitBox();
        if (aH.getMinX() + player.getX() < bH.getMinX() + bX
                && aH.getMaxX() + player.getX() - bH.getMinX() - bX < player.getSpeedX()
                && (player.getSpeedX() > 0 || player.getMovementKeys().contains("d"))) {
            player.setX(bH.getMinX() + bX - aH.getMaxX());
            player.setSpeedX(0);
        }
        if (aH.getMinX() + player.getX() - bH.getMaxX() - bX > player.getSpeedX()
                && bH.getMaxX() + bX < aH.getMaxX() + player.getX()
                && (player.getSpeedX() < 0 || player.getMovementKeys().contains("a"))) {
            player.setX(bH.getMaxX() + bX - aH.getMinX());
            player.setSpeedX(0);
        }
        if (aH.getMinY() + player.getY() < bH.getMinY() + bY
                && aH.getMaxY() + player.getY() - bH.getMinY() - bY < player.getSpeedY()
                && (player.getSpeedY() > 0 || player.getMovementKeys().contains("s"))) {
            player.setY(bH.getMinY() + bY - aH.getMaxY());
            player.setSpeedY(0);
        }
        if (aH.getMinY() + player.getY() - bH.getMaxY() - bY > player.getSpeedY()
                && bH.getMaxY() + bY < aH.getMaxY() + player.getY()
                && (player.getSpeedY() < 0 || player.getMovementKeys().contains("w"))) {
            player.setY(bH.getMaxY() + bY - aH.getMinY());
            player.setSpeedY(0);
        }
    }

    public boolean handleCollision(Player player) {
        ArrayList<Integer> rows = new ArrayList<>();
        ArrayList<Integer> cols = new ArrayList<>();
        rows.add((int) ((player.getY() + player.getHitBox().getY()) / Dungeon.TILESIZE));
        rows.add((int) ((player.getY() + player.getHitBox().getMaxY()) / Dungeon.TILESIZE));
        cols.add((int) ((player.getX() + player.getHitBox().getX()) / Dungeon.TILESIZE));
        cols.add((int) ((player.getX() + player.getHitBox().getMaxX()) / Dungeon.TILESIZE));
        for (int r : rows) {
            for (int c : cols) {
                if (collides(player, tileGridArray[r][c], r, c)) {
                    CollisionHandler.handleCollision(player, tileGridArray[r][c], r, c);
                    return true;
                }
            }
        }
        return true;
    }

    public int getLayer(Entity entity) {
        if (height == null) {
            return 0;
        }
        ArrayList<Integer> rows = new ArrayList<>();
        ArrayList<Integer> cols = new ArrayList<>();
        rows.add((int) ((entity.getY() + entity.getHitBox().getY()) / Dungeon.TILESIZE));
        rows.add((int) ((entity.getY() + entity.getHitBox().getMaxY()) / Dungeon.TILESIZE));
        cols.add((int) ((entity.getX() + entity.getHitBox().getX()) / Dungeon.TILESIZE));
        cols.add((int) ((entity.getX() + entity.getHitBox().getMaxX()) / Dungeon.TILESIZE));
        for (int r : rows) {
            for (int c : cols) {
                if (height[r][c]) {
                    return 0;
                }
            }
        }
        return 1;
    }

    public boolean checkPoint(double x, double y) {
        // TODO
        return true;
        // int r = (int) (y / Dungeon.TILESIZE);
        // int c = (int) (x / Dungeon.TILESIZE);
        // return !collision[r][c];
    }

}