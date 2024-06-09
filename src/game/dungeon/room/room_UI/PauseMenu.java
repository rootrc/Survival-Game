package game.dungeon.room.room_UI;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import game.utilities.ImageUtilities;
import game.utilities.game_components.PopupUI;
import game.utilities.game_components.UIButton;

public class PauseMenu extends PopupUI {
	private ConfirmUI confirmUI;
	private ConfirmUI restartConfirm;
	private ConfirmUI mainMenuConfirm;
	private ConfirmUI titleScreenConfirm;

	public PauseMenu(Action removeRoomUI, Action pause, Action restart, Action changePanel) {
		super(640, 480, removeRoomUI, 8);
		add(new UIButton(pause, "pause", new Rectangle(getWidth() / 2 - 224, 88, 448, 64),
				ImageUtilities.getImage("UI", "ResumeButton")));
		add(new UIButton(confirm, "restart", new Rectangle(getWidth() / 2 - 224, 168, 448, 64),
				ImageUtilities.getImage("UI", "RestartButton")));
		add(new UIButton(confirm, "mainMenu", new Rectangle(getWidth() / 2 - 224, 248, 448, 64),
				ImageUtilities.getImage("UI", "MainMenuButton")));
		add(new UIButton(confirm, "title", new Rectangle(getWidth() / 2 - 224, 328, 448, 64),
				ImageUtilities.getImage("UI", "TitleScreenButton")));
		restartConfirm = new ConfirmUI(removeConfirmUI, restart, "restart", getWidth(), getHeight());
		mainMenuConfirm = new ConfirmUI(removeConfirmUI, changePanel, "mainMenu", getWidth(), getHeight());
		titleScreenConfirm = new ConfirmUI(removeConfirmUI, changePanel, "title", getWidth(), getHeight());
	}

	private final Action confirm = new AbstractAction() {

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("restart")) {
				confirmUI = restartConfirm;
			} else if (e.getActionCommand().equals("mainMenu")) {
				confirmUI = mainMenuConfirm;
			} else if (e.getActionCommand().equals("title")) {
				confirmUI = titleScreenConfirm;
			}
			add(confirmUI);
			confirmUI.enter();
		}
	};

	private final Action removeConfirmUI = new AbstractAction() {

		public void actionPerformed(ActionEvent e) {
			remove(confirmUI);
			revalidate();
		}
	};
}
