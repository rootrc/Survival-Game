package core.dungeon.mechanics.particles;

import java.awt.Color;

import core.dungeon.room.entity.Player;
import core.dungeon.room.tile.Tile;
import core.utilities.RNGUtilities;

public class SnowParticles extends ParticleSystem {
    private static final double SPEED = 1.5;
    private Player player;
    private int N;
    private double depth;

    public SnowParticles(Player player) {
        super();
        this.player = player;
        N = (int) ((double) getHeight() / SPEED / 10.0);
        for (int i = 0; i < getHeight(); i++) {
            if (RNGUtilities.getBoolean()) {
                update();
            } else {
                super.update();
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (getParticleCount() < N) {
            if (RNGUtilities.getBoolean((N - getParticleCount()) / 10.0)) {
                addParticle(new SnowParticle());
            }
        }
    }

    public void setDepth(int depth) {
        N = (int) Math.round(N * Math.pow(1.5, (depth - this.depth)));
        for (int i = 0; i < getParticleCount(); i++) {
            getParticles().get(i).ySpeed = (int) Math.round(getParticles().get(i).ySpeed * Math.pow(1.5, (depth - this.depth)));
        }
        this.depth = depth;
    }

    private class SnowParticle extends Particle {
        SnowParticle() {
            super(3, Color.WHITE, (depth * depth / 100.0 + RNGUtilities.getDouble(0.1)) * player.getStats().getSnowVisibilityMulti(), RNGUtilities.getInt(getWidth()), 0,
                    RNGUtilities.getDouble(depth * depth / 100.0), SPEED + RNGUtilities.getDouble(1), 0, 0);
        }

        protected boolean isInvalid() {
            return y > getHeight() - Tile.SIZE;
        }

        protected boolean shouldDraw() {
            return true;
        }
    }
}