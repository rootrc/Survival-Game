package core.dungeon.mechanics.inventory.items;

import core.utilities.FileOpener;

public class ItemFileData extends FileOpener {
    private final int N = 22;
    private final int M = 16;
    private final String names[][] = new String[N][M];
    private final String description[][] = new String[N][M];

    public ItemFileData() {
        super("items/descriptions");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                names[i][j] = nextStr();
                description[i][j] = nextStr();
            }
        }
        closeFile();
    }
}
