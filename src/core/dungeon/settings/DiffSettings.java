package core.dungeon.settings;

// This is kind of a hack and is unsafe but it works
public class DiffSettings {
    public static int startingInventorySize;
    public static int startingHealth;
    public static int playerLightStartAmount;
    public static int playerLightRadiusFactor;
    public static int playerLightDecreaseFactor;
    public static int playerLightDetectionRadiusSquared;
    public static int lightRockValue;
    public static int lightRockLightRadius;

    public static double itemHealthRegen;
    public static double itemDamageMulti;
    public static int itemHealthIncrease;
    public static int itemHealthPointIncrease;
    public static int itemSpeedIncrease;
    public static double itemAccDecrease;

    private DiffSettings() {
    }

    public static void setEasy() {
        startingInventorySize = 8;
        startingHealth = 5;
        playerLightStartAmount = 1000;
        playerLightRadiusFactor = 30;
        playerLightDecreaseFactor = 75;
        playerLightDetectionRadiusSquared = 300;
        lightRockValue = 20;
        lightRockLightRadius = 25;

        itemHealthRegen = 1.0/60;
        itemDamageMulti = 0.5;
        itemHealthIncrease = 3;
        itemHealthPointIncrease = 1;
        itemSpeedIncrease = 1;
        itemAccDecrease = 4;
    }
}
