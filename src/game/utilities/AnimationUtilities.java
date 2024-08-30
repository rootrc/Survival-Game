package game.utilities;

public class AnimationUtilities {
    private AnimationUtilities() {
    }

    public static double easeOutQuad(double x) {
        return 1 - (1 - x) * (1 - x);
    }

    public static double easeInQuad(double x) {
        return x * x;
    }
}
