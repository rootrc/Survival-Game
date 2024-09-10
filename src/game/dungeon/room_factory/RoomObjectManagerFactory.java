package game.dungeon.room_factory;

import game.dungeon.Dungeon;
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
        RoomObjectManager roomObjectManager = new RoomObjectManager(player, tileGrid);
        if (file.getModifier() == RoomFileData.NO_MODIFIER) {
            for (RoomObjectData data : file.getRoomObjects()) {
                roomObjectManager.add(RoomObject.getRoomObject(data));
            }
        } else if (file.getModifier() == RoomFileData.REFLECTION_MODIFIER) {
            for (RoomObjectData data : file.getRoomObjects()) {
                roomObjectManager.add(getHorizontialReflection(tileGrid, RoomObject.getRoomObject(data)));
            }
        }
        return roomObjectManager;
    }

    private RoomObject getHorizontialReflection(TileGrid tileGrid, RoomObject roomObject) {
        roomObject.setLocation(tileGrid.getWidth() - roomObject.getX()
                - Dungeon.TILESIZE * (roomObject.getMaxCol() - roomObject.getMinCol() + 1), roomObject.getY());
        return roomObject;
    }
}