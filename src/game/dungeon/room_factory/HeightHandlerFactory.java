package game.dungeon.room_factory;

import game.dungeon.mechanics.HeightHandler;
import game.game_components.Factory;

public class HeightHandlerFactory extends Factory<HeightHandler> {
    public HeightHandler getHeightHandler(RoomFileData roomFileData) {
        return new HeightHandler(getHeightArray(roomFileData.getN(), roomFileData.getM(), roomFileData.getTileGridArray()));
    }

    private boolean[][] getHeightArray(int N, int M, int[][] tileGridArray) {
        boolean[][] height = new boolean[N][M];
        // TEMP
        if (tileGridArray == null) {
            return height;
        }
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < M; c++) {
                height[r][c] = (tileGridArray[r][c] == 1 || tileGridArray[r][c] == 2 || tileGridArray[r][c] == 3 || tileGridArray[r][c] == 6);
            }
        }
        return height;
    }
}
