package game.dungeon.room.object_utilities;

import game.dungeon.Dungeon;
import game.dungeon.room.object.Ladder;
import game.dungeon.room.object.LightRock;
import game.dungeon.room.object.TreasureChest;
import game.game_components.Factory;
import game.utilities.ImageUtilities;

public class RoomObjectFactory extends Factory<RoomObject> {
    public RoomObject getRoomObject(RoomObjectData data) {
        if (data.id == 0 || data.id == 1) {
            return getLadder(data);
        } else if (10 <= data.id && data.id <= 14) {
            return getTreasureChest(data);
        } else if (15 <= data.id && data.id <= 17) {
            return getLightRock(data);
        }
        return null;
    }

    private Ladder getLadder(RoomObjectData data) {
        if (data.id == 0) {
            return new Ladder(ImageUtilities.getImage("objects", "ladderup"), data.r, data.c,
                    -Dungeon.TILESIZE / 2, Dungeon.TILESIZE,
                    new CollisionBox(0, 1.25, 1, 0.75),
                    new CollisionBox(-0.25, 1, 1.5, 1.75), 1);
        } else {
            return new Ladder(ImageUtilities.getImage("objects", "ladderdown"), data.r, data.c,
                    0, -Dungeon.TILESIZE * 3,
                    new CollisionBox(0.5, 0.375, 1, 1.125),
                    new CollisionBox(0.25, -0.25, 1.5, 2), -1);
        }
    }

    private TreasureChest getTreasureChest(RoomObjectData data) {
        switch (data.id) {
            case 10:
                return new TreasureChest(data.r, data.c,
                        new CollisionBox(0.375, 1, 1.25, 0.8125),
                        new CollisionBox(0.375, 1.5, 1.25, 1.0625), 0);
            case 11:
                return new TreasureChest(data.r, data.c,
                        new CollisionBox(0.3125, 0.8125, 1.375, 1),
                        new CollisionBox(0.3125, 1.3125, 1.375, 1.25), 1);
            case 12:
                return new TreasureChest(data.r, data.c,
                        new CollisionBox(0.3125, 0.8125, 1.375, 1),
                        new CollisionBox(0.3125, 1.3125, 1.375, 1.25), 2);
            case 13:
                return new TreasureChest(data.r, data.c,
                        new CollisionBox(0.125, 0.6875, 1.75, 1.125),
                        new CollisionBox(0.125, 1.1875, 1.75, 1.375), 3);
            case 14:
                return new TreasureChest(data.r, data.c,
                        new CollisionBox(0.3125, 0.8125, 1.375, 1),
                        new CollisionBox(0.3125, 1.3125, 1.375, 1.25), 4);

        }
        return null;

    }

    private LightRock getLightRock(RoomObjectData data) {
        switch (data.id) {
            case 15:
                return new LightRock(
                        ImageUtilities.getImage("objects", "lightRocks").getSubimage(80, 64, 16, 32),
                        ImageUtilities.getImage("objects", "lightRocks").getSubimage(80, 16, 16, 32),
                        data.r, data.c,
                        new CollisionBox(0, 1.125, 1, 0.75),
                        new CollisionBox(-0.5, 0.625, 2, 1.75));
            case 16:
                return new LightRock(
                        ImageUtilities.getImage("objects", "lightRocks").getSubimage(48, 64, 32, 32),
                        ImageUtilities.getImage("objects", "lightRocks").getSubimage(48, 16, 32, 32),
                        data.r, data.c,
                        new CollisionBox(0.125, 0.5, 1.75, 1.375),
                        new CollisionBox(-0.5, -0.125, 3, 2.625));
            case 17:
                return new LightRock(
                        ImageUtilities.getImage("objects", "lightRocks").getSubimage(0, 48, 48, 48),
                        ImageUtilities.getImage("objects", "lightRocks").getSubimage(0, 0, 48, 48),
                        data.r, data.c,
                        new CollisionBox(0.5, 1.375, 2, 1.5),
                        new CollisionBox(-0.25, 0.625, 3.5, 3));
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
