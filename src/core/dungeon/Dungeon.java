package core.dungeon;

import java.awt.Graphics2D;

import core.dungeon.mechanics.inventory.InventoryManager;
import core.dungeon.room.RoomManager;
import core.window.GamePanel;

public class Dungeon extends GamePanel {
    private RoomManager room;
    private DungeonData dungeonData;
    private InventoryManager inventory;

    public Dungeon() {
        dungeonData = new DungeonData();
        inventory = new InventoryManager();
        room = new RoomManager(dungeonData, inventory);
        add(room.get());
    }

    public void update() {
        room.update();
        // inventory.update();
    }

    public void draw(Graphics2D g2d) {
        room.draw(g2d);
        // inventory.draw(g2d);
    }
}