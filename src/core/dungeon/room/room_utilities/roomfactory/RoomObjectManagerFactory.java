package core.dungeon.room.room_utilities.roomfactory;

import core.dungeon.room.objects.entity.Player;
import core.dungeon.room.objects.object_utilities.RoomObjectFactory;
import core.dungeon.room.objects.object_utilities.RoomObjectManager;
import core.dungeon.room.objects.object_utilities.RoomObjectFactory.RoomObjectData;
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