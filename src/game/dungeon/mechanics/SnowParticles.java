package game.dungeon.mechanics;

import java.awt.Color;

import game.dungeon.Dungeon;

public class SnowParticles extends ParticleSystem {
    private static final int speed = 6;
    private int N;
    private CollisionChecker collision;

    public SnowParticles(int width, int height, CollisionChecker collision) {
        super(width, height);
        this.collision = collision;
        for (int i = 0; i < 2 * height / speed; i++) {
            if (Math.random() < 0.5) {
                addParticle(new SnowParticle());
            }
            update();
        }
        N = getParticleCount();
    }

    @Override
    public void update() {
        super.update();
        if (getParticleCount() < N) {
            addParticle(new SnowParticle());
        }
    }

    private class SnowParticle extends Particle {
        SnowParticle() {
            super(2, Color.white, (int) (Math.random() * getWidth()), 0, 0, (int) (speed / 2 + speed * Math.random()),
                    0, 0);
        }

        protected boolean isInvalid() {
            return y > getHeight() - Dungeon.TILESIZE;
        }

        protected boolean shouldDraw() {
            return collision.checkPoint(x, y);
        }
    }
}
