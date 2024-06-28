package game.dungeon.items;

import game.dungeon.inventory.Inventory;
import game.game_components.Factory;

public class ItemFactory extends Factory<Item> {
    private Inventory inventory;
    private ItemFileData data;
    // private final Item items[][] = new Item[tileN][tileM];

    public ItemFactory(Inventory inventory) {
        this.inventory = inventory;
        data = new ItemFileData();
        // for (int r = 0; r < data.N; r++) {
        //     for (int c = 0; c < data.M; c++) {
        //         getItem(r, c);
        //     }
        // }
    }

    public Item getItem() {
        // TODO
        return new UICreatorItem(inventory, 0, 0, data.getName(0, 0), data.getDescription(0, 0));
    }
}