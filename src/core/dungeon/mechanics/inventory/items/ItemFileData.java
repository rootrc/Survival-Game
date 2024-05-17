package core.dungeon.mechanics.inventory.items;

import core.utilities.FileOpener;

public class ItemFileData extends FileOpener {
    public final int N = 30;
    public final int M = 14;
    private final String type[][] = new String[N][M];
    private final String names[][] = new String[N][M];
    private final String description[][] = new String[N][M];

    public ItemFileData() {
        super("item_data/type");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                type[i][j] = nextStr();
            }
        }
        openFile("item_data/names");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                names[i][j] = nextStr();
            }
        }
        openFile("item_data/descriptions");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                description[i][j] = nextStr();
            }
        }
        closeFile();
    }

    public String getName(int r, int c) {
        return names[r][c];
    }

    public String getDescription(int r, int c) {
        return description[r][c];
    }
}
