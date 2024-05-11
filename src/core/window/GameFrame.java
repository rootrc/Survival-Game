package core.window;

import javax.swing.JFrame;

public class GameFrame extends JFrame {
    public GameFrame(GamePanel gamePanel) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Game");

        add(gamePanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
