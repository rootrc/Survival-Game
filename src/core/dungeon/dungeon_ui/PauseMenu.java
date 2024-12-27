package core.dungeon.dungeon_ui;

import java.awt.Rectangle;
import javax.swing.Action;

import core.game_components.PopupUI;
import core.game_components.UIButton;
import core.game_components.UILayer;
import core.utilities.ImageUtilities;

public class PauseMenu extends PopupUI {

	public PauseMenu(UILayer UILayer, Action restart, Action mainMenu, Action title) {
		super(UILayer, 576, 480, 8);
		add(new UIButton(close, new Rectangle(getWidth() / 2 - 192, 88, 384, 64),
				ImageUtilities.getImage("UI", "ResumeButton")));
		add(new UIButton(UILayer.createAndOpenConfirmUI(restart), new Rectangle(getWidth() / 2 - 192, 168, 384, 64),
				ImageUtilities.getImage("UI", "RestartButton")));
		add(new UIButton(UILayer.createAndOpenConfirmUI(mainMenu), new Rectangle(getWidth() / 2 - 192, 248, 384, 64),
				ImageUtilities.getImage("UI", "MenuButton")));
		add(new UIButton(UILayer.createAndOpenConfirmUI(title), new Rectangle(getWidth() / 2 - 192, 328, 384, 64),
				ImageUtilities.getImage("UI", "TitleButton")));
	}
}
