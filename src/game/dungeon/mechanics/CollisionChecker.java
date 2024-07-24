package game.dungeon.mechanics;

import game.dungeon.room.entity.Player;
import game.dungeon.room.tile.Tile;

public class CollisionChecker {
    private Tile[][] collisionArray;

    public CollisionChecker(Tile[][] collisionArray) {
        this.collisionArray = collisionArray;
    }

    public void handleCollision(Player player) {
        int[] rows = { player.getMinRow(), player.getMaxRow() };
        int[] cols = { player.getMinCol(), player.getMaxCol() };
        for (int r : rows) {
            for (int c : cols) {
                if (CollisionHandler.collides(player, collisionArray[r][c], r, c)) {
                    CollisionHandler.handleCollision(player, collisionArray[r][c], r, c);
                    return;
                }
            }
        }
    }

    public Tile[][] getCollisionArray() {
        return collisionArray;
    }
}