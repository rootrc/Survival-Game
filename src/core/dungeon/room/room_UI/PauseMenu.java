package core.dungeon.room.room_UI;

import java.awt.Rectangle;
import javax.swing.Action;

import core.game_components.PopupUI;
import core.game_components.UIButton;
import core.game_components.UILayer;
import core.utilities.ImageUtilities;

public class PauseMenu extends PopupUI {

	public PauseMenu(UILayer UILayer, Action restart, Action mainMenu, Action title) {
		super(UILayer, 640, 480, 8);
		add(new UIButton(close, new Rectangle(getWidth() / 2 - 224, 88, 448, 64),
				ImageUtilities.getImage("UI", "ResumeButton")));
		add(new UIButton(UILayer.createAndOpenConfirmUI(restart), new Rectangle(getWidth() / 2 - 224, 168, 448, 64),
				ImageUtilities.getImage("UI", "RestartButton")));
		add(new UIButton(UILayer.createAndOpenConfirmUI(mainMenu), new Rectangle(getWidth() / 2 - 224, 248, 448, 64),
				ImageUtilities.getImage("UI", "MainMenuButton")));
		add(new UIButton(UILayer.createAndOpenConfirmUI(title), new Rectangle(getWidth() / 2 - 224, 328, 448, 64),
				ImageUtilities.getImage("UI", "TitleScreenButton")));
	}
}
