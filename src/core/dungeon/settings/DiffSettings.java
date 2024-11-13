package core.dungeon.settings;

// This is kind of a hack and is unsafe but it works
public class DiffSettings {
    public static int startingInventorySize;
    public static int startingHealth;
    public static int playerLightStartAmount;
    public static int playerLightRadiusFactor;
    public static int playerLightDecreaseFactor;
    public static int playerDetectionRadiusSquared;
    public static int lightRockValue;
    public static int lightRockLightRadius;

    private DiffSettings() {
    }

    public static void setEasy() {
        startingInventorySize = 8;
        startingHealth = 5;
        playerLightStartAmount = 1000;
        playerLightRadiusFactor = 30;
        playerLightDecreaseFactor = 75;
        playerDetectionRadiusSquared = 200;
        lightRockValue = 20;
        lightRockLightRadius = 25;
    }
}