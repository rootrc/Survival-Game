package game.dungeon.mechanics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import game.game_components.GameComponent;

public abstract class ParticleSystem extends GameComponent {
    private final ArrayList<Particle> particles = new ArrayList<>();

    public ParticleSystem(int width, int height) {
        super(width, height);
    }

    public void update() {
        for (Particle particle : particles) {
            particle.update();
        }
    }

    public final void drawComponent(Graphics2D g2d) {
        g2d.setColor(Color.white);
        for (int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(i);
            if (checkValid(particle)) {
                particles.remove(particle);
            } else if (shouldDraw(particle)) {
                particle.draw(g2d);
            }
        }
    }

    protected abstract boolean checkValid(Particle particle);

    protected abstract boolean shouldDraw(Particle particle);

    public void addParticle(Particle particle) {
        particles.add(particle);
    }

    protected class Particle {
        int size;
        Color color;
        double x;
        double y;
        double xSpeed;
        double ySpeed;
        double xAcc;
        double yAcc;

        Particle(int size, Color color, double x0, double y0, double xSpeed0, double ySpeed0, double xAcc0, double yAcc0) {
            this.size = size;
            this.color = color;
            x = x0;
            y = y0;
            xSpeed = xSpeed0;
            ySpeed = ySpeed0;
            xAcc = xAcc0;
            yAcc = yAcc0;
        }

        public void update() {
            x += xSpeed;
            y += ySpeed;
            xSpeed += xAcc;
            ySpeed += yAcc;
        }

        public void draw(Graphics2D g2d) {
            g2d.fillRect((int) (x) - size / 2, (int) (y) - size / 2, size, size);
        }
    }

}
