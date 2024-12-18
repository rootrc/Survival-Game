package core.dungeon.room.room_UI;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Action;

import core.dungeon.items.Inventory;
import core.dungeon.items.Item;
import core.dungeon.settings.KeyBinds;
import core.game_components.GameButton;
import core.game_components.PopupUI;
import core.game_components.UILayer;
import core.utilities.ActionUtilities;
import core.utilities.ImageUtilities;

public class ChestUI extends PopupUI {
	private static final int numItems = 3;
	private static final BufferedImage chestSlot = ImageUtilities.getImage("UI", "ChestSlot");
	private static final int distanceBetweenChestSlots = 32;

	private Action check;

	public ChestUI(UILayer UILayer, Inventory inventory, Item[] items, Action flash) {
		super(UILayer, 448, 256, 30, "ChestFloor");
		GetItemButton[] getItemButtons = new GetItemButton[numItems];
		for (int i = 0; i < numItems; i++) {
			Item item = items[i];
			getItemButtons[i] = new GetItemButton(
					new AbstractAction() {
						public void actionPerformed(ActionEvent e) {
							if (!inventory.isFull()) {
								ActionUtilities.combineActions(new AbstractAction() {
									public void actionPerformed(ActionEvent e) {
										inventory.addItem(item);
										for (int j = 0; j < numItems; j++) {
											getActionMap().put(new StringBuilder("get").append(j).toString(), null);
										}
									}
								}, flash, close).actionPerformed(e);
							} else {
								flash.actionPerformed(e);
							}
						}
					},
					items[i],
					new Rectangle(
							(getWidth() - GetItemButton.SIZE) / 2
									+ (chestSlot.getWidth() + distanceBetweenChestSlots) * (i - 1),
							(getHeight() - GetItemButton.SIZE) / 2, GetItemButton.SIZE, GetItemButton.SIZE), !inventory.isFull());
			add(getItemButtons[i]);
			getInputMap(2).put(KeyBinds.NUMBER[i + 1], new StringBuilder("get").append(i).toString());
			getActionMap().put(new StringBuilder("get").append(i).toString(), getItemButtons[i].getAction());
		}
		if (!inventory.isFull()) {
			check = UILayer.createAndOpenConfirmUI((new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					ChestUI.super.exitPanel();
				}
			}));
		} else {
			check = new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					ChestUI.super.exitPanel();
				}
			};
		}
		getActionMap().put("close", check);
	}

	@Override
	public void drawComponent(Graphics2D g2d) {
		super.drawComponent(g2d);
		for (int i = 0; i < numItems; i++) {
			g2d.drawImage(chestSlot,
					(getWidth() - chestSlot.getWidth()) / 2
							+ (i - numItems / 2) * (chestSlot.getWidth() + distanceBetweenChestSlots),
					(getHeight() - chestSlot.getHeight()) / 2, null);
		}
	}

	private static class GetItemButton extends GameButton {
		private static final int SIZE = 64;

		public GetItemButton(Action action, Item item, Rectangle rect, boolean setIconNull) {
			super(null, rect);
			setAction(action);
			setAction(ActionUtilities.combineActions(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					if (setIconNull) {
						setIcon(null);
					}
				}
			}, action));
			setIcon(ImageUtilities.resize(item.getImageIcon(), SIZE, SIZE));
			setRolloverIcon(ImageUtilities.resize(item.getImageIcon(), SIZE, SIZE));
			String ogToolTip = item.getToolTip();
			setToolTipText(new StringBuilder(ogToolTip.substring(0, ogToolTip.length() - "</html>".length()))
					.append("<br><br>Click or press the corresponding number to get this item!").append("</html>")
					.toString());
		}

	}
}
