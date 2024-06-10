package game.game_components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Action;
import javax.swing.JPanel;

import game.Game;

public abstract class GamePanel extends JPanel {
    public final static int TILESIZE = 16;
    public final static int maxScreenRow = 48;
    public final static int maxScreenCol = 64;
    public final static int screenWidth = TILESIZE * maxScreenCol; // 1024 pixels
    public final static int screenHeight = TILESIZE * maxScreenRow; // 768 pixels

    public GamePanel(Action changePanel) {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        if (!Game.DEBUG) {
            setBackground(Color.black);
        }
        setLayout(null);
        setDoubleBuffered(true);
        setFocusable(true);
    }

    @Override
    public final void paintComponent(Graphics g) {
        super.paintComponent(g);
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

    public void add(GameComponent gameComponent) {
        add(gameComponent, 0);
    }

}
