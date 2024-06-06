package core.dungeon.room_connection;

import core.dungeon.room.Room;
import core.dungeon.room.objects.Ladder;
import core.utilities.FileOpener;

public class DungeonLayoutGenerator extends FileOpener {
    private int[][] hashes;

    public DungeonLayoutGenerator() {
        super("dungeongeneration/dungeonlayout");
        int N = nextInt();
        int M = nextInt();
        hashes = new int[N + 1][M];
        int cnt1 = 1;
        int cnt2 = 0;
        for (int i = 1; i <= N; i++) {
            for (int j = 0; j < cnt1; j++) {
                hashes[i][j] = nextInt();
                cnt2 += hashes[i][j] % 10;
            }
            cnt1 = cnt2;
            cnt2 = 0;
        }
    }

    public int getGeneratedId(Ladder ladder, DungeonData dungeonData) {
        int hash = hashes[dungeonData.getDepth()][dungeonData.getDepthCnt(dungeonData.getDepth())];
        if (hash != 0) {
            return dungeonData.getMapId(ladder, hash / 10, hash % 10);
        } else if (ladder.getDirection() == 1) {
            return dungeonData.getMapId(ladder, 0, 1);
        } else {
            return dungeonData.getMapId(ladder, 1, 0);
        }
    }
}
