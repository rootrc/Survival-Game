package game.dungeon.room_factory;

import java.util.ArrayList;

import game.dungeon.room.object_utilities.RoomObject.RoomObjectData;
import game.utilities.FileOpener;
import game.utilities.RNGUtilities;

class RoomFileData extends FileOpener {
    private int id;
    private int N, M;
    private int arr[][];
    private ArrayList<RoomObjectData> roomObjects;
    private int ladderUpCnt;
    private int ladderDownCnt;

    RoomFileData(int id) {
        super(new StringBuilder("dungeongeneration/tileGrids/map")
                .append(String.format("%02d", id)).toString());
        this.id = id;
        N = nextInt();
        M = nextInt();
        arr = new int[N][M];
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < M; c++) {
                arr[r][c] = nextInt();
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
            RoomObjectData temp = new RoomObjectData(id2, nextInt(), nextInt());
            roomObjects.add(temp);
            if (id2 == 0) {
                ladderUpCnt++;
            } else {
                ladderDownCnt++;
            }
        }
        K = nextInt();
        for (int i = 0; i < K; i++) {
            RoomObjectData temp = new RoomObjectData(10 + nextInt(), nextInt(), nextInt());
            roomObjects.add(temp);
        }
        closeFile();
    }

    int getId() {
        return id;
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

    int getLadderUp() {
        return ladderUpCnt;
    }

    int getLadderDown() {
        return ladderDownCnt;
    }

    ArrayList<RoomObjectData> getRoomObjects() {
        return roomObjects;
    }
}
