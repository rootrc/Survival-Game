package game.dungeon.mechanics.collision;

import java.awt.Rectangle;

import game.dungeon.Dungeon;
import game.dungeon.room.entity.Entity;
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
        return collides(a.getX(), a.getY(), a.getHitbox(), b.getX(), b.getY(), b.getHitbox());
    }

    public static boolean collides(Entity entity, Tile tile, int r, int c) {
        if (tile == null) {
            return false;
        }
        return collides(entity.getX(), entity.getY(), entity.getHitbox(), c * Dungeon.TILESIZE, r * Dungeon.TILESIZE,
                tile.getHitbox());
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

    public static void handleCollision(Entity entity, RoomObject roomObject) {
        handleCollision(entity, roomObject.getX(), roomObject.getY(), roomObject.getHitbox());
    }

    public static void handleCollision(Entity entity, Tile[][] collisionArray, int r, int c) {
        if (DirectionUtilities.getXMovement(entity.getDirection()) == -1 && CollisionHandler.collides(entity, collisionArray[r][c + 1], r, c + 1)) {
            handleCollision(entity, (c + 1) * Dungeon.TILESIZE, r * Dungeon.TILESIZE, collisionArray[r][c + 1].getHitbox());
        } else {
            handleCollision(entity, c * Dungeon.TILESIZE, r * Dungeon.TILESIZE, collisionArray[r][c].getHitbox());
        }
    }

    private static void handleCollision(Entity entity, int bX, int bY, Rectangle bH) {
        if (bH == null) {
            return;
        }
        CollisionBox aH = entity.getHitbox();
        double aX0 = (entity.getX() + aH.getX());
        double aY0 = (entity.getY() + aH.getY());
        double aX1 = (entity.getX() + aH.getMaxX());
        double aY1 = (entity.getY() + aH.getMaxY());

        double bX0 = (bX + bH.getX());
        double bY0 = (bY + bH.getY());
        double bX1 = (bX + bH.getMaxX());
        double bY1 = (bY + bH.getMaxY());

        if (entity.getSpeedY() != 0 && entity.getSpeedY() == 0) {
            if (bX0 < aX1 && aX1 < bX1) {
                entity.setX(bX0 - aH.getMaxX());
            } else if (bX0 < aX0 && aX0 < bX1) {
                entity.setX(bX1 - aH.getX());
            }
            entity.setSpeedX(0);
            return;
        } else if (entity.getSpeedX() == 0 && entity.getSpeedY() != 0) {
            if (bY0 < aY1 && aY1 < bY1) {
                entity.setY(bY0 - aH.getMaxY());
            } else if (bY0 < aY0 && aY0 < bY1) {
                entity.setY(bY1 - aH.getY());
            }
            entity.setSpeedY(0);
            return;
        }
        double xDist = Integer.MAX_VALUE;
        if (bX0 < aX1 && aX1 < bX1) {
            xDist = bX0 - aH.getMaxX() - entity.getX();
        } else if (bX0 < aX0 && aX0 < bX1) {
            xDist = bX1 - aH.getX() - entity.getX();
        }
        double yDist = Integer.MAX_VALUE;
        if (bY0 < aY1 && aY1 < bY1) {
            yDist = bY0 - aH.getMaxY() - entity.getY();
        } else if (bY0 < aY0 && aY0 < bY1) {
            yDist = bY1 - aH.getY() - entity.getY();
        }
        if (Math.abs(xDist) < Math.abs(yDist)) {
            entity.moveX(xDist);
            entity.setSpeedX(0);
        } else if (Math.abs(xDist) > Math.abs(yDist)) {
            entity.moveY(yDist);
            entity.setSpeedY(0);
        } else if (xDist != Integer.MAX_VALUE) {
            entity.moveX(xDist);
            if (collides(entity.getX(), entity.getY(), aH, bX, bY, bH)) {
                entity.moveX(-xDist);
                entity.moveY(yDist);
                entity.setSpeedY(0);
            } else {
                entity.setSpeedX(0);
            }
        }
    }
}