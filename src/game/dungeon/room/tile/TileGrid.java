package game.dungeon.room.tile;

import game.dungeon.mechanics.HeightHandler;
import game.dungeon.mechanics.collision.CollisionChecker;
import game.dungeon.room.entity.Player;

public class TileGrid {
    private int N, M;
    private TileGridFloor tileGridFloor;
    private TileGridCeiling tileGridCeiling;
    private CollisionChecker collisionChecker;
    private HeightHandler heightHandler;

    public TileGrid(Tile[][][][] tileGridArray, Player player, CollisionChecker collisionChecker, HeightHandler heightHandler) {
        N = tileGridArray[0][0].length;
        M = tileGridArray[0][0][0].length;
        this.collisionChecker = collisionChecker;
        this.heightHandler = heightHandler;
        tileGridFloor = new TileGridFloor(tileGridArray[0]);
        tileGridCeiling = new TileGridCeiling(tileGridArray[1], player, heightHandler);
    }

    public void setPlayer(Player player) {
        tileGridCeiling.setOpacity(player);
    }

    public TileGridFloor getTileGridFloor() {
        return tileGridFloor;
    }

    public TileGridCeiling getTileGridCeiling() {
        return tileGridCeiling;
    }

    public CollisionChecker getCollisionChecker() {
        return collisionChecker;
    }

    public HeightHandler getHeightHandler() {
        return heightHandler;
    }

    public int getN() {
        return N;
    }

    public int getM() {
        return M;
    }

    public int getWidth() {
        return tileGridFloor.getWidth();
    }

    public int getHeight() {
        return tileGridFloor.getHeight();
    }
   
}