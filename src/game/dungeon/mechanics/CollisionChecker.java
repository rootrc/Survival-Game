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
        ArrayList<Integer> rows = new ArrayList<>();
        ArrayList<Integer> cols = new ArrayList<>();
        rows.add((int) ((entity.getY() + y) / Dungeon.TILESIZE));
        rows.add((int) ((entity.getY() + y) / Dungeon.TILESIZE));
        rows.add((int) ((entity.getY() + y) / Dungeon.TILESIZE) + 1);
        rows.add((int) ((entity.getY() + y) / Dungeon.TILESIZE) + 1);
        cols.add((int) ((entity.getX() + x) / Dungeon.TILESIZE));
        cols.add((int) ((entity.getX() + x) / Dungeon.TILESIZE) + 1);
        cols.add((int) ((entity.getX() + x) / Dungeon.TILESIZE));
        cols.add((int) ((entity.getX() + x) / Dungeon.TILESIZE) + 1);
        for (int r: rows) {
            for (int c: cols) {
                if (collision[r][c]) {
                    return false;
                }
            }
        }
        return true;
    }

}