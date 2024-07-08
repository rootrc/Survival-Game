package game.dungeon.room_factory;

import game.dungeon.mechanics.CollisionHandler;
import game.dungeon.room.tile.Tile;
import game.dungeon.room.tile.TileGrid;
import game.game_components.Factory;

class CollisionHandlerFactory extends Factory<CollisionHandler> {
    CollisionHandler getCollisionChecker(TileGrid tileGrid) {
        return new CollisionHandler(
                getCollisionTiles(tileGrid.getN(), tileGrid.getM(), tileGrid.getTileGridFloor().getTileGridArray()));
    }

    private Tile[][] getCollisionTiles(int N, int M, Tile[][][] tileGrid) {
        Tile[][] res = new Tile[N][M];
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < M; c++) {
                if (tileGrid[2][r][c] != null) {
                    if (tileGrid[2][r][c].getHitBox() != null) {
                        res[r][c] = tileGrid[2][r][c];
                    } else {
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
                res[r][c] = tileGrid[0][r][c];
            }
        }
        return res;
    }

}
