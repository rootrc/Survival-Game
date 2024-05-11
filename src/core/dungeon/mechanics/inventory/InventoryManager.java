package core.dungeon.mechanics.inventory;

import java.awt.Graphics2D;

import core.dungeon.mechanics.inventory.items.Item;
import core.dungeon.mechanics.inventory.items.ItemFactory;
import core.utilities.Manager;
import core.window.GamePanel;

public class InventoryManager extends Manager.SubScreen<Inventory> {
    private ItemFactory itemFactory;

    public InventoryManager() {
        set(new Inventory());
        // get().setLocation(GamePanel.screenWidth / 2 - getWidth() / 2, GamePanel.screenHeight - getHeight());
        itemFactory = new ItemFactory();
    }

    public void addItem(Item item, int cnt) {
        get().addItem(item, cnt);
    }

    public void draw(Graphics2D g2d) {
        // super.draw(g2d, get().getX(), get().getY());
    }
}
