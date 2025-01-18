package core.dungeon.settings;

// This is kind of a hack and is unsafe but it works
public class DiffSettings {
    public static int startingInventorySize;
    public static int startingHealth;
    public static int playerLightRadiusFactor;
    public static int playerDetectionRadiusSquared;
    public static double lightRockValue;
    public static int lightRockLightRadius;

    private DiffSettings() {
    }

    public static void setEasy() {
        startingInventorySize = 2;
        startingHealth = 5;
        playerLightRadiusFactor = 25;
        playerDetectionRadiusSquared = 200;
        lightRockValue = 0.04;
        lightRockLightRadius = 40;
    }
}