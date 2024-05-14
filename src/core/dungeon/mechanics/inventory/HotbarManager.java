package core.dungeon.mechanics.inventory;

import java.awt.Rectangle;

import javax.swing.KeyStroke;

import core.dungeon.Dungeon;
import core.dungeon.mechanics.inventory.items.Item;
import core.dungeon.mechanics.inventory.items.ItemFactory;
import core.utilities.Manager;
import core.window.GamePanel;

public class HotbarManager extends Manager.SubScreen<Hotbar> {
    private Dungeon dungeon;
    private int size;
    private ItemSlot[] inventorySlots;
    private ItemFactory itemFactory;

    public HotbarManager(Dungeon dungeon) {
        this.dungeon = dungeon;
        size = 8;
        set(new Hotbar(size));
        get().setLocation(GamePanel.screenWidth / 2 - getWidth() / 2, GamePanel.screenHeight - getHeight());
        itemFactory = new ItemFactory();

        inventorySlots = new ItemSlot[size + 1];
        String actionName, key;
        for (int i = 1; i <= size; i++) {
            actionName = String.valueOf(i);
            inventorySlots[i] = new ItemSlot(get().selectItem, actionName, new Rectangle(42 * i - 34, 8, 40, 40));
            get().add(inventorySlots[i]);
            key = "pressed " + String.valueOf(i % 10);
            get().getInputMap(2).put(KeyStroke.getKeyStroke(key), actionName);
            get().getActionMap().put(actionName, get().selectItem);
        }
        // Test

        inventorySlots[1].setItem(itemFactory.getItem(5, 0));
    }

    @Override
    public void update() {
        super.update();
    }

    public void addItem(Item item, int cnt) {
        get().addItem(item, cnt);
    }
}
