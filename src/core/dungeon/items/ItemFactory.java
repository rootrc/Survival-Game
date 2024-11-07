package core.dungeon.items;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import core.dungeon.dungeon_ui.Timer;
import core.dungeon.items.Item.ItemEffect;
import core.dungeon.room.entity.Player;
import core.dungeon.room_factory.RoomFactory;
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
                        player.getStats().setHealthRegenPerSecond(1.0 / 60);
                    }

                    public void doReversedEffect() {
                        player.getStats().setHealthRegenPerSecond(0);
                    }
                };
            case 1:
                return new ItemEffect() {
                    public void doEffect() {
                        player.getStats().setDamageMulti(0.5);
                    }

                    public void doReversedEffect() {
                        player.getStats().setDamageMulti(0);
                    }
                };
            case 2:
                return new ItemEffect() {
                    public void doEffect() {
                        player.addHealth(3);
                    }

                    public void doReversedEffect() {
                        player.addHealth(-3);
                    }
                };
            case 3:
                return new ItemEffect() {
                    public void doEffect() {
                        player.addHealthPoints(1);
                    }

                    public void doReversedEffect() {
                        player.addHealthPoints(-1);
                    }
                };
            case 4:
                return new ItemEffect() {
                    public void doEffect() {
                        player.setMaxSpeed(player.getMaxSpeed() + 1);
                    }

                    public void doReversedEffect() {
                        player.setMaxSpeed(player.getMaxSpeed() - 1);
                    }
                };
            case 5:
                return new ItemEffect() {
                    public void doEffect() {
                        player.multiplyAcc(4);
                        player.multiplyDeacc(4);
                    }

                    public void doReversedEffect() {
                        player.multiplyAcc(1.0 / 4);
                        player.multiplyDeacc(1.0 / 4);
                    }
                };
            case 6:
                return new ItemEffect() {
                    public void doEffect() {
                        player.getStats().addRevive(1);
                    }

                    public void doReversedEffect() {
                        player.getStats().addRevive(-1);
                    }
                };
            case 7:
                return new ItemEffect() {
                    public void doEffect() {
                        player.getStats().multiLightRadius(1.4);
                    }

                    public void doReversedEffect() {
                        player.getStats().multiLightRadius(1.0 / 1.4);
                    }
                };
            case 8:
                return new ItemEffect() {
                    public void doEffect() {
                        player.getStats().multiDetectionRadius(2);
                        player.getStats().multiVision(2);
                        player.getStats().setDamageMulti(1.5);
                        ;
                    }

                    public void doReversedEffect() {
                        player.getStats().multiDetectionRadius(0.5);
                        player.getStats().multiVision(0.5);
                        player.getStats().setDamageMulti(1.0 / 1.5);
                        ;
                    }
                };
            case 9:
                return new ItemEffect() {
                    public void doEffect() {
                        player.getStats().multiDetectionRadius(0.5);
                        player.getStats().multiVision(0.6);
                        player.addHealthPoints(1);
                    }

                    public void doReversedEffect() {
                        player.getStats().multiDetectionRadius(2);
                        player.getStats().multiVision(1.0 / 0.6);
                        player.addHealthPoints(-1);
                    }
                };
            case 10:
                return new ItemEffect() {
                    public void doEffect() {
                        player.getStats().multiFogOfWarMulti(1.75);
                    }

                    public void doReversedEffect() {
                        player.getStats().multiFogOfWarMulti(1.0 / 1.75);
                    }
                };
            case 11:
                return new ItemEffect() {
                    public void doEffect() {
                        player.getStats().setSeeAbove(true);
                    }

                    public void doReversedEffect() {
                        player.getStats().setSeeAbove(false);
                    }
                };
            case 12:
                return new ItemEffect() {
                    public void doEffect() {
                        timer.addStartTime(300);
                    }
                    
                    public void doReversedEffect() {
                        timer.addStartTime(-300);
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
                    if (res[j] == item || res[j].isIncompatible(item) || item.isIncompatible(res[j])) {
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
        for (int i : item.getIncompatible()) {
            removeItem2(items[i]);
        }
    }

    public void removeItem2(Item item) {
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