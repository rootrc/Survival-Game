package core.dungeon.mechanics.particles;

import java.awt.Color;

import core.dungeon.room.tile.Tile;
import core.utilities.RNGUtilities;

public class SnowParticles extends ParticleSystem {
    private static final double SPEED = 1.5;
    
    private int N;

    public SnowParticles() {
        super();
        for (int i = 0; i < getHeight() / SPEED; i++) {
            if (RNGUtilities.getBoolean(0.1)) {
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
            super(3, Color.white, 0.1, RNGUtilities.getInt(getWidth()), 0, 0, SPEED + RNGUtilities.getDouble(1),
                    0, 0);
        }

        protected boolean isInvalid() {
            return y > getHeight() - Tile.SIZE;
        }

        protected boolean shouldDraw() {
            return true;
        }
    }
}