package core.dungeon.room.object;

import core.Game;
import core.dungeon.room.entity.Player;
import core.dungeon.room.object_utilities.CollisionBox;
import core.dungeon.room.object_utilities.RoomObject;
import core.dungeon.room.object_utilities.SpriteSheet;
import core.dungeon.settings.DiffSettings;
import core.utilities.ImageUtilities;
import core.utilities.RNGUtilities;

public class LightRock extends RoomObject {
    private double lightAmount;

    public LightRock(SpriteSheet spriteSheet, int r, int c, CollisionBox hitbox, CollisionBox interactbox,
            double lightAmount) {
        super(spriteSheet, r, c, hitbox, interactbox);
        this.lightAmount = lightAmount;
    }

    public void update() {
    }

    public void collides(Player player) {
    }

    public void interaction(Player player) {
        if (getSpriteSheet().getFrame() == 1) {
            return;
        }
        // 
        player.addHealth(lightAmount);
        getSpriteSheet().nextFrame();
        getSpriteSheet().getImage();
        setLightRadius(0);
    }

    public static LightRock getLightRock(int depth, RoomObjectData data) {
        LightRock lightRock;
        switch (data.id) {
            case RoomObjectData.SMALL_LIGHTROCK:
                if (-(10 - depth) * (10 - depth) / 40.0 + 7.5 > RNGUtilities.getInt(10) && !Game.TEST) {
                    return null;
                }
                lightRock = new LightRock(new SpriteSheet(ImageUtilities.getImage("objects", "lightRock0"), 2),
                        data.r, data.c,
                        CollisionBox.getCollisionBox(0, 1.125, 1, 0.75),
                        CollisionBox.getCollisionBox(-0.5, 0.625, 2, 1.75), DiffSettings.lightRockValue);
                lightRock.setLightRadius(DiffSettings.lightRockLightRadius);
                return lightRock;
            case RoomObjectData.MEDIUM_LIGHTROCK:
                if (depth > RNGUtilities.getInt(40) && !Game.TEST) {
                    return null;
                }
                lightRock = new LightRock(new SpriteSheet(ImageUtilities.getImage("objects", "lightRock1"), 2),
                        data.r, data.c,
                        CollisionBox.getCollisionBox(0.125, 0.5, 1.75, 1.375),
                        CollisionBox.getCollisionBox(-0.375, 0, 2.75, 2.375), 4 * DiffSettings.lightRockValue);
                lightRock.setLightRadius(2 * DiffSettings.lightRockLightRadius);
                return lightRock;
            case RoomObjectData.LARGE_LIGHTROCK:
                lightRock = new LightRock(new SpriteSheet(ImageUtilities.getImage("objects", "lightRock2"), 2),
                        data.r, data.c,
                        CollisionBox.getCollisionBox(0.5, 1.375, 2, 1.5),
                        CollisionBox.getCollisionBox(0, 0.875, 3, 2.5), 6 * DiffSettings.lightRockValue);
                lightRock.setLightRadius(4 * DiffSettings.lightRockLightRadius);
                return lightRock;
            default:
                return null;
        }
    }
}