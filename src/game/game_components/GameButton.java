package game.game_components;

import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.Action;
import javax.swing.JButton;

public abstract class GameButton extends JButton {
    public GameButton(Action action, String actionCommand, Rectangle rect) {
        super(action);
        setActionCommand(actionCommand);
        setBounds(rect);
        setBorder(null);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusable(false);
        setLayout(null);
        setMargin(new Insets(0, 0, 0, 0));
    }

}
