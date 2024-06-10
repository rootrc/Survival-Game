package game.dungeon.mechanics;

import java.util.ArrayList;

import game.dungeon.room.entity.Entity;
import game.game_components.GamePanel;

public class CollisionChecker {
    private boolean collision[][];

    public CollisionChecker(boolean collision[][]) {
        this.collision = collision;
    }

    public boolean checkTile(Entity entity, double x, double y) {
        ArrayList<Integer> rows = new ArrayList<>();
        ArrayList<Integer> cols = new ArrayList<>();
        rows.add((int) ((entity.getY() + y) / GamePanel.TILESIZE));
        rows.add((int) ((entity.getY() + y) / GamePanel.TILESIZE));
        rows.add((int) ((entity.getY() + y) / GamePanel.TILESIZE) + 1);
        rows.add((int) ((entity.getY() + y) / GamePanel.TILESIZE) + 1);
        cols.add((int) ((entity.getX() + x) / GamePanel.TILESIZE));
        cols.add((int) ((entity.getX() + x) / GamePanel.TILESIZE) + 1);
        cols.add((int) ((entity.getX() + x) / GamePanel.TILESIZE));
        cols.add((int) ((entity.getX() + x) / GamePanel.TILESIZE) + 1);
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