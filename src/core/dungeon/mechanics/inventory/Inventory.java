package core.dungeon.mechanics.inventory;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Comparator;
import java.util.TreeMap;

import core.dungeon.mechanics.inventory.items.Item;
import core.window.GameComponent;

public class Inventory extends GameComponent {
    private final TreeMap<Item, Integer> items = new TreeMap<>(new CustomComparator());

    public Inventory() {
        super(400, 100);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        draw(g2d);
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        g2d.setColor(Color.white);
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

    public void addItem(Item item, int cnt) {
        items.put(item, items.get(item) + cnt);
    }
}