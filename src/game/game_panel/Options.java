package game.game_panel;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import game.Game;
import game.game_components.GamePanel;
import game.game_components.UILayer;

public class Options extends GamePanel {

    public Options(Game game, UILayer UILayer) {
        super(game, UILayer);
        getInputMap(2).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "pause");
        getActionMap().put("pause", UILayer.createAndOpenConfirmUI(game.changePanel("mainMenu")));
    }

}
