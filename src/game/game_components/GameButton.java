package game.game_components;

import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.Action;
import javax.swing.JButton;

// Custom GameButton drawn at a rectangle using actions 
public abstract class GameButton extends JButton {
    public GameButton(Action action, Rectangle rect) {
        super(action);
        setBounds(rect);
        setBorder(null);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusable(false);
        setLayout(null);
        setMargin(new Insets(0, 0, 0, 0));
    }

}
