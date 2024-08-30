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
        if (aH.getX() + player.getX() < bH.getX() + bX
                && aH.getMaxX() + player.getX() - bH.getX() - bX <= player.getSpeedX()
                && (player.getSpeedX() > 0)) {
            player.setX(bH.getX() + bX - aH.getMaxX());
            player.setSpeedX(0);
        }
        if (aH.getX() + player.getX() - bH.getMaxX() - bX >= player.getSpeedX()
                && bH.getMaxX() + bX < aH.getMaxX() + player.getX()
                && (player.getSpeedX() < 0)) {
            player.setX(bH.getMaxX() + bX - aH.getX());
            player.setSpeedX(0);
        }
        if (aH.getY() + player.getY() < bH.getY() + bY
                && aH.getMaxY() + player.getY() - bH.getY() - bY <= player.getSpeedY()
                && (player.getSpeedY() > 0)) {
            player.setY(bH.getY() + bY - aH.getMaxY());
            player.setSpeedY(0);
        }
        if (aH.getY() + player.getY() - bH.getMaxY() - bY >= player.getSpeedY()
                && bH.getMaxY() + bY < aH.getMaxY() + player.getY()
                && (player.getSpeedY() < 0)) {
            player.setY(bH.getMaxY() + bY - aH.getY());
            player.setSpeedY(0);
        }
    }
}