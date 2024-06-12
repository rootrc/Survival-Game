package game.game_components;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.Action;

import game.utilities.ActionUtilities;
import game.utilities.ImageUtilities;

public class ConfirmUI extends PopupUI {

    public ConfirmUI(GamePanel gamePanel, Action action, String actionCommand) {
        super(gamePanel, 480, 256, 4);
        add(new UIButton(ActionUtilities.combineActions(action, close), actionCommand, new Rectangle(getWidth() / 2 - 164, 152, 160, 64),
                ImageUtilities.getImage("UI", "YesButton")));
        add(new UIButton(close, "close", new Rectangle(getWidth() / 2 + 4, 152, 160, 64),
                ImageUtilities.getImage("UI", "NoButton")));
    }

    @Override
    public void drawComponent(Graphics2D g2d) {
        super.drawComponent(g2d);
        g2d.drawImage(ImageUtilities.getImage("UI", "ReallyButton"),
                (getWidth() - ImageUtilities.getImage("UI", "ReallyButton").getWidth()) / 2, 56, null);
    }
}
