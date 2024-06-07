package core.game_panels.dungeon.room.room_utilities;

import javax.swing.Action;

import core.game_panels.dungeon.mechanics.LightingEngine;
import core.game_panels.dungeon.mechanics.inventory.InventoryManager;
import core.game_panels.dungeon.room.Room;
import core.game_panels.dungeon.room.objects.entity.Player;
import core.game_panels.dungeon.room.room_utilities.roomfactory.RoomFactory;
import core.game_panels.dungeon.room_connection.DungeonData;
import core.utilities.Manager;
import core.window.Game;
import core.window.GamePanel;

public class RoomManager extends Manager.Component<Room> {
    private RoomFactory roomFactory;
    private Player player;

    public RoomManager(DungeonData dungeonData, InventoryManager inventory, Action nextRoom) {
        player = new Player(nextRoom, inventory);
        roomFactory = new RoomFactory(dungeonData, player, new LightingEngine(player));
        set(roomFactory.getStartingRoom(1));
        // set(roomFactory.createRandomRoom(58, 32));
    }

    public void updateComponentManager() {
        if (!Game.DEBUG) {
            int tx = GamePanel.screenWidth / 2 - (int) player.getX();
            int ty = GamePanel.screenHeight / 2 - (int) player.getY();
            int dx = tx - get().getX();
            int dy = ty - get().getY();
            setLocation(get().getX() + Math.min(dx / 16, 16), get().getY() + Math.min(dy / 16, 16));
        }
    }

    public void nextRoom() {
        set(roomFactory.getNextRoom(get()));
    }
}
