package core.dungeon.items;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.ImageIcon;

public class Item implements Comparable<Item> {
    private int id;
    private ImageIcon imageIcon;
    // private ImageIcon rolloverIcon;
    private Action action;
    private String name;
    private String description;
    private ArrayList<Integer> incompatible;
    private ItemEffect itemEffect;

    public Item(int id, BufferedImage image, ItemEffect itemEffect, String name, String description,
            ArrayList<Integer> incompatible) {
        this.id = id;
        this.itemEffect = itemEffect;
        this.imageIcon = new ImageIcon(image);
        this.name = name;
        this.description = description;
        this.incompatible = incompatible;
    }

    public int compareTo(Item o) {
        return Integer.compare(id, o.getId());
    }

    private int getId() {
        return id;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    public Action getAction() {
        return action;
    }

    public ItemEffect getItemEffect() {
        return itemEffect;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Integer> getIncompatible() {
        return incompatible;
    }

    public boolean isIncompatible(Item item) {
        return incompatible.contains(item.getId());
    }

    public String getToolTip() {
        return new StringBuilder("<html>").append(name).append("<br>").append(description).append("</html>").toString();
    }

    public static abstract class ItemEffect implements PassiveEffect {

    }

    private interface PassiveEffect {
        public void doEffect();

        public void doReversedEffect();
    }
}
