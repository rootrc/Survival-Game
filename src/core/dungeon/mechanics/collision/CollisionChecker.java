package core.dungeon.mechanics.collision;

import core.dungeon.room.entity.Entity;
import core.dungeon.room.tile.Tile;

public class CollisionChecker {
    private Tile[][] collisionArray;

    public CollisionChecker(Tile[][] collisionArray) {
        this.collisionArray = collisionArray;
    }

    public void handleCollision(Entity entity) {
        int[] rows = { entity.getMinRow(), entity.getMaxRow() };
        int[] cols = { entity.getMinCol(), entity.getMaxCol() };
        for (int r : rows) {
            for (int c : cols) {
                if (r < 0 || c < 0 || r >= collisionArray.length || c >= collisionArray[0].length) {
                    continue;
                }
                if (CollisionHandler.collides(entity, collisionArray[r][c], r, c)) {
                    CollisionHandler.handleCollision(entity, collisionArray, r, c);
                    return;
                }
            }
        }
    }

    public Tile[][] getCollisionArray() {
        return collisionArray;
    }
}