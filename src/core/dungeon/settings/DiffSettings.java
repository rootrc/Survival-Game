package core.dungeon.settings;

// This is kind of a hack and is unsafe but it works
public class DiffSettings {
    public static int difficulty;
    public static int startingInventorySize;
    public static int startingHealth;
    public static int playerLightRadiusFactor;
    public static int playerDetectionRadiusSquared;
    public static double lightRockValue;
    public static int lightRockLightRadius;

    private DiffSettings() {
    }

    public static void setEasy() {
        difficulty = 0;
        startingInventorySize = 2;
        startingHealth = 6;
        playerLightRadiusFactor = 35;
        playerDetectionRadiusSquared = 250;
        lightRockValue = 0.06;
        lightRockLightRadius = 60;
    }
    
    public static void setMedium() {
        difficulty = 1;
        startingInventorySize = 2;
        startingHealth = 5;
        playerLightRadiusFactor = 25;
        playerDetectionRadiusSquared = 200;
        lightRockValue = 0.04;
        lightRockLightRadius = 40;
    }

    public static void setHard() {
        difficulty = 2;
        startingInventorySize = 2;
        startingHealth = 4;
        playerLightRadiusFactor = 15;
        playerDetectionRadiusSquared = 100;
        lightRockValue = 0.02;
        lightRockLightRadius = 20;
    }
}