package game.game_components;

import java.awt.Rectangle;

import javax.swing.Action;

import game.utilities.ImageUtilities;

public class ConfirmUI extends PopupUI {

    public ConfirmUI(GamePanel gamePanel, Action action, String actionCommand) {
        super(gamePanel, 480, 256, 4);
        add(new UIButton(action, actionCommand, new Rectangle(getWidth() / 2 - 164, 152, 160, 64),
                ImageUtilities.getImage("UI", "YesButton")));
        add(new UIButton(close, "close", new Rectangle(getWidth() / 2 + 4, 152, 160, 64),
                ImageUtilities.getImage("UI", "NoButton")));
    }
}
