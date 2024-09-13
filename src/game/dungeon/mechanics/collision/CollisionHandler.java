package game.dungeon.mechanics.collision;

import game.dungeon.Dungeon;
import game.dungeon.room.entity.Entity;
import game.dungeon.room.entity.Player;
import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.RoomObject;
import game.dungeon.room.tile.Tile;

public class CollisionHandler {

    private CollisionHandler() {
    }

    public static boolean collides(RoomObject a, RoomObject b) {
        if (b == null) {
            return false;
        }
        return collides(a.getX(), a.getY(), a.getHitBox(), b.getX(), b.getY(), b.getHitBox());
    }

    public static boolean collides(Entity entity, Tile tile, int r, int c) {
        if (tile == null) {
            return false;
        }
        return collides(entity.getX(), entity.getY(), entity.getHitBox(), c * Dungeon.TILESIZE, r * Dungeon.TILESIZE,
                tile.getHitBox());
    }

    private static boolean collides(int aX, int aY, CollisionBox aH, int bX, int bY, CollisionBox bH) {
        if (aH == null || bH == null) {
            return false;
        }
        if (aH.getX() + aX < bH.getMaxX() + bX
                && aH.getMaxX() + aX > bH.getX() + bX
                && aH.getY() + aY < bH.getMaxY() + bY
                && aH.getMaxY() + aY > bH.getY() + bY) {
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
        int aX0 = (int) (player.getX() + aH.getX());
        int aY0 = (int) (player.getY() + aH.getY());
        int aX1 = (int) (player.getX() + aH.getMaxX());
        int aY1 = (int) (player.getY() + aH.getMaxY());

        int bX0 = (int) (bX + bH.getX());
        int bY0 = (int) (bY + bH.getY());
        int bX1 = (int) (bX + bH.getMaxX());
        int bY1 = (int) (bY + bH.getMaxY());

        if (player.getSpeedY() != 0 && player.getSpeedY() == 0) {
            if (bX0 < aX1 && aX1 < bX1) {
                player.setX(bX0 - aH.getMaxX());
            } else if (bX0 < aX0 && aX0 < bX1) {
                player.setX(bX1 - aH.getX());
            }
            return;
        } else if (player.getSpeedX() == 0 && player.getSpeedY() != 0) {
            if (bY0 < aY1 && aY1 < bY1) {
                player.setY(bY0 - aH.getMaxY());
            } else if (bY0 < aY0 && aY0 < bY1) {
                player.setY(bY1 - aH.getY());
            }
            return;
        }
        double xDist = Integer.MAX_VALUE;
        if (bX0 < aX1 && aX1 < bX1) {
            xDist = bX0 - aH.getMaxX() - player.getX();
        } else if (bX0 < aX0 && aX0 < bX1) {
            xDist = bX1 - aH.getX() - player.getX();
        }
        double yDist = Integer.MAX_VALUE;
        if (bY0 < aY1 && aY1 < bY1) {
            yDist = bY0 - aH.getMaxY() - player.getY();
        } else if (bY0 < aY0 && aY0 < bY1) {
            yDist = bY1 - aH.getY() - player.getY();
        }
        if (Math.abs(xDist) < Math.abs(yDist)) {
            player.moveX(xDist);
        } else if (Math.abs(xDist) > Math.abs(yDist)) {
            player.moveY(yDist);
        } else if (xDist != Integer.MAX_VALUE) {   
            player.moveX(xDist);
            if (collides(player.getX(), player.getY(), aH, bX, bY, bH)) {
                player.moveX(xDist);
                player.moveY(yDist);
            }
        }
        if (collides(player.getX(), player.getY(), aH, bX, bY, bH)) {
            player.moveX(-player.getSpeedX());
            player.moveY(-player.getSpeedY());
        }
    }
}