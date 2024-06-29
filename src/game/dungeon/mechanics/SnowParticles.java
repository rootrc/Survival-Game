package game.dungeon.mechanics;

import java.awt.Color;

import game.dungeon.Dungeon;

public class SnowParticles extends ParticleSystem {
    private static final int speed = 5;
    private CollisionChecker collision;

    public SnowParticles(int width, int height, CollisionChecker collision) {
        super(width, height);
        this.collision = collision;
        for (int i = 0; i < height / speed; i++) {
            update();
        }
    }

    @Override
    public void update() {
        super.update();
        if (Math.random() < 0.5) {
            addParticle(new SnowParticle());
        }
    }

    protected boolean checkValid(Particle particle) {
        return particle.y > getHeight() - Dungeon.TILESIZE;
    }

    protected boolean shouldDraw(Particle particle) {
        return collision.checkPoint(particle.x, particle.y);
    }

    private class SnowParticle extends Particle {
        SnowParticle() {
            super(2, Color.white, (int) (Math.random() * getWidth()), 0, 0, (int) (speed / 2 + speed * Math.random()),
                    0, 0);
        }
    }
}
