package core.dungeon.room_connection;

import java.util.ArrayList;
import java.util.HashMap;

import core.dungeon.room.objects.Ladder;
import core.utilities.FileOpener;

public class DungeonData extends FileOpener {
    private HashMap<Integer, ArrayList<Integer>> maps;
    private HashMap<Integer, Integer> idToHash;
    private int depth;
    private int depthMapCnt[];

    public DungeonData() {
        super("dungeongeneration/ladders");
        int N = nextInt();
        maps = new HashMap<>();
        idToHash = new HashMap<>();
        depthMapCnt = new int[12];
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
        depth = 0;
    }

    public int getMapId(Ladder ladder, int a, int b) {
        int hash = 10 * a + b;
        ArrayList<Integer> list = maps.get(hash);
        depthMapCnt[depth]++;;
        return list.get((int) (list.size() * Math.random()));
    }

    public void updateLadderConnections(Ladder ladder, int id) {
        int hash = idToHash.get(id);
        ArrayList<Integer> list = maps.get(hash);
        list.remove((Integer) id);
        // if (ladder.getDirection() == -1) {
        //     maps.get(hash - 1).add(id);
        //     idToHash.put(id, hash - 1);
        // } else {
        //     maps.get(hash - 10).add(id);
        //     idToHash.put(id, hash - 10);
        // }
    }

    public int getDepth() {
        return depth;
    }

    public int getDepthCnt(int i) {
        return depthMapCnt[i];
    }

    public void changeDepth(Ladder ladder) {
        if (ladder.getDirection() == 1) {
            depth--;
        } else {
            depth++;
        }
    }
}
