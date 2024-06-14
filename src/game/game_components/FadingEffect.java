package game.game_components;

import java.awt.Color;
import java.awt.Graphics2D;

import game.Game;

class FadingEffect extends GameComponent{
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
