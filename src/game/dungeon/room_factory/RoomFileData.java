package game.dungeon.room_factory;

import java.util.ArrayList;

import game.dungeon.room.object_utilities.RoomObject.RoomObjectData;
import game.utilities.FileOpener;
import game.utilities.RNGUtilities;

class RoomFileData extends FileOpener {
    private int N, M;
    private int arr[][];
    private ArrayList<RoomObjectData> roomObjects;

    public static final int NO_MODIFIER = 0;
    public static final int REFLECTION_MODIFIER = 1;
    private int modifier;

    RoomFileData(int id) {
        super(new StringBuilder("dungeongeneration/tileGrids/map")
                .append(String.format("%02d", id)).toString());
        modifier = RNGUtilities.getInt(2);
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
        int K;
        for (int i = 0; i < setNum; i++) {
            K = nextInt();
            for (int j = 0; j < K; j++) {
                next(3);
            }
            K = nextInt();
            for (int j = 0; j < K; j++) {
                next(3);
            }
        }
        K = nextInt();
        roomObjects = new ArrayList<>();
        for (int i = 0; i < K; i++) {
            int id2 = nextInt();
            roomObjects.add(new RoomObjectData(id2, nextInt(), nextInt()));
        }
        K = nextInt();
        for (int i = 0; i < K; i++) {
            roomObjects.add(new RoomObjectData(10 + nextInt(), nextInt(), nextInt()));
        }
    }

    int getN() {
        return N;
    }

    int getM() {
        return M;
    }

    int[][] getTileGridArray() {
        return arr;
    }

    int getModifier() {
        return modifier;
    }

    ArrayList<RoomObjectData> getRoomObjects() {
        return roomObjects;
    }
}
