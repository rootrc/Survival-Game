package game.dungeon.mechanics.particles;

import java.awt.Color;

import game.Game;
import game.dungeon.room.entity.Entity;
import game.dungeon.room.object_utilities.DirectionUtilities;
import game.dungeon.room.tile.TileGrid;
import game.utilities.RNGUtilities;

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
            super(1, Color.gray, 1,
                    entity.getX() + entity.getHitBox().getX() + RNGUtilities.getDouble(entity.getHitBox().getWidth()),
                    entity.getY() + entity.getHitBox().getY() + RNGUtilities.getDouble(entity.getHitBox().getHeight()),
                    -2 * DirectionUtilities.getXDirection(entity), -2 * DirectionUtilities.getYDirection(entity),
                    DirectionUtilities.getXDirection(entity) / 10.0, DirectionUtilities.getYDirection(entity) / 10.0);
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
