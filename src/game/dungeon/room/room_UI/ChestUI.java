package game.dungeon.room.room_UI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.text.Utilities;

import game.dungeon.Dungeon;
import game.dungeon.inventory.Item;
import game.game_components.PopupUI;
import game.utilities.ActionUtilities;
import game.utilities.ImageUtilities;

public class ChestUI extends PopupUI {
	private Action check;

	public ChestUI(Dungeon dungeon, Item item) {
		super(dungeon, 320, 256, 8, "ChestFloor");
		add(new GetItemButton(this, item, new Rectangle(getWidth() / 2 - 32, getHeight() / 2 - 32, 64, 64)));
		check = ActionUtilities.createConfirmUI(dungeon, (Action) (new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				ChestUI.super.exitPanel();
			}
		}), "areYouSure");
	}

	@Override
	public void buildImage(String tileSet) {
		super.buildImage(tileSet);
		Graphics2D g2d = getImage().createGraphics();
		g2d.setColor(new Color(165, 120, 85));
		g2d.drawImage(ImageUtilities.getImage("UI", "ChestSlot"), getWidth() / 2 - 46, getHeight() / 2 - 46, null);
		g2d.dispose();
	}

	@Override
	public void exitPanel() {
		if (getComponentCount() == 0) {
			super.exitPanel();
		} else {
			check.actionPerformed(null);
		}
	}

}
