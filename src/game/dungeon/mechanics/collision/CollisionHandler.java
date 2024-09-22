package game.dungeon.mechanics.collision;

import java.awt.Rectangle;

import game.dungeon.Dungeon;
import game.dungeon.room.entity.Entity;
import game.dungeon.room.entity.Player;
import game.dungeon.room.object_utilities.CollisionBox;
import game.dungeon.room.object_utilities.DirectionUtilities;
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

    public static boolean collides(int aX, int aY, Rectangle aH, int bX, int bY, Rectangle bH) {
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

    public static void handleCollision(Player player, Tile[][] collisionArray, int r, int c) {
        if (DirectionUtilities.getXMovement(player.getDirection()) == -1 && CollisionHandler.collides(player, collisionArray[r][c + 1], r, c + 1)) {
            handleCollision(player, (c + 1) * Dungeon.TILESIZE, r * Dungeon.TILESIZE, collisionArray[r][c + 1].getHitBox());
        } else {
            handleCollision(player, c * Dungeon.TILESIZE, r * Dungeon.TILESIZE, collisionArray[r][c].getHitBox());
        }
    }

    private static void handleCollision(Player player, int bX, int bY, Rectangle bH) {
        CollisionBox aH = player.getHitBox();
        double aX0 = (player.getX() + aH.getX());
        double aY0 = (player.getY() + aH.getY());
        double aX1 = (player.getX() + aH.getMaxX());
        double aY1 = (player.getY() + aH.getMaxY());

        double bX0 = (bX + bH.getX());
        double bY0 = (bY + bH.getY());
        double bX1 = (bX + bH.getMaxX());
        double bY1 = (bY + bH.getMaxY());

        if (player.getSpeedY() != 0 && player.getSpeedY() == 0) {
            if (bX0 < aX1 && aX1 < bX1) {
                player.setX(bX0 - aH.getMaxX());
            } else if (bX0 < aX0 && aX0 < bX1) {
                player.setX(bX1 - aH.getX());
            }
            player.setSpeedX(0);
            return;
        } else if (player.getSpeedX() == 0 && player.getSpeedY() != 0) {
            if (bY0 < aY1 && aY1 < bY1) {
                player.setY(bY0 - aH.getMaxY());
            } else if (bY0 < aY0 && aY0 < bY1) {
                player.setY(bY1 - aH.getY());
            }
            player.setSpeedY(0);
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
            player.setSpeedX(0);
        } else if (Math.abs(xDist) > Math.abs(yDist)) {
            player.moveY(yDist);
            player.setSpeedY(0);
        } else if (xDist != Integer.MAX_VALUE) {
            player.moveX(xDist);
            if (collides(player.getX(), player.getY(), aH, bX, bY, bH)) {
                player.moveX(-xDist);
                player.moveY(yDist);
                player.setSpeedY(0);
            } else {
                player.setSpeedX(0);
            }
        }
    }
}