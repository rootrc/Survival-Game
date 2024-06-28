package game.dungeon.inventory;

import java.awt.Rectangle;

import javax.swing.KeyStroke;

import game.dungeon.items.Item;
import game.game_components.GameButton;

public class ItemSlot extends GameButton {
    private int idx;
    private Item item;

    public ItemSlot(int idx, Rectangle rect) {
        super(null, null, rect);
        this.idx = idx;
        getInputMap(2).put(
                    KeyStroke.getKeyStroke((new StringBuilder("pressed ").append(idx % 10)).toString()), idx);
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
