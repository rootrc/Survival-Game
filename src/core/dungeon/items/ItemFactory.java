package core.dungeon.items;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import core.dungeon.dungeon_ui.Timer;
import core.dungeon.items.Item.ItemEffect;
import core.dungeon.room.entity.Player;
import core.dungeon.room_factory.RoomFactory;
import core.dungeon.settings.DiffSettings;
import core.game_components.Factory;
import core.utilities.FileOpener;
import core.utilities.ImageUtilities;
import core.utilities.RNGUtilities;

public class ItemFactory extends Factory<Item> {
    private Player player;
    private Timer timer;
    private RoomFactory roomFactory;
    private Item items[];
    private ArrayList<Item> itemListUsingRarities;

    public ItemFactory(Player player, Timer timer, RoomFactory RoomFactory) {
        this.player = player;
        this.timer = timer;
        roomFactory = RoomFactory;
        ItemFileData data = new ItemFileData();
        items = new Item[data.N];
        itemListUsingRarities = new ArrayList<>();
        for (int i = 0; i < data.N; i++) {
            items[i] = new Item(i, data.image[i], getItemEffect(i), data.name[i], data.description[i],
                    data.incompatible[i]);
            for (int j = 0; j < data.rarity[i]; j++) {
                itemListUsingRarities.add(items[i]);
            }
        }
    }

    public ItemEffect getItemEffect(int idx) {
        switch (idx) {
            case 0:
                return new ItemEffect() {
                    public void doEffect() {
                        player.getPassiveItemStats().setHealthRegenPerSecond(DiffSettings.itemHealthRegen);
                    }

                    public void doReversedEffect() {
                        player.getPassiveItemStats().setHealthRegenPerSecond(0);
                    }
                };
            case 1:
                return new ItemEffect() {
                    public void doEffect() {
                        player.getPassiveItemStats().setDamageMulti(DiffSettings.itemDamageMulti);
                    }

                    public void doReversedEffect() {
                        player.getPassiveItemStats().setDamageMulti(0);
                    }
                };
            case 2:
                return new ItemEffect() {
                    public void doEffect() {
                        player.addHealth(DiffSettings.itemHealthIncrease);
                    }

                    public void doReversedEffect() {
                        player.addHealth(-DiffSettings.itemHealthIncrease);
                    }
                };
            case 3:
                return new ItemEffect() {
                    public void doEffect() {
                        player.addHealthPoints(DiffSettings.itemHealthPointIncrease);
                    }

                    public void doReversedEffect() {
                        player.addHealthPoints(-DiffSettings.itemHealthPointIncrease);
                    }
                };
            case 4:
                return new ItemEffect() {
                    public void doEffect() {
                        player.setMaxSpeed(player.getMaxSpeed() + DiffSettings.itemSpeedIncrease);
                    }

                    public void doReversedEffect() {
                        player.setMaxSpeed(player.getMaxSpeed() - DiffSettings.itemSpeedIncrease);
                    }
                };
            case 5:
                return new ItemEffect() {
                    public void doEffect() {
                        player.multiplyAcc(DiffSettings.itemAccDecrease);
                        player.multiplyDeacc(DiffSettings.itemAccDecrease);
                    }

                    public void doReversedEffect() {
                        player.multiplyAcc(1.0 / DiffSettings.itemAccDecrease);
                        player.multiplyDeacc(1.0 / DiffSettings.itemAccDecrease);
                    }
                };
            default:
                return null;
        }
    }

    // TEMP
    public Item getItem(int idx) {
        return items[idx];
    }

    public Item[] getThreeItems() {
        Item[] res = new Item[3];
        for (int i = 0; i < 3; i++) {
            while (true) {
                Item item = itemListUsingRarities.get(RNGUtilities.getInt(0, itemListUsingRarities.size()));
                boolean flag = true;
                for (int j = 0; j < i; j++) {
                    if (res[j] == item) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    res[i] = item;
                    break;
                }
            }
        }
        return res;
    }

    public void removeItem(Item item) {
        int idx = Collections.binarySearch(itemListUsingRarities, item);
        while (idx != 0 && itemListUsingRarities.get(idx - 1) == item) {
            idx--;
        }
        while (idx != itemListUsingRarities.size() && itemListUsingRarities.get(idx) == item) {
            itemListUsingRarities.remove(item);
        }
    }

    private static class ItemFileData extends FileOpener {
        int N;
        String name[];
        String description[];
        int rarity[];
        BufferedImage image[];
        ArrayList<Integer> incompatible[];

        @SuppressWarnings("unchecked")
        public ItemFileData() {
            super("item_data/items");
            N = nextInt();
            name = new String[N];
            description = new String[N];
            rarity = new int[N];
            image = new BufferedImage[N];
            incompatible = new ArrayList[N];
            for (int i = 0; i < N; i++) {
                name[i] = next();
                description[i] = next();
                rarity[i] = nextInt();
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