package core.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public abstract class GamePanel extends JPanel {
    public final static int TILESIZE = 16;
    public final static int maxScreenRow = 48;
    public final static int maxScreenCol = 64;
    public final static int screenWidth = TILESIZE * maxScreenCol; // 1024 pixels
    public final static int screenHeight = TILESIZE * maxScreenRow; // 768 pixels

    public GamePanel() {
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
    
    public abstract void update();
}
