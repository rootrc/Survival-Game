package core.game_panel;

import core.Game;
import core.dungeon.settings.KeyBinds;
import core.game_components.GamePanel;
import core.game_components.UILayer;

public class Credits extends GamePanel {
    public Credits(Game game, UILayer UILayer) {
        super(game, UILayer);
        getInputMap(2).put(KeyBinds.ESC, "exit");
        getActionMap().put("exit", UILayer.createAndOpenConfirmUI(game.changePanel("mainMenu")));
    }
}