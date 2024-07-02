package game.dungeon.room_factory;

import game.dungeon.room.entity.Player;
import game.dungeon.room.object_utilities.RoomObjectFactory;
import game.dungeon.room.object_utilities.RoomObjectManager;
import game.dungeon.room.tile.TileGrid;
import game.dungeon.room.object_utilities.RoomObjectFactory.RoomObjectData;
import game.game_components.Factory;

class RoomObjectManagerFactory extends Factory<RoomObjectManager> {
    private Player player;
    private RoomObjectFactory RoomObjectFactory;

    RoomObjectManagerFactory(Player player) {
        this.player = player;
        RoomObjectFactory = new RoomObjectFactory();
    }

    public RoomObjectManager getRoomObjectManager(RoomFileData file, TileGrid tileGrid) {
        RoomObjectManager objectManager = new RoomObjectManager(tileGrid.getWidth(), tileGrid.getHeight(), player);
        for (RoomObjectData data : file.getObjects()) {
            objectManager.add(RoomObjectFactory.getRoomObject(data), -1);
        }
        return objectManager;
    }
}