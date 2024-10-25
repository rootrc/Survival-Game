package core.dungeon.items;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import core.game_components.Factory;
import core.utilities.FileOpener;
import core.utilities.ImageUtilities;

public class ItemFactory extends Factory<Item> {
    private ItemFileData data;
    private Item items[];

    public ItemFactory() {
        data = new ItemFileData();
        items = new Item[data.N];
        for (int i = 0; i < data.N; i++) {
            items[i] = new Item(data.image[i], data.name[i], data.description[i], data.incompatible[i]);
        }
    }

    public Item getItem(int idx) {
        return items[idx];
    }

    private static class ItemFileData extends FileOpener {
        int N;
        String name[];
        String description[];
        BufferedImage image[];
        ArrayList<Integer> incompatible[];
    
        @SuppressWarnings("unchecked")
        public ItemFileData() {
            super("item_data/items");
            N = nextInt();
            name = new String[N];
            description = new String[N];
            image = new BufferedImage[N];
            incompatible = new ArrayList[N];
            for (int i = 0; i < N; i++) {
                name[i] = next();
                description[i] = next();
                int r = nextInt();
                int c = nextInt();
                image[i] = ImageUtilities.getImage("item_images", "playerSkills", r, c, 46, 46);
                int K = nextInt();
                incompatible[i] = new ArrayList<>();
                for (int j = 0; j < K; j++) {
                    incompatible[i].add(nextInt());
                }
            }
            closeFile();
        }
    }
}