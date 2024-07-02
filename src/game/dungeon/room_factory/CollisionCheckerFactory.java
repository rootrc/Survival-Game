package game.dungeon.room_factory;

import game.dungeon.mechanics.CollisionChecker;
import game.dungeon.room.tile.Tile;
import game.dungeon.room.tile.TileGrid;
import game.game_components.Factory;

class CollisionCheckerFactory extends Factory<CollisionChecker> {

    CollisionChecker getCollisionChecker(RoomFileData file, TileGrid tileGrid) {
        return new CollisionChecker(getCollisionArray(tileGrid.getN(), tileGrid.getM(), tileGrid.getTileGrid(0)),
                getHeightArray(file.getN(), file.getM(), file.getTileGrid()));
    }

    private boolean[][] getCollisionArray(int N, int M, Tile[][][] tileGrid) {
        boolean[][] collision = new boolean[N][M];
        for (int i = 0; i < tileGrid.length; i++) {
            if (i == 2) {
                continue;
            }
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < M; c++) {
                    if (tileGrid[i][r][c] != null) {
                        collision[r][c] |= tileGrid[i][r][c].getCollision();
                    }
                }
            }
        }
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < M; c++) {
                if (tileGrid[2][r][c] != null) {
                    collision[r][c] = tileGrid[2][r][c].getCollision();
                }
            }
        }
        return collision;
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
