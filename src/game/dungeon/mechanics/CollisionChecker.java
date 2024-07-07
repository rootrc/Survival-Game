package game.dungeon.mechanics;

import java.util.ArrayList;

import game.dungeon.Dungeon;
import game.dungeon.room.entity.Entity;

public class CollisionChecker {
    private boolean collision[][];
    private boolean height[][];

    public CollisionChecker(boolean collision[][], boolean[][] height) {
        this.collision = collision;
        this.height = height;
    }

    public boolean canMove(Entity entity, double dx, double dy) {
        ArrayList<Integer> rows = new ArrayList<>();
        ArrayList<Integer> cols = new ArrayList<>();
        rows.add((int) ((entity.getY() + entity.getHitBox().getY() + dy) / Dungeon.TILESIZE));
        rows.add((int) ((entity.getY() + entity.getHitBox().getMaxY() + dy) / Dungeon.TILESIZE));
        cols.add((int) ((entity.getX() + entity.getHitBox().getX() + dx) / Dungeon.TILESIZE));
        cols.add((int) ((entity.getX() + entity.getHitBox().getMaxX() + dx) / Dungeon.TILESIZE));
        for (int r : rows) {
            for (int c : cols) {
                if (collision[r][c]) {
                    return false;
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
        return !collision[(int) (y / Dungeon.TILESIZE)][(int) (x / Dungeon.TILESIZE)];
    }

}