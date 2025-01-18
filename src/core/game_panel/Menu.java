package core.game_panel;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import core.Game;
import core.dungeon.settings.KeyBinds;
import core.game_components.GamePanel;
import core.game_components.UIButton;
import core.game_components.UILayer;
import core.utilities.ImageUtilities;

public class Menu extends GamePanel {
    public Menu(Game game, UILayer UILayer) {
        super(game, UILayer);
        add(new UIButton(game.changePanel("dungeon"), new Rectangle(Game.SCREEN_WIDTH / 2 - 192, 380, 384, 96),
                ImageUtilities.getImage("UI", "StartButton")));
        add(new UIButton(game.changePanel("options"), new Rectangle(Game.SCREEN_WIDTH / 2 - 192, 500, 384, 96),
                ImageUtilities.getImage("UI", "OptionsButton")));
        add(new UIButton(game.changePanel("rules"), new Rectangle(Game.SCREEN_WIDTH / 2 - 192, 620, 384, 96),
                ImageUtilities.getImage("UI", "RulesButton")));
        getInputMap(2).put(KeyBinds.ESC, "exit");
        getActionMap().put("exit", UILayer.createAndOpenConfirmUI(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        }));
    }
}