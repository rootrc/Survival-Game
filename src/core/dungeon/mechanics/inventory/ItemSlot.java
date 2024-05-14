package core.dungeon.mechanics.inventory;

import java.awt.Rectangle;

import javax.swing.Action;

import core.dungeon.mechanics.inventory.items.Item;
import core.utilities.GameButton;

public class ItemSlot extends GameButton {
    private Item item;

    public ItemSlot(Action action, String actionCommand, Rectangle rect) {
        super(action, actionCommand, rect);   
    }

    public void setItem(Item item) {
        this.item = item;
        setIcon(item.getImage());
    }
}
