package core.dungeon.room.objects.object_utilities;

import core.dungeon.room.objects.Ladder;
import core.dungeon.room.objects.TreasureChest;
import core.utilities.Factory;
import core.utilities.ImageUtilities;
import core.window.GamePanel;

public class RoomObjectFactory extends Factory<RoomObject> {

    public RoomObject getRoomObject(RoomObjectData data) {
        if (data.id == 0 || data.id == 1) {
            return getLadder(data);
        } else if (10 <= data.id && data.id <= 14) {
            return getTreasureChest(data);
        }
        return null;
    }

    private Ladder getLadder(RoomObjectData data) {
        if (data.id == 0) {
            return new Ladder(ImageUtilities.getImage("objects", "ladderup"), data.r, data.c,
                    0, GamePanel.TILESIZE * 2,
                    new CollisionBox(0, 1.25, 1, 0.75),
                    new CollisionBox(-0.25, 1, 1.5, 1.75), 1);
        } else {
            return new Ladder(ImageUtilities.getImage("objects", "ladderdown"), data.r, data.c,
                    GamePanel.TILESIZE / 2, -GamePanel.TILESIZE * 3 / 4,
                    new CollisionBox(0.5, 0.5, 1, 1),
                    new CollisionBox(0.25, -0.25, 1.5, 2), -1);
        }
    }

    private TreasureChest getTreasureChest(RoomObjectData data) {
        if (data.id == 10) {
            return new TreasureChest(data.r, data.c,
                    new CollisionBox(0.375, 1, 1.25, 0.8125),
                    new CollisionBox(0.375, 1.5, 1.25, 1.0625), 0);
        } else if (data.id == 11) {
            return new TreasureChest(data.r, data.c,
                    new CollisionBox(0.3125, 0.8125, 1.375, 1),
                    new CollisionBox(0.3125, 1.3125, 1.375, 1.25), 1);
        } else if (data.id == 12) {
            return new TreasureChest(data.r, data.c,
                    new CollisionBox(0.3125, 0.8125, 1.375, 1),
                    new CollisionBox(0.3125, 1.3125, 1.375, 1.25), 2);
        } else if (data.id == 13) {
            return new TreasureChest(data.r, data.c,
                    new CollisionBox(0.125, 0.6875, 1.75, 1.125),
                    new CollisionBox(0.125, 1.1875, 1.75, 1.375), 3);
        } else if (data.id == 14) {
            return new TreasureChest(data.r, data.c,
                    new CollisionBox(0.3125, 0.8125, 1.375, 1),
                    new CollisionBox(0.3125, 1.3125, 1.375, 1.25), 4);
        }
        return null;
    }

    public static class RoomObjectData {
        private int id, r, c;

        public RoomObjectData(int id, int r, int c) {
            this.id = id;
            this.r = r;
            this.c = c;
        }
    }
}
