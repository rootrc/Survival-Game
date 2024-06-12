package game.dungeon.room.room_UI;

import java.awt.Rectangle;
import javax.swing.Action;

import game.dungeon.Dungeon;
import game.game_components.PopupUI;
import game.game_components.UIButton;
import game.utilities.ActionUtilities;
import game.utilities.ImageUtilities;

public class RoomMenu extends PopupUI {

	public RoomMenu(Dungeon dungeon, Action pause, Action restart, Action changePanel) {
		super(dungeon, 640, 480, 8);
		add(new UIButton(close, "pause", new Rectangle(getWidth() / 2 - 224, 88, 448, 64),
				ImageUtilities.getImage("UI", "ResumeButton")));
		add(new UIButton(ActionUtilities.createConfirmUI(dungeon, restart, "restart"), "restart", new Rectangle(getWidth() / 2 - 224, 168, 448, 64),
				ImageUtilities.getImage("UI", "RestartButton")));
		add(new UIButton(ActionUtilities.createConfirmUI(dungeon, changePanel, "mainMenu"), "mainMenu", new Rectangle(getWidth() / 2 - 224, 248, 448, 64),
				ImageUtilities.getImage("UI", "MainMenuButton")));
		add(new UIButton(ActionUtilities.createConfirmUI(dungeon, changePanel, "title"), "title", new Rectangle(getWidth() / 2 - 224, 328, 448, 64),
				ImageUtilities.getImage("UI", "TitleScreenButton")));
		setEscapeExits(false);
	}
}
