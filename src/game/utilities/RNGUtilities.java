package game.utilities;

import java.util.Random;

public class RNGUtilities {
    public static final Random random = new Random();

    public static int getInt(int l, int r) {
        return random.nextInt(l, r);
    }

    public static int getInt(int r) {
        return random.nextInt(r);
    }

    public static double getDouble(double l, double r) {
        return random.nextDouble(l, r);
    }

    public static double getDouble(double r) {
        return random.nextDouble(r);
    }

    public static double getDouble() {
        return random.nextDouble();
    }

    public static boolean getBoolean(double p) {
        return random.nextDouble(1) < p;
    }

    public static boolean getBoolean() {
        return random.nextBoolean();
    }
}
