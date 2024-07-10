package game.dungeon.room_factory;

import game.dungeon.mechanics.HeightHandler;
import game.game_components.Factory;

public class HeightHandlerFactory extends Factory<HeightHandler> {
    public HeightHandler getHeightHandler(RoomFileData roomFileData) {
        return new HeightHandler(
                getHeightArray(roomFileData.getN(), roomFileData.getM(), roomFileData.getTileGridArray()));
    }

    private int[][] getHeightArray(int N, int M, int[][] tileGridArray) {
        int[][] height = new int[N][M];
        // TEMP
        if (tileGridArray == null) {
            return height;
        }
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < M; c++) {
                if (height[r][c] != 0) {
                    continue;
                }
                if ((tileGridArray[r][c] == 1 || tileGridArray[r][c] == 3 || tileGridArray[r][c] == 6)) {
                    height[r][c] = HeightHandler.BOTTOM;
                }
                if (tileGridArray[r][c] != 2) {
                    continue;
                }
                if (tileGridArray[r + 1][c] == 5) {
                    height[r][c - 1] = HeightHandler.STAIR_UP;
                    height[r][c] = HeightHandler.STAIR_UP;
                    height[r][c + 1] = HeightHandler.STAIR_UP;
                    height[r + 1][c - 1] = HeightHandler.STAIR_UP;
                    height[r + 1][c] = HeightHandler.STAIR_UP;
                    height[r + 1][c + 1] = HeightHandler.STAIR_UP;
                } else if (tileGridArray[r - 1][c] == 5) {
                    height[r - 1][c - 1] = HeightHandler.STAIR_DOWN;
                    height[r - 1][c] = HeightHandler.STAIR_DOWN;
                    height[r - 1][c + 1] = HeightHandler.STAIR_DOWN;
                    height[r][c - 1] = HeightHandler.STAIR_DOWN;
                    height[r][c] = HeightHandler.STAIR_DOWN;
                    height[r][c + 1] = HeightHandler.STAIR_DOWN;
                } else if (tileGridArray[r][c + 1] == 5) {
                    height[r - 2][c] = HeightHandler.STAIR_LEFT;
                    height[r - 1][c] = HeightHandler.STAIR_LEFT;
                    height[r][c] = HeightHandler.STAIR_LEFT;
                    height[r + 1][c] = HeightHandler.STAIR_LEFT;
                    height[r + 2][c] = HeightHandler.STAIR_LEFT;
                } else if (tileGridArray[r][c - 1] == 5) {
                    height[r - 2][c] = HeightHandler.STAIR_RIGHT;
                    height[r - 1][c] = HeightHandler.STAIR_RIGHT;
                    height[r][c] = HeightHandler.STAIR_RIGHT;
                    height[r + 1][c] = HeightHandler.STAIR_RIGHT;
                    height[r + 2][c] = HeightHandler.STAIR_RIGHT;
                }
            }
        }
        return height;
    }
}
