package core.dungeon;

import core.dungeon.mechanics.inventory.HotbarManager;
import core.dungeon.room.room_utilities.RoomManager;
import core.dungeon.room_connection.DungeonData;
import core.window.GamePanel;

public class Dungeon extends GamePanel {
    private RoomManager room;
    private DungeonData dungeonData;
    private HotbarManager inventory;

    public Dungeon() {
        dungeonData = new DungeonData();
        inventory = new HotbarManager(this);
        room = new RoomManager(this, dungeonData, inventory);
        add(inventory.get());
        add(room.get());
    }

    public void update() {
        room.update();
        inventory.update();
    }
}