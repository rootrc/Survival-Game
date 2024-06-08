package core.game_panels.dungeon.room.room_UI;

import java.awt.Rectangle;

import javax.swing.Action;

import core.utilities.ImageUtilities;
import core.utilities.PopupUI;
import core.utilities.UIButton;
import core.window.GamePanel;

public class PauseMenu extends PopupUI {

	public PauseMenu(Action pause, Action restart, Action changePanel) {
		super(640, 480);
		setLocation((GamePanel.screenWidth - getWidth()) / 2, (GamePanel.screenHeight - getHeight()) / 2);
		add(new UIButton(pause, "pause", new Rectangle(getWidth() / 2 - 224, 88, 448, 64),
				ImageUtilities.getImage("UI", "ResumeButton")));
		add(new UIButton(restart, "restart", new Rectangle(getWidth() / 2 - 224, 168, 448, 64),
				ImageUtilities.getImage("UI", "RestartButton")));
		add(new UIButton(changePanel, "mainMenu", new Rectangle(getWidth() / 2 - 224, 248, 448, 64),
				ImageUtilities.getImage("UI", "MainMenuButton")));
		add(new UIButton(changePanel, "title", new Rectangle(getWidth() / 2 - 224, 328, 448, 64),
				ImageUtilities.getImage("UI", "TitleScreenButton")));
	}
}
