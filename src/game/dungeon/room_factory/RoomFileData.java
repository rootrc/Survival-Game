package game.dungeon.room_factory;

import java.util.ArrayList;

import game.Game;
import game.dungeon.room.object_utilities.RoomObject.RoomObjectData;
import game.utilities.FileOpener;
import game.utilities.RNGUtilities;

public class RoomFileData extends FileOpener {
    private int N, M;
    private int arr[][];
    private ArrayList<RoomObjectData> roomObjects;

    public static final int NO_MODIFIER = 0;
    public static final int REFLECTION_MODIFIER = 1;
    private int modifier;

    public RoomFileData(int id) {
        super(new StringBuilder("dungeongeneration/tileGrids/map")
                .append(String.format("%02d", id)).toString());
        modifier = RNGUtilities.getInt(2);
        if (Game.DEBUG) {
            modifier = 0;
        }
        readRoomData();
        closeFile();
    }

    private void readRoomData() {
        N = nextInt();
        M = nextInt();
        arr = new int[N][M];
        if (modifier == NO_MODIFIER) {
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < M; c++) {
                    arr[r][c] = nextInt();
                }
            }
        } else if (modifier == REFLECTION_MODIFIER) {
            for (int r = 0; r < N; r++) {
                for (int c = M - 1; c >= 0; c--) {
                    arr[r][c] = nextInt();
                }
            }
        }
        int objectDataSetCnt = nextInt();
        int setNum = RNGUtilities.getInt(objectDataSetCnt);
        // setNum = 1;
        int K;
        for (int i = 0; i < 3 * setNum; i++) {
            K = nextInt();
            for (int k = 0; k < K; k++) {
                next(3);
            }
        }
        roomObjects = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            K = nextInt();
            for (int j = 0; j < K; j++) {
                roomObjects.add(new RoomObjectData(10 * i + nextInt(), nextInt(), nextInt()));
            }
        }
    }

    public int getN() {
        return N;
    }

    public int getM() {
        return M;
    }

    public int[][] getTileGridArray() {
        return arr;
    }

    public int getModifier() {
        return modifier;
    }

    public ArrayList<RoomObjectData> getRoomObjects() {
        return roomObjects;
    }
}
