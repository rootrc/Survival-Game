package core.window;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public abstract class GamePanel extends JPanel {
    public final static int TILESIZE = 32;
    public final static int maxScreenRow = 48 /2;
    public final static int maxScreenCol = 64 /2;
    public final static int screenWidth = TILESIZE * maxScreenCol; // 1024 pixels
    public final static int screenHeight = TILESIZE * maxScreenRow; // 768 pixels

    public GamePanel() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        if (!Game.DEBUG) {
            setBackground(Color.black);
        }
        setDoubleBuffered(true);
        setFocusable(true);
    }

    public abstract void update();
}
