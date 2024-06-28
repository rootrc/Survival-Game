package game.dungeon.mechanics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import game.game_components.GameComponent;

public class SnowEffect extends GameComponent {
    private static final int speed = 6;
    private final ArrayList<Integer> x = new ArrayList<>();
    private final ArrayList<Integer> y = new ArrayList<>();
    private final ArrayList<Integer> s = new ArrayList<>();
    private CollisionChecker collision;

    public SnowEffect(int width, int height, CollisionChecker collision) {
        super(width, height);
        this.collision = collision;
        for (int i = 0; i < height / speed; i++) {
            update();
        }
    }

    public void update() {
        for (int i = 0; i < x.size(); i++) {
            y.set(i, y.get(i) + s.get(i));
        }
        if (Math.random() < 0.5) {
            x.add((int) (Math.random() * getWidth()));
            y.add(0);
            s.add((int) (speed / 2 + speed * Math.random()));
        }
    }

    public void drawComponent(Graphics2D g2d) {
        g2d.setColor(Color.white);
        for (int i = 0; i < x.size(); i++) {
            if (y.get(i) > getHeight() - 32) {
                x.remove(i);
                y.remove(i);
                s.remove(i);
            } else if (collision.checkPoint(x.get(i), y.get(i))) {
                g2d.fillRect(x.get(i) - 1, y.get(i) - 1, 2, 2);
            }
        }
    }

}
