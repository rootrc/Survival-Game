package game.dungeon.mechanics;

import java.awt.Color;

import game.dungeon.Dungeon;
import game.utilities.RNGUtilities;

public class SnowParticles extends ParticleSystem {
    private static final int speed = 6;
    private int N;

    public SnowParticles(int width, int height) {
        super(width, height);
        for (int i = 0; i < 2 * height / speed; i++) {
            if (RNGUtilities.getBoolean(0.2)) {
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
            super(3, Color.white, 0.4, RNGUtilities.getInt(getWidth()), 0, 0, speed / 2 + RNGUtilities.getDouble(2),
                    0, 0);
        }

        protected boolean isInvalid() {
            return y > getHeight() - Dungeon.TILESIZE;
        }

        protected boolean shouldDraw() {
            return true;
        }
    }
}
