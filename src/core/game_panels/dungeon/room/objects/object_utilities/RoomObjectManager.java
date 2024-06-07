package core.game_panels.dungeon.room.objects.object_utilities;

import core.game_panels.dungeon.room.objects.entity.Player;
import core.utilities.Manager;

public class RoomObjectManager extends Manager.List<RoomObject>{
    private Player player;

    public RoomObjectManager(Player player) {
        this.player = player;
    }

    @Override
    public void update() {
        super.update();
        objectInteractions();
        for (RoomObject object : get()) {
            if (player.collides(object)) {
                player.collide(object);
            }
        }
    }

    public void objectInteractions() {
        for (RoomObject object : get()) {
            if (player.interacts(object)) {
                player.setInteractable(object);
                return;
            }
        }
        player.setInteractable(null);
    }
}
