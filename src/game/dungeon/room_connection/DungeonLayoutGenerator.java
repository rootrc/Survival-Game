package game.dungeon.room_connection;

import game.Game;
import game.dungeon.room.object.Ladder;
import game.utilities.FileOpener;

public class DungeonLayoutGenerator extends FileOpener {
    private int[][] hashes;

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
    }

    public int getGeneratedId(Ladder ladder, DungeonData dungeonData) {
        int hash = hashes[dungeonData.getDepth()][dungeonData.getDepthCnt(dungeonData.getDepth())];
        if (Game.DEBUG) {
            System.out.println(
                    hash + " " + dungeonData.getDepth() + " " + dungeonData.getDepthCnt(dungeonData.getDepth()));
        }
        if (hash != 0) {
            return dungeonData.getMapId(ladder, hash / 10, hash % 10);
        } else if (ladder.getDirection() == 1) {
            return dungeonData.getMapId(ladder, 0, 1);
        } else {
            return dungeonData.getMapId(ladder, 1, 0);
        }
    }
}
