package core.game_panels.dungeon.room.room_utilities.roomfactory;

import core.game_panels.dungeon.room.objects.entity.Player;
import core.game_panels.dungeon.room.objects.object_utilities.RoomObjectFactory;
import core.game_panels.dungeon.room.objects.object_utilities.RoomObjectManager;
import core.game_panels.dungeon.room.objects.object_utilities.RoomObjectFactory.RoomObjectData;
import core.utilities.Factory;

class RoomObjectManagerFactory extends Factory<RoomObjectManager> {
    RoomObjectManager getRoomObjectManager(Player player, RoomFileData file) {
        RoomObjectManager objectManager = new RoomObjectManager(player);
        RoomObjectFactory RoomObjectFactory = new RoomObjectFactory();
        for (RoomObjectData data : file.getObjects()) {
            objectManager.add(RoomObjectFactory.getRoomObject(data));
        }
        return objectManager;
    }
}