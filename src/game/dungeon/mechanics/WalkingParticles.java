package game.dungeon.mechanics;

import java.awt.Color;

import javax.tools.Diagnostic;

import game.Game;
import game.dungeon.room.entity.Entity;
import game.dungeon.room.object_utilities.DirectionUtilities;

public class WalkingParticles extends ParticleSystem {
    private Entity entity;

    public WalkingParticles(int width, int height, Entity entity) {
        super(width, height);
        this.entity = entity;
    }

    @Override
    public void update() {
        super.update();
        if (entity.isMoving()) {
            if (Math.random() < 0.3) {
                addParticle(new WalkParticle());
            }
        }
    }

    private class WalkParticle extends Particle {
        private int lifespan;

        WalkParticle() {
            super(1, Color.gray,
                    entity.getX() + entity.getHitBox().getX() + Math.random() * entity.getHitBox().getWidth(),
                    entity.getY() + entity.getHitBox().getY() + Math.random() * entity.getHitBox().getHeight(),
                    -2 * DirectionUtilities.getXDirection(entity), -2 * DirectionUtilities.getYDirection(entity),
                    DirectionUtilities.getXDirection(entity) / 10.0, DirectionUtilities.getYDirection(entity) / 10.0);
            lifespan = 5 * Game.UPS + (int) (8 * Game.UPS * Math.random());
        }

        protected boolean isInvalid() {
            return t > lifespan;
        }

        protected boolean shouldDraw() {
            return true;
        }
    }
}
