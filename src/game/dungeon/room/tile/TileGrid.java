package game.dungeon.room.tile;

import game.dungeon.Dungeon;
import game.dungeon.room.entity.Player;

public class TileGrid {
    private int N, M;
    private int width, height;
    private TileGridFloor tileGridFloor;
    private TileGridCeiling tileGridCeiling;

    public TileGrid(int N, int M, Tile[][][][] tileGrid, Player player) {
        this.N = N;
        this.M = M;
        height = Dungeon.TILESIZE * N;
        width = Dungeon.TILESIZE * M;
        tileGridFloor = new TileGridFloor(N, M, tileGrid[0]);
        tileGridCeiling = new TileGridCeiling(N, M, tileGrid[1], player);
    }

    public TileGridFloor getTileGridFloor() {
        return tileGridFloor;
    }

    public TileGridCeiling getTileGridCeiling() {
        return tileGridCeiling;
    }

    public void set(Player player) {
        tileGridCeiling.setOpacity(player);
    }

    public int getN() {
        return N;
    }

    public int getM() {
        return M;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
