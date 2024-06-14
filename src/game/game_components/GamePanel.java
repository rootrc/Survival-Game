package game.game_components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.Action;
import javax.swing.JPanel;

import game.Game;

public abstract class GamePanel extends JPanel {
    private FadingEffect fadingEffect;

    public GamePanel(Action changePanel) {
        setPreferredSize(new Dimension(Game.screenWidth, Game.screenHeight));
        if (!Game.DEBUG) {
            setBackground(Color.black);
        }
        setLayout(null);
        setDoubleBuffered(true);
        setFocusable(true);
        fadingEffect = new FadingEffect();
        add(fadingEffect);
    }

    public void enterFrame() {
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

    public final void add(GameComponent gameComponent) {
        add(gameComponent, 0);
    }

    private class FadingEffect extends GameComponent {
        private int alpha;

        public FadingEffect() {
            super(Game.screenWidth, Game.screenWidth);
        }

        private static int speed = 10;

        public void update() {
            alpha = alpha - speed;
            alpha = Math.max(alpha, 0);
        }

        public void drawComponent(Graphics2D g2d) {
            g2d.setColor(new Color(0, 0, 0, alpha));
            g2d.fillRect(0, 0, Game.screenWidth, Game.screenWidth);
        }

        public void fadeIn() {
            alpha = 255;
        }
    }
}
