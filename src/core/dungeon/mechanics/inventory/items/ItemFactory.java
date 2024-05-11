package core.dungeon.mechanics.inventory.items;

import core.dungeon.room.tile.Tile;
import core.utilities.Factory;
import core.utilities.ImageUtilities;

public class ItemFactory extends Factory<Item> {
    private final String name = "itemTileSet";
    private ItemFileData data;
    // private final int tileN = 18;
    // private final int tileM = 16;
    // private final Item items[][] = new Item[tileN][tileM];

    public ItemFactory() {
        data = new ItemFileData();
        // for (int r = 0; r < tileN; r++) {
        //     for (int c = 0; c < tileM; c++) {
        //         items[r][c] = getTile(r, c);
        //     }
        // }
    }

    public Item getImage(int r, int c) {
        return new Item(ImageUtilities.getImage("tiles", name, r, c));
    }
}
