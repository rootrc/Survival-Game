package core.game_panel;

import core.Game;
import core.dungeon.settings.KeyBinds;
import core.game_components.GamePanel;
import core.game_components.GameSlider;
import core.game_components.UILayer;

public class Options extends GamePanel {

    public Options(Game game, UILayer UILayer) {
        super(game, UILayer);
        getInputMap(2).put(KeyBinds.ESC, "pause");
        getActionMap().put("pause", UILayer.createAndOpenConfirmUI(game.changePanel("mainMenu")));
        add(new GameSlider());
    }

}
