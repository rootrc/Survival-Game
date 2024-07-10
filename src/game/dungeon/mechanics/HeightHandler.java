package game.dungeon.mechanics;

import java.util.ArrayList;

import game.dungeon.Dungeon;
import game.dungeon.room.entity.Entity;

public class HeightHandler {
    public static final int TOP = 0;
    public static final int BOTTOM = 1;
    public static final int STAIR_UP = 2;
    public static final int STAIR_DOWN = 3;
    public static final int STAIR_LEFT = 4;
    public static final int STAIR_RIGHT = 5;
    public static final int SLIDE_WALL = 6;

    private int[][] height;

    public HeightHandler(int[][] height) {
        this.height = height;
    }

    public int getLayer(Entity entity) {
        ArrayList<Integer> rows = new ArrayList<>();
        ArrayList<Integer> cols = new ArrayList<>();
        rows.add((int) ((entity.getY() + entity.getHitBox().getY()) / Dungeon.TILESIZE));
        rows.add((int) ((entity.getY() + entity.getHitBox().getMaxY() - 1) / Dungeon.TILESIZE));
        cols.add((int) ((entity.getX() + entity.getHitBox().getX()) / Dungeon.TILESIZE));
        cols.add((int) ((entity.getX() + entity.getHitBox().getMaxX() - 1) / Dungeon.TILESIZE));
        for (int r : rows) {
            for (int c : cols) {
                if (height[r][c] != 0) {
                    return TOP;
                }
            }
        }
        return BOTTOM;
    }

    public int getLocation(Entity entity) {
        ArrayList<Integer> rows = new ArrayList<>();
        ArrayList<Integer> cols = new ArrayList<>();
        rows.add((int) ((entity.getY() + entity.getHitBox().getY()) / Dungeon.TILESIZE));
        rows.add((int) ((entity.getY() + entity.getHitBox().getMaxY() - 1) / Dungeon.TILESIZE));
        cols.add((int) ((entity.getX() + entity.getHitBox().getX()) / Dungeon.TILESIZE));
        cols.add((int) ((entity.getX() + entity.getHitBox().getMaxX() - 1) / Dungeon.TILESIZE));
        for (int r : rows) {
            for (int c : cols) {
                if (STAIR_UP <= height[r][c]) {
                    return height[r][c];
                }
            }
        }
        return -1;
    }
}
