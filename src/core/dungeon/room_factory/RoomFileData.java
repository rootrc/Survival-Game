package core.dungeon.room_factory;

import java.util.ArrayList;

import core.Game;
import core.dungeon.Dungeon;
import core.dungeon.room.object_utilities.RoomObject.RoomObjectData;
import core.utilities.FileOpener;
import core.utilities.RNGUtilities;

class RoomFileData extends FileOpener {
    public static final int NO_MODIFIER = 0;
    public static final int REFLECTION_MODIFIER = 1;
    
    private int N, M;
    private int arr[][];
    private ArrayList<RoomObjectData> roomObjects;
    private int modifier;
    private int depth;

    RoomFileData(int id, int depth) {
        super(new StringBuilder("dungeongeneration/tileGrids/map")
                .append(String.format("%02d", id)).toString());
        modifier = RNGUtilities.getInt(2);
        this.depth = depth;
        if (Game.DEBUG) {
            modifier = NO_MODIFIER;
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
        if (Game.DEBUG) {
            setNum = Dungeon.SETNUM;
        }
        int K;
        for (int i = 0; i < 3 * setNum; i++) {
            K = nextInt();
            for (int k = 0; k < K; k++) {
                readLine();
            }
        }
        roomObjects = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            K = nextInt();
            for (int j = 0; j < K; j++) {
                int id = 10 * i + nextInt();
                RoomObjectData roomObjectData;
                if (RoomObjectData.FIVE_SET.contains(id)) {
                    roomObjectData = new RoomObjectData(id, nextInt(), nextInt(), nextInt(), nextInt());
                } else if (RoomObjectData.FOUR_SET.contains(id)) {
                    roomObjectData = new RoomObjectData(id, nextInt(), nextInt(), nextInt());
                } else {
                    roomObjectData = new RoomObjectData(id, nextInt(), nextInt());
                }
                roomObjects.add(roomObjectData);
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

    public ArrayList<RoomObjectData> getRoomObjects() {
        return roomObjects;
    }

    public int getModifier() {
        return modifier;
    }

    public int getDepth() {
        return depth;
    }
}