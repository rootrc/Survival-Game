package game.dungeon.room.room_UI;

import java.awt.Rectangle;

import game.dungeon.inventory.Item;
import game.game_components.GameButton;
import game.game_components.PopupUI;
import game.utilities.ActionUtilities;
import game.utilities.ImageUtilities;

public class GetItemButton extends GameButton {

    public GetItemButton(PopupUI popupUI, Item item, Rectangle rect) {
        super(null, "getItem", rect);
        setAction(ActionUtilities.combineActions(item.getAquireItem(), ActionUtilities.closeJComponent(popupUI, this)));
		setIcon(ImageUtilities.resize(item.getImageIcon(), 64, 64));
		setRolloverIcon(ImageUtilities.resize(item.getRolloverIcon(), 64, 64));
    }

}