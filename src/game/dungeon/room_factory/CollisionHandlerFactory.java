package game.dungeon.room_factory;

import game.dungeon.mechanics.CollisionHandler;
import game.dungeon.room.tile.Tile;
import game.dungeon.room.tile.TileGrid;
import game.game_components.Factory;

class CollisionHandlerFactory extends Factory<CollisionHandler> {
    CollisionHandler getCollisionChecker(RoomFileData file, TileGrid tileGrid) {
        return new CollisionHandler(
                getCollisionTiles(tileGrid.getN(), tileGrid.getM(), tileGrid.getTileGridFloor().getTileGridArray()),
                getHeightArray(file.getN(), file.getM(), file.getTileGrid()));
    }

    // TEMP
    CollisionHandler getCollisionChecker(int N, int M, TileGrid tileGrid) {
        return new CollisionHandler(getCollisionTiles(N, M, tileGrid.getTileGridFloor().getTileGridArray()), null);
    }

    private Tile[][] getCollisionTiles(int N, int M, Tile[][][] tileGrid) {
        Tile[][] res = new Tile[N][M];
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < M; c++) {
                if (tileGrid[2][r][c] != null) {
                    if (tileGrid[2][r][c].getHitBox() != null) {
                        res[r][c] = tileGrid[2][r][c];
                    }
                    continue;
                }
                if (tileGrid[1][r][c] != null) {
                    if (tileGrid[1][r][c].getHitBox() != null) {
                        res[r][c] = tileGrid[1][r][c];
                        continue;
                    }
                }
                if (tileGrid[3][r][c] != null) {
                    if (tileGrid[3][r][c].getHitBox() != null) {
                        res[r][c] = tileGrid[3][r][c];
                        continue;
                    }
                }
            }
        }
        return res;
    }

    private boolean[][] getHeightArray(int N, int M, int[][] tileGrid) {
        boolean[][] height = new boolean[N][M];
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < M; c++) {
                height[r][c] = (tileGrid[r][c] == 1 || tileGrid[r][c] == 3 || tileGrid[r][c] == 6);
            }
        }
        return height;
    }
}
