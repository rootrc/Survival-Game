package core.dungeon.inventory;

import java.awt.Rectangle;

import core.dungeon.items.Item;
import core.dungeon.settings.KeyBinds;
import core.game_components.GameButton;
import core.utilities.ImageUtilities;

public class ItemSlot extends GameButton {
    private int idx;
    private Item item;

    public ItemSlot(int idx, Rectangle rect) {
        super(null, rect);
        this.idx = idx;
        getInputMap(2).put(KeyBinds.NUMBER[idx], idx);
    }

    public void setItem(Item item) {
        this.item = item;
        setAction(item.getAction());
        getActionMap().put(idx, getAction());
        setIcon(ImageUtilities.resize(item.getImageIcon(), 32, 32));
        setRolloverIcon(ImageUtilities.resize(item.getImageIcon(), 32, 32));
        setToolTipText(item.getToolTip());
    }

    public Item getItem() {
        return item;
    }
}
