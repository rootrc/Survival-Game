package core.dungeon.items;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ImageIcon;

public class Item implements Comparable<Item> {
    private int id;
    private ImageIcon imageIcon;
    private String name;
    private String description;
    private ArrayList<Integer> incompatible;
    private ItemAction itemAction;

    public Item(int id, BufferedImage image, ItemAction itemAction, String name, String description,
            ArrayList<Integer> incompatible) {
        this.id = id;
        this.itemAction = itemAction;
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

    public ItemAction getItemAction() {
        return itemAction;
    }

    public boolean canBeFoundInChest() {
        return itemAction.canBeFoundInChest();
    }

    public ArrayList<Integer> getIncompatible() {
        return incompatible;
    }

    public boolean isIncompatible(Item item) {
        return Collections.binarySearch(incompatible, item.getId()) >= 0;
    }

    public String getToolTipText() {
        return new StringBuilder("<html><h4>").append(name).append("</h4>").append(description).append("</html>").toString();
    }

    public static abstract class ItemAction extends AbstractItemActions {

    }

    private static abstract class AbstractItemActions {
        public boolean canBeFoundInChest() {
            return true;
        }
        public abstract void doEffect();
        public abstract void doReversedEffect();
    }
}