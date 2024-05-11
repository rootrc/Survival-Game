package core.dungeon.room.roomfactory;

import core.dungeon.room.objects.RoomObjectFactory;
import core.dungeon.room.objects.RoomObjectManager;
import core.dungeon.room.objects.RoomObjectFactory.RoomObjectData;
import core.dungeon.room.objects.entity.Player;
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