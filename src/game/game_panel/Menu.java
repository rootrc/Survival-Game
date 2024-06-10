package game.game_panel;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import game.game_components.GamePanel;
import game.game_components.UIButton;
import game.utilities.ActionUtilities;
import game.utilities.ImageUtilities;

public class Menu extends GamePanel {
    private final Action exit = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    };

    public Menu(Action changePanel) {
        super(changePanel);
        add(new UIButton(changePanel, "dungeon", new Rectangle(GamePanel.screenWidth / 2 - 192, 380, 384, 96),
                ImageUtilities.getImage("UI", "StartButton")));
        add(new UIButton(changePanel, "options", new Rectangle(GamePanel.screenWidth / 2 - 192, 500, 384, 96),
                ImageUtilities.getImage("UI", "OptionsButton")));
        add(new UIButton(ActionUtilities.createConfirmUI(this, exit, "exit"), "exit", new Rectangle(GamePanel.screenWidth / 2 - 192, 620, 384, 96),
                ImageUtilities.getImage("UI", "ExitButton")));
    }
}
