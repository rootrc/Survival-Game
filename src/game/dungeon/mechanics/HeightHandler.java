package game.dungeon.mechanics;

import java.util.ArrayList;

import game.dungeon.Dungeon;
import game.dungeon.room.entity.Entity;

public class HeightHandler {
    private boolean[][] height;

    public HeightHandler(boolean[][] height) {
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
                if (height[r][c]) {
                    return 0;
                }
            }
        }
        return 1;
    }
}
