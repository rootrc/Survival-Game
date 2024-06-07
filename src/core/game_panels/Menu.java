package core.game_panels;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import core.game_panels.dungeon.Dungeon;
import core.utilities.ImageUtilities;
import core.utilities.UIButton;
import core.window.GamePanel;

public class Menu extends GamePanel {
    public Menu(Dungeon dungeon, Action changePanel) {
        super(changePanel);
        Action exit = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
        add(new UIButton(changePanel, "dungeon", new Rectangle(GamePanel.screenWidth / 2 - 192, 380, 384, 96),
                ImageUtilities.getImage("UI", "StartButton")));
        add(new UIButton(changePanel, "options", new Rectangle(GamePanel.screenWidth / 2 - 192, 500, 384, 96),
                ImageUtilities.getImage("UI", "OptionsButton")));
        add(new UIButton(exit, "exit", new Rectangle(GamePanel.screenWidth / 2 - 192, 620, 384, 96),
                ImageUtilities.getImage("UI", "ExitButton")));
    }

    public void update() {

    }
}
