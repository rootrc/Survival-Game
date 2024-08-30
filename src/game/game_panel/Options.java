package game.game_panel;

import game.Game;
import game.dungeon.settings.KeyBinds;
import game.game_components.GamePanel;
import game.game_components.UILayer;
import game.game_components.GameSlider;

public class Options extends GamePanel {

    public Options(Game game, UILayer UILayer) {
        super(game, UILayer);
        getInputMap(2).put(KeyBinds.escape, "pause");
        getActionMap().put("pause", UILayer.createAndOpenConfirmUI(game.changePanel("mainMenu")));
        add(new GameSlider());
    }

}
