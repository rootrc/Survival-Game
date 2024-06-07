package core.game_panels.dungeon.mechanics.inventory;

import core.game_panels.dungeon.mechanics.inventory.items.ItemFactory;
import core.utilities.Manager;
import core.window.GamePanel;

public class InventoryManager extends Manager.Component<Inventory> {
    private int size;
    private ItemFactory itemFactory;

    public InventoryManager() {
        size = 8;
        set(new Inventory(size));
        setLocation(GamePanel.screenWidth / 2 - getWidth() / 2, GamePanel.screenHeight - getHeight());
        itemFactory = new ItemFactory();

        // Test
        get().inventorySlots[1].setItem(itemFactory.getItem(5, 0));
        get().getActionMap().put(1, get().inventorySlots[1].getAction());
    }

    public void updateComponentManager() {

    }
}
