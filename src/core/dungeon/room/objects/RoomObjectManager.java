package core.dungeon.room.objects;

import core.dungeon.room.objects.entity.Player;
import core.dungeon.room.objects.objectUtilities.RoomObject;
import core.utilities.Manager;

public class RoomObjectManager extends Manager.List<RoomObject>{
    private Player player;

    public RoomObjectManager(Player player) {
        this.player = player;
    }

    public void update() {
        super.update();
        for (RoomObject object : get()) {
            if (player.interacts(object)) {
                object.interaction(player);
            }
            if (player.collides(object)) {
                player.collide(object);
            }
        }
    }
}
