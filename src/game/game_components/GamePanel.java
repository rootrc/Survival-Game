package game.game_components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import game.Game;

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
        add(fadingEffect);
    }

    public void fadeIn() {
        fadingEffect.fadeIn();
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
    public final void add(GameComponent gameComponent) {
        add(gameComponent, 0);
    }

    // Fading effect
    private static class FadingEffect extends GameComponent {
        private int alpha;
        private static int speed = 10;

        public FadingEffect() {
            super(Game.SCREEN_WIDTH, Game.SCREEN_WIDTH);
        }

        public void update() {
            alpha = Math.max(alpha - speed, 0);
        }

        public void drawComponent(Graphics2D g2d) {
            g2d.setColor(new Color(0, 0, 0, alpha));
            g2d.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_WIDTH);
        }

        public void fadeIn() {
            alpha = 255;
        }
    }
}
