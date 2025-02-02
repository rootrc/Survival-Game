package core.dungeon.mechanics.particles;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import core.Game;
import core.dungeon.room.tile.TileGrid;
import core.game_components.GameComponent;

public abstract class ParticleSystem extends GameComponent {
    private final ArrayList<Particle> particles = new ArrayList<>();

    public ParticleSystem(TileGrid tileGrid) {
        super(tileGrid.getWidth(), tileGrid.getHeight());
    }

    public ParticleSystem() {
        super(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
    }
    
    public void update() {
        for (int i = 0; i < particles.size(); i++) {
            particles.get(i).update();
            if (particles.get(i).isInvalid()) {
                particles.remove(particles.get(i));
            }
        }
    }

    public final void drawComponent(Graphics2D g2d) {
        if (Game.TEST) {
            return;
        }
        for (int i = 0; i < particles.size(); i++) {
            if (particles.get(i).shouldDraw()) {
                particles.get(i).draw(g2d);
            }
        }
    }

    public final ArrayList<Particle> getParticles() {
        return particles;
    }

    public final int getParticleCount() {
        return particles.size();
    }

    public final void addParticle(Particle particle) {
        particles.add(particle);
    }

    protected abstract class Particle {
        protected int size;
        protected Color color;
        private AlphaComposite opacity;
        protected double x;
        protected double y;
        protected double xSpeed;
        protected double ySpeed;
        private double xAcc;
        private double yAcc;
        protected int time;

        Particle(int size, Color color, double opacity, double x, double y, double xSpeed, double ySpeed, double xAcc,
                double yAcc) {
            this.size = size;
            this.color = color;
            this.opacity = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) opacity);
            this.x = x;
            this.y = y;
            this.xSpeed = xSpeed;
            this.ySpeed = ySpeed;
            this.xAcc = xAcc;
            this.yAcc = yAcc;
        }

        public void update() {
            x += xSpeed;
            y += ySpeed;
            xSpeed += xAcc;
            ySpeed += yAcc;
            if (Math.abs(xSpeed) < 0.000001) {
                xAcc = 0;
            }
            if (Math.abs(ySpeed) < 0.000001) {
                yAcc = 0;
            }
            time++;
        }

        public void draw(Graphics2D g2d) {
            g2d.setColor(color);    
            g2d.setComposite(opacity);
            g2d.fillRect((int) (x) - size / 2, (int) (y) - size / 2, size, size);
        }

        protected abstract boolean isInvalid();

        protected abstract boolean shouldDraw();

        public final void setOpacity(double opacity) {
            this.opacity = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    (float) Math.max(0, Math.min(1, opacity)));
        }
    }
}