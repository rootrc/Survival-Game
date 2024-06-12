package game.dungeon.inventory;

import java.awt.Rectangle;

import game.game_components.GameButton;

public class ItemSlot extends GameButton {
    private Item item;

    public ItemSlot(Rectangle rect) {
        super(null, null, rect);
    }

    public void setItem(Item item) {
        this.item = item;
        setAction(item.useItem);
        setIcon(item.getImageIcon());
        setRolloverIcon(item.getRolloverIcon());
        setToolTipText(item.getToolTip());
    }
}
