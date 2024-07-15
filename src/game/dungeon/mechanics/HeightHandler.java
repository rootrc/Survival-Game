package game.dungeon.mechanics;

import game.dungeon.room.entity.Entity;
import game.dungeon.room.object_utilities.RoomObject;

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

    public int getLayer(RoomObject roomObject) {
        int[] rows = { roomObject.getMinRow(), roomObject.getMaxRow() };
        int[] cols = { roomObject.getMinCol(), roomObject.getMaxCol() };
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
        int[] rows = { entity.getMinRow(), entity.getMaxRow() };
        int[] cols = { entity.getMinCol(), entity.getMaxCol() };
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
