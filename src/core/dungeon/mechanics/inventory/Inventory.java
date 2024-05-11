package core.dungeon.mechanics.inventory;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Comparator;
import java.util.TreeMap;

import core.dungeon.mechanics.inventory.items.Item;
import core.window.SubScreen;

public class Inventory extends SubScreen {
    public Inventory() {
        super(400, 100);
    }

    private final TreeMap<Item, Integer> items = new TreeMap<>(new CustomComparator());

    public void addItem(Item item, int cnt) {
        items.put(item, items.get(item) + cnt);
    }

    class CustomComparator implements Comparator<Item> {
        @Override
        public int compare(Item a, Item b) {
            if (a.getPriority() > b.getPriority()) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public void update() {
    }

    public void draw(Graphics2D g2d) {
        // g2d.setColor(Color.white);
        g2d.fillRect(0, 0, 400, 100);
        g2d.setColor(Color.black);
    }
}