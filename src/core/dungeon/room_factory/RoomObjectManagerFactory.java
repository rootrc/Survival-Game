package core.dungeon.room_factory;

import core.dungeon.room.entity.Player;
import core.dungeon.room.object_utilities.RoomObject;
import core.dungeon.room.object_utilities.RoomObjectManager;
import core.dungeon.room.object_utilities.RoomObject.RoomObjectData;
import core.dungeon.room.tile.Tile;
import core.dungeon.room.tile.TileGrid;
import core.game_components.Factory;

class RoomObjectManagerFactory extends Factory<RoomObjectManager> {
    private Player player;

    RoomObjectManagerFactory(Player player) {
        this.player = player;
    }

    RoomObjectManager getRoomObjectManager(RoomFileData file, TileGrid tileGrid) {
        RoomObjectManager roomObjectManager = new RoomObjectManager(player, tileGrid);
        if (file.getModifier() == RoomFileData.NO_MODIFIER) {
            for (RoomObjectData data : file.getRoomObjects()) {
                RoomObject roomObject = RoomObject.getRoomObject(file.getDepth(), data, player, tileGrid);
                if (roomObject != null) {
                    roomObjectManager.add(roomObject);
                }
            }
        } else if (file.getModifier() == RoomFileData.REFLECTION_MODIFIER) {
            for (RoomObjectData data : file.getRoomObjects()) {
                RoomObject roomObject = getHorizontialReflection(tileGrid, RoomObject.getRoomObject(file.getDepth(), data, player, tileGrid));
                if (roomObject != null) {
                    roomObjectManager.add(roomObject);
                }
            }
        }
        return roomObjectManager;
    }

    private RoomObject getHorizontialReflection(TileGrid tileGrid, RoomObject roomObject) {
        if (roomObject == null) {
            return roomObject;
        }
        roomObject.setLocation(tileGrid.getWidth() - roomObject.getX()
                - Tile.SIZE * ((roomObject.getX() + roomObject.getWidth() - 1) / Tile.SIZE
                        - roomObject.getX() / Tile.SIZE + 1),
                roomObject.getY());

        return roomObject;
    }
}