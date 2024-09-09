package game.utilities;

import java.awt.Point;

public class Easing {
    private Point p0;
    private Point p1;
    private double frames;

    public Easing(int frames) {
        this.frames = frames;
        p0 = new Point();
        p1 = new Point();
    }

    public void set(Point p0, Point p1) {
        this.p0 = p0;
        this.p1 = p1;
    }

    public Point getP0() {
        return p0;
    }

    public Point getP1() {
        return p1;
    }

    public static double easeOutQuad(double x) {
        return 1 - (1 - x) * (1 - x);
    }

    public static double easeInQuad(double x) {
        return x * x;
    }

    public static double easeInOutQuad(double x) {
        return x < 0.5 ? 2 * x * x : 1 - Math.pow(-2 * x + 2, 2) / 2;
    }

    public Point easeOutQuad(int x) {
        double multi = easeOutQuad(x / frames);
        return new Point((int) (p0.getX() + (p1.getX() - p0.getX()) * multi),
                (int) (p0.getY() + (p1.getY() - p0.getY()) * multi));
    }

    public Point easeInQuad(int x) {
        double multi = easeInQuad(x / frames);
        return new Point((int) (p0.getX() + (p1.getX() - p0.getX()) * multi),
        (int) (p0.getY() + (p1.getY() - p0.getY()) * multi));
    }

    public Point easeInOutQuad(int x) {
        double multi = easeInOutQuad(x / frames);
        return new Point((int) (p0.getX() + (p1.getX() - p0.getX()) * multi),
                (int) (p0.getY() + (p1.getY() - p0.getY()) * multi));
    }

}
