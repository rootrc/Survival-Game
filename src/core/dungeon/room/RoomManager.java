package core.dungeon.room;

import core.dungeon.DungeonData;
import core.dungeon.mechanics.LightingEngine;
import core.dungeon.mechanics.inventory.InventoryManager;
import core.dungeon.room.objects.entity.Player;
import core.dungeon.room.roomfactory.RoomFactory;
import core.game.Game;
import core.utilities.Manager;
import core.window.GamePanel;

public class RoomManager extends Manager.SubScreen<Room> {
    private RoomFactory roomFactory;
    private Player player;

    public RoomManager(DungeonData dungeonData, InventoryManager inventory) {
        player = new Player(inventory);
        roomFactory = new RoomFactory(dungeonData, player, new LightingEngine(player));
        set(roomFactory.getStartingRoom(1));
        // set(roomFactory.createRandomRoom(58, 32));
    }

    public void update() {
        super.update();
        if (!Game.DEBUG) {
            get().setLocation(GamePanel.screenWidth / 2 - (int) Math.round(player.getX()),
                    GamePanel.screenHeight / 2 - (int) Math.round(player.getY()));
        } else {
            get().setLocation(0, 0);
        }
        if (player.getLadder() != null) {
            set(roomFactory.getNextRoom(get()));
        }
    }
}
