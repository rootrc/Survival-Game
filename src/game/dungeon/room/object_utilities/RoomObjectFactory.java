package game.dungeon.room.object_utilities;

import game.dungeon.Dungeon;
import game.dungeon.room.object.Ladder;
import game.dungeon.room.object.LightRock;
import game.dungeon.room.object.TreasureChest;
import game.dungeon.settings.DiffSettings;
import game.game_components.Factory;
import game.utilities.ImageUtilities;

public class RoomObjectFactory extends Factory<RoomObject> {
    public RoomObject getRoomObject(RoomObjectData data) {
        if (data.id <= RoomObjectData.ladderDown) {
            return getLadder(data);
        } else if (RoomObjectData.treasureChest0 <= data.id && data.id <= RoomObjectData.treasureChest4) {
            return getTreasureChest(data);
        } else if (RoomObjectData.smallLightRock <= data.id && data.id <= RoomObjectData.largeLightRock) {
            return getLightRock(data);
        }
        return null;
    }

    private Ladder getLadder(RoomObjectData data) {
        switch (data.id) {
            case RoomObjectData.ladderUp:
                return new Ladder(ImageUtilities.getImage("objects", "ladderup"), data.r, data.c,
                        -Dungeon.TILESIZE / 2, Dungeon.TILESIZE,
                        new CollisionBox(0, 1.25, 1, 0.75),
                        new CollisionBox(-0.25, 1, 1.5, 1.75), 1);
            case RoomObjectData.ladderDown:
                return new Ladder(ImageUtilities.getImage("objects", "ladderdown"), data.r, data.c,
                        0, -Dungeon.TILESIZE * 3,
                        new CollisionBox(0.25, 0.125, 1.5, 1.625),
                        new CollisionBox(0, -0.5, 2, 2.5), -1);
        }
        return null;
    }

    private TreasureChest getTreasureChest(RoomObjectData data) {
        switch (data.id) {
            case RoomObjectData.treasureChest0:
                return new TreasureChest(data.r, data.c,
                        new CollisionBox(0.375, 1, 1.25, 0.8125),
                        new CollisionBox(0.375, 1.5, 1.25, 1.0625), 0);
            case RoomObjectData.treasureChest1:
                return new TreasureChest(data.r, data.c,
                        new CollisionBox(0.3125, 0.8125, 1.375, 1),
                        new CollisionBox(0.3125, 1.3125, 1.375, 1.25), 1);
            case RoomObjectData.treasureChest2:
                return new TreasureChest(data.r, data.c,
                        new CollisionBox(0.3125, 0.8125, 1.375, 1),
                        new CollisionBox(0.3125, 1.3125, 1.375, 1.25), 2);
            case RoomObjectData.treasureChest3:
                return new TreasureChest(data.r, data.c,
                        new CollisionBox(0.125, 0.6875, 1.75, 1.125),
                        new CollisionBox(0.125, 1.1875, 1.75, 1.375), 3);
            case RoomObjectData.treasureChest4:
                return new TreasureChest(data.r, data.c,
                        new CollisionBox(0.3125, 0.8125, 1.375, 1),
                        new CollisionBox(0.3125, 1.3125, 1.375, 1.25), 4);

        }
        return null;

    }

    private LightRock getLightRock(RoomObjectData data) {
        LightRock lightRock;
        switch (data.id) {
            case RoomObjectData.smallLightRock:
                lightRock = new LightRock(
                        ImageUtilities.getImage("objects", "lightRocks").getSubimage(80, 64, 16, 32),
                        ImageUtilities.getImage("objects", "lightRocks").getSubimage(80, 16, 16, 32),
                        data.r, data.c,
                        new CollisionBox(0, 1.125, 1, 0.75),
                        new CollisionBox(-0.5, 0.625, 2, 1.75), DiffSettings.lightRockValue);
                lightRock.setLightRadius(DiffSettings.lightRockLightRadius);
                return lightRock;
            case RoomObjectData.mediumLightRock:
                lightRock = new LightRock(
                        ImageUtilities.getImage("objects", "lightRocks").getSubimage(48, 64, 32, 32),
                        ImageUtilities.getImage("objects", "lightRocks").getSubimage(48, 16, 32, 32),
                        data.r, data.c,
                        new CollisionBox(0.125, 0.5, 1.75, 1.375),
                        new CollisionBox(-0.375, 0, 2.75, 2.375), 4 * DiffSettings.lightRockValue);
                lightRock.setLightRadius(2 * DiffSettings.lightRockLightRadius);
                return lightRock;
            case RoomObjectData.largeLightRock:
                lightRock = new LightRock(
                        ImageUtilities.getImage("objects", "lightRocks").getSubimage(0, 48, 48, 48),
                        ImageUtilities.getImage("objects", "lightRocks").getSubimage(0, 0, 48, 48),
                        data.r, data.c,
                        new CollisionBox(0.5, 1.375, 2, 1.5),
                        new CollisionBox(0, 0.875, 3, 2.5), 6 * DiffSettings.lightRockValue);
                lightRock.setLightRadius(4 * DiffSettings.lightRockLightRadius);
                return lightRock;
        }
        return null;
    }

    public static class RoomObjectData {
        private static final int ladderUp = 0;
        private static final int ladderDown = 1;
        private static final int treasureChest0 = 10;
        private static final int treasureChest1 = 11;
        private static final int treasureChest2 = 12;
        private static final int treasureChest3 = 13;
        private static final int treasureChest4 = 14;
        private static final int smallLightRock = 15;
        private static final int mediumLightRock = 16;
        private static final int largeLightRock = 17;
        
        private int id;
        private int r;
        private int c;

        public RoomObjectData(int id, int r, int c) {
            this.id = id;
            this.r = r;
            this.c = c;
        }
    }
}
