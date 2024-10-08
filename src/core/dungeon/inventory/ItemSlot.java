package core.dungeon.inventory;

import java.awt.Rectangle;

import core.dungeon.items.Item;
import core.dungeon.settings.KeyBinds;
import core.game_components.GameButton;

public class ItemSlot extends GameButton {
    private int idx;
    private Item item;

    public ItemSlot(int idx, Rectangle rect) {
        super(null, rect);
        this.idx = idx;
        getInputMap(2).put(KeyBinds.useItem[idx], idx);
    }

    public void setItem(Item item) {
        this.item = item;
        item.enable(idx);
        setAction(item.getUseItem());
        getActionMap().put(idx, getAction());
        setIcon(item.getImageIcon());
        setRolloverIcon(item.getRolloverIcon());
        setToolTipText(item.getToolTip());
    }

    public Item getItem() {
        return item;
    }
}
