package core.game_panels.dungeon.mechanics.inventory;

import java.awt.Rectangle;

import core.game_panels.dungeon.mechanics.inventory.items.Item;
import core.utilities.GameButton;

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
        setToolTipText("<html>" + item.getName() + "<br>" + item.getDescription() + "</html>");
    }
}
