package core.game_panel;

import java.awt.Rectangle;

import core.Game;
import core.dungeon.settings.KeyBinds;
import core.game_components.GamePanel;
import core.game_components.GameSlider;
import core.game_components.UIButton;
import core.game_components.UILayer;
import core.utilities.ImageUtilities;

public class Options extends GamePanel {

    public Options(Game game, UILayer UILayer) {
        super(game, UILayer);
        getInputMap(2).put(KeyBinds.ESC, "exit");
        getActionMap().put("exit", UILayer.createAndOpenConfirmUI(game.changePanel("mainMenu")));
        add(new GameSlider(new String[]{"EASY", "MEDIUM", "HARD"}, new Rectangle(100, 100, 300, 50)));
        add(new UIButton(game.changePanel("credits"), new Rectangle(704, 672, 288, 64),
				ImageUtilities.getImage("UI", "CreditsButton")));
    }
}