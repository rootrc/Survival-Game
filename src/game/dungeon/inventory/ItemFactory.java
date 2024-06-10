package game.dungeon.inventory;

import game.game_components.Factory;

public class ItemFactory extends Factory<Item> {
    private ItemFileData data;
    // private final Item items[][] = new Item[tileN][tileM];

    public ItemFactory() {
        data = new ItemFileData();
        for (int r = 0; r < data.N; r++) {
            for (int c = 0; c < data.M; c++) {
                getItem(r, c);
            }
        }
    }

    public Item getItem(int r, int c) {
        return new Item(r, c, data.getName(r, c), data.getDescription(r, c));
    }
}