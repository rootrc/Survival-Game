package game.dungeon.room_factory;

import game.dungeon.room.entity.Player;
import game.dungeon.room.object_utilities.RoomObject;
import game.dungeon.room.object_utilities.RoomObjectManager;
import game.dungeon.room.tile.TileGrid;
import game.dungeon.room.object_utilities.RoomObject.RoomObjectData;
import game.game_components.Factory;

class RoomObjectManagerFactory extends Factory<RoomObjectManager> {
    private Player player;

    RoomObjectManagerFactory(Player player) {
        this.player = player;
    }

    public RoomObjectManager getRoomObjectManager(RoomFileData file, TileGrid tileGrid) {
        RoomObjectManager objectManager = new RoomObjectManager(player, tileGrid);
        for (RoomObjectData data : file.getRoomObjects()) {
            objectManager.add(RoomObject.getRoomObject(data), -1);
        }
        return objectManager;
    }

    // TEMP
    public RoomObjectManager getRoomObjectManager(TileGrid tileGrid) {
        return new RoomObjectManager(player, tileGrid);
    }
}