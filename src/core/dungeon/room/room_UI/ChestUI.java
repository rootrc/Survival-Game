package core.dungeon.room.room_UI;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import core.dungeon.inventory.Inventory;
import core.dungeon.items.Item;
import core.dungeon.settings.KeyBinds;
import core.game_components.GameButton;
import core.game_components.PopupUI;
import core.game_components.UILayer;
import core.utilities.ActionUtilities;
import core.utilities.ImageUtilities;

public class ChestUI extends PopupUI {
	private Action check;

	public ChestUI(UILayer UILayer, Inventory inventory, Item item, Action flash) {
		super(UILayer, 320, 256, 30, "ChestFloor");
		GetItemButton getItemButton = new GetItemButton(
				ActionUtilities.combineActions(new AbstractAction() {
					public void actionPerformed(ActionEvent e) {
						if (!inventory.addItem(item)) {
							// TODO
							System.out.println("Inventory full oops");
						}
					}
					
				},flash, close), item, new Rectangle(getWidth() / 2 - 32, getHeight() / 2 - 32, 64, 64));
		add(getItemButton);
		getInputMap(2).put(KeyBinds.ONE, "getAll");
		getActionMap().put("getAll", getItemButton.getAction());
		check = UILayer.createAndOpenConfirmUI((new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				ChestUI.super.exitPanel();
			}
		}));
		getActionMap().put("close", check);
	}

	@Override
	public void drawComponent(Graphics2D g2d) {
		super.drawComponent(g2d);
		g2d.drawImage(ImageUtilities.getImage("UI", "ChestSlot"), getWidth() / 2 - 46, getHeight() / 2 - 46, null);
	}

	private static class GetItemButton extends GameButton {
		public GetItemButton(Action flash, Item item, Rectangle rect) {
			super(null, rect);
			setAction(ActionUtilities.combineActions(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					setIcon(null);
				}
			}, flash));
			setIcon(ImageUtilities.resize(item.getImageIcon(), 64, 64));
			setRolloverIcon(ImageUtilities.resize(item.getImageIcon(), 64, 64));
			setToolTipText(item.getToolTip());
		}

	}
}
