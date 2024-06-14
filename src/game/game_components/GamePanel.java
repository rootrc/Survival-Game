package game.game_components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

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
        add(fadingEffect, 0);
    }

    public void enterFrame() {
        fadingEffect.fadeIn();
    }

    public void updateComponent() {
        updateChildren();
    }

    private final void updateChildren() {
        for (Component component : getComponents()) {
            if (component instanceof GameComponent) {
                ((GameComponent) component).updateComponent();
            }
        }
    }

    public void add(GameComponent gameComponent) {
        add(gameComponent, 1);
    }

}
