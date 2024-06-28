package game.dungeon.mechanics;

import java.util.ArrayList;

import game.dungeon.Dungeon;
import game.dungeon.room.entity.Entity;

public class CollisionChecker {
    private boolean collision[][];

    public CollisionChecker(boolean collision[][]) {
        this.collision = collision;
    }

    public boolean checkTile(Entity entity, double x, double y) {
        if (entity.getX() < 0 || entity.getY() < 0) {
            return true;
        }
        ArrayList<Integer> rows = new ArrayList<>();
        ArrayList<Integer> cols = new ArrayList<>();
        rows.add((int) ((entity.getY() + entity.getHitBox().getY() + y) / Dungeon.TILESIZE));
        rows.add((int) ((entity.getY() + entity.getHitBox().getMaxY() + y) / Dungeon.TILESIZE));
        cols.add((int) ((entity.getX() + entity.getHitBox().getX() + x) / Dungeon.TILESIZE));
        cols.add((int) ((entity.getX() + entity.getHitBox().getMaxX() + x) / Dungeon.TILESIZE));
        for (int r : rows) {
            for (int c : cols) {
                if (collision[r][c]) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkPoint(int x, int y) {
        return !collision[y / Dungeon.TILESIZE][x / Dungeon.TILESIZE];
    }

}