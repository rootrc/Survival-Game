package game.utilities.game_components;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class GameImageLabel extends JLabel {
    public GameImageLabel(BufferedImage image) {
        super(new ImageIcon(image));
        setLayout(null);
        setBorder(null);
        setFocusable(false);
    }

    public GameImageLabel() {
        super();
        setLayout(null);
        setBorder(null);
        setFocusable(false);
    }

    protected void setIcon(BufferedImage image) {
        setIcon(new ImageIcon(image));
    }
}
