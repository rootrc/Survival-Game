package core.dungeon.room.room_utilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Timer;

import core.dungeon.Dungeon;
import core.dungeon.mechanics.LightingEngine;
import core.dungeon.mechanics.inventory.HotbarManager;
import core.dungeon.room.Room;
import core.dungeon.room.objects.entity.Player;
import core.dungeon.room.room_utilities.roomfactory.RoomFactory;
import core.dungeon.room_connection.DungeonData;
import core.utilities.Manager;
import core.window.Game;
import core.window.GamePanel;

public class RoomManager extends Manager.Component<Room> {
    private Dungeon dungeon;
    private RoomFactory roomFactory;
    private Player player;

    public RoomManager(Dungeon dungeon, DungeonData dungeonData, HotbarManager inventory, Action nextRoom) {
        this.dungeon = dungeon;
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
