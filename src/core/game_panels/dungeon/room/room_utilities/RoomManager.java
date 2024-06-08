package core.game_panels.dungeon.room.room_utilities;

import javax.swing.Action;

import core.game_panels.dungeon.mechanics.LightingEngine;
import core.game_panels.dungeon.mechanics.inventory.InventoryManager;
import core.game_panels.dungeon.room.Room;
import core.game_panels.dungeon.room.objects.entity.Player;
import core.game_panels.dungeon.room.room_utilities.roomfactory.RoomFactory;
import core.game_panels.dungeon.room_connection.DungeonData;
import core.utilities.Manager;

public class RoomManager extends Manager.TComponent<Room> {
    private RoomFactory roomFactory;
    private Player player;

    public RoomManager(DungeonData dungeonData, InventoryManager inventory, Action nextRoom) {
        player = new Player(nextRoom, inventory);
        roomFactory = new RoomFactory(dungeonData, player, new LightingEngine(player));
        set(roomFactory.getStartingRoom(1));
        // set(roomFactory.createRandomRoom(58, 32));
    }

    public void nextRoom() {
        set(roomFactory.getNextRoom(get()));
    }
}
