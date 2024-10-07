package core.dungeon.room_connection;

import java.util.ArrayList;
import java.util.HashMap;

import core.dungeon.room.object.Ladder;
import core.utilities.FileOpener;
import core.utilities.RNGUtilities;

public class DungeonLayoutGenerator extends FileOpener {
    private int[][] hashes;
    private HashMap<Integer, ArrayList<Integer>> maps;
    private HashMap<Integer, Integer> idToHash;

    public DungeonLayoutGenerator() {
        super("dungeongeneration/dungeonlayout");
        int N = nextInt();
        int M = nextInt();
        hashes = new int[N + 1][M];
        for (int i = 1; i <= N; i++) {
            int K = nextInt();
            for (int j = 0; j < K; j++) {
                hashes[i][j] = nextInt();
            }
        }
        openFile("dungeongeneration/ladders");
        N = nextInt();
        maps = new HashMap<>();
        idToHash = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                maps.put(10 * i + j, new ArrayList<>());
            }
        }
        for (int i = 0; i < N; i++) {
            int id = nextInt();
            int up = nextInt();
            int down = nextInt();
            int hash = 10 * up + down;
            maps.get(hash).add(id);
            idToHash.put(id, hash);
        }
    }

    public int getGeneratedId(Ladder ladder, int depth, int[] depthMapCnt) {
        int hash = hashes[depth][depthMapCnt[depth]];
        if (hash != 0) {
            return getMapId(ladder, hash / 10, hash % 10, depth, depthMapCnt);
        } else if (ladder.getDirection() == Ladder.UP_DIRECTION) {
            return getMapId(ladder, 0, 1, depth, depthMapCnt);
        } else if (ladder.getDirection() == Ladder.DOWN_DIRECTION) {
            return getMapId(ladder, 1, 0, depth, depthMapCnt);
        }
        return 0;
    }

    public int getMapId(Ladder ladder, int a, int b, int depth, int[] depthMapCnt) {
        int hash = 10 * a + b;
        ArrayList<Integer> list = maps.get(hash);
        depthMapCnt[depth]++;
        int mapId = list.get(RNGUtilities.getInt(list.size()));
        // TEMP
        while (mapId == 1) {
            mapId = list.get(RNGUtilities.getInt(list.size()));
        }
        list.remove((Integer) mapId);
        return mapId;
    }
}
