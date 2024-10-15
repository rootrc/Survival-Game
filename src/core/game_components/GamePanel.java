package core.game_components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import core.Game;

// Custom GamePanel that updates children with utility methods and graphical features
public abstract class GamePanel extends JPanel {
    private FadingEffect fadingEffect;
    protected UILayer UILayer;
    // private Game game;

    public GamePanel(Game game, UILayer UILayer) {
        this.UILayer = UILayer;
        setPreferredSize(new Dimension(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT));
        if (!Game.DEBUG) {
            setBackground(Color.black);
        }
        setLayout(null);
        setDoubleBuffered(true);
        setFocusable(true);
        fadingEffect = new FadingEffect();
        add(fadingEffect, 0);
    }

    public void updateComponent() {
        updateChildren();
    }

    private void updateChildren() {
        for (Component component : getComponents()) {
            if (component instanceof GameComponent) {
                ((GameComponent) component).updateComponent();
            }
        }
    }

    // Utility method as gamecomponents are usually added to the back
    public void add(GameComponent gameComponent) {
        add(gameComponent, 1);
    }

    public void fadeIn() {
        fadingEffect.fadeIn(FadingEffect.DEFAULT_SPEED);
    }

    public void fadeOut() {
        fadingEffect.fadeOut(FadingEffect.DEFAULT_SPEED);
    }

    public void fadeIn(int speed) {
        fadingEffect.fadeIn(speed);
    }

    public void fadeOut(int speed) {
        fadingEffect.fadeOut(speed);
    }

    private static class FadingEffect extends GameComponent {
        private static final int DEFAULT_SPEED = 10;

        private int alpha;
        private int speed;
        private boolean fadeIn;

        public FadingEffect() {
            super(Game.SCREEN_WIDTH, Game.SCREEN_WIDTH);
            fadeIn = true;
            speed = DEFAULT_SPEED;
        }

        public void update() {
            if (fadeIn) {
                alpha = Math.max(alpha - speed, 0);
            } else {
                alpha = Math.min(alpha + speed, 255);
            }
        }

        public void drawComponent(Graphics2D g2d) {
            g2d.setColor(new Color(0, 0, 0, alpha));
            g2d.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_WIDTH);
        }

        public void fadeIn(int speed) {
            this.speed = speed;
            alpha = 255;
            fadeIn = true;
        }

        public void fadeOut(int speed) {
            this.speed = speed;
            alpha = 0;
            fadeIn = false;
        }

    }
}
