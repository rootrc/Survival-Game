package game.dungeon.room.room_UI;

import java.awt.Rectangle;

import javax.swing.Action;

import game.utilities.ImageUtilities;
import game.utilities.game_components.PopupUI;
import game.utilities.game_components.UIButton;

public class PauseMenu extends PopupUI {

	public PauseMenu(Action removeRoomUI, Action pause, Action restart, Action changePanel) {
		super(640, 480, removeRoomUI, 8);
		add(new UIButton(pause, "pause", new Rectangle(getWidth() / 2 - 224, 88, 448, 64),
				ImageUtilities.getImage("UI", "ResumeButton")));
		add(new UIButton(restart, "restart", new Rectangle(getWidth() / 2 - 224, 168, 448, 64),
				ImageUtilities.getImage("UI", "RestartButton")));
		add(new UIButton(changePanel, "mainMenu", new Rectangle(getWidth() / 2 - 224, 248, 448, 64),
				ImageUtilities.getImage("UI", "MainMenuButton")));
		add(new UIButton(changePanel, "title", new Rectangle(getWidth() / 2 - 224, 328, 448, 64),
				ImageUtilities.getImage("UI", "TitleScreenButton")));
		enter();
	}
}
