package core.window;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
    public final static int TILESIZE = 16;
    public final static int maxScreenRow = 48 * 1;
    public final static int maxScreenCol = 64 * 1;
    public final static int screenWidth = TILESIZE * maxScreenCol; // 1024 pixels
    public final static int screenHeight = TILESIZE * maxScreenRow; // 768 pixels

    public GamePanel() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.black);
        setDoubleBuffered(true);
        setFocusable(true);   
    }

    public void update() {
        
    }

    // public void paintComponent(Graphics g) {
    //     super.paintComponent(g);
    //     Graphics2D g2d = (Graphics2D) g;
    // }
}
