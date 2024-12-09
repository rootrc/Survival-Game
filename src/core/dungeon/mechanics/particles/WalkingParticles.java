package core.dungeon.mechanics.particles;

import java.awt.Color;

import core.Game;
import core.dungeon.room.entity.Entity;
import core.dungeon.room.object_utilities.DirectionUtilities;
import core.dungeon.room.tile.TileGrid;
import core.utilities.RNGUtilities;

public class WalkingParticles extends ParticleSystem {
    private Entity entity;

    public WalkingParticles(TileGrid tileGrid, Entity entity) {
            super(tileGrid);
        this.entity = entity;
    }

    @Override
    public void update() {
        super.update();
        if (entity.isMoving()) {
            if (RNGUtilities.getBoolean(0.3)) {
                addParticle(new WalkParticle());
            }
        }
    }

    private class WalkParticle extends Particle {
        private int lifespan;

        WalkParticle() {
            super(1, Color.GRAY, 1,
                    entity.getX() + entity.getHitbox().getX() + RNGUtilities.getDouble(entity.getHitbox().getWidth()),
                    entity.getY() + entity.getHitbox().getY() + RNGUtilities.getDouble(entity.getHitbox().getHeight()),
                    -2 * DirectionUtilities.getXMovement(entity.getDirection()), -2 * DirectionUtilities.getYMovement(entity.getDirection()),
                    DirectionUtilities.getXMovement(entity.getDirection()) / 10.0, DirectionUtilities.getYMovement(entity.getDirection()) / 10.0);
            lifespan = 5 * Game.UPS + RNGUtilities.getInt(8 * Game.UPS);
        }

        @Override
        public void update() {
            super.update();
            setOpacity(1 - (double) time / lifespan);
        }

        protected boolean isInvalid() {
            return time > lifespan;
        }

        protected boolean shouldDraw() {
            return true;
        }
    }
}