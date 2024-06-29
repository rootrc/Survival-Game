package game.dungeon.room.room_UI;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import game.dungeon.items.Item;
import game.game_components.GameButton;
import game.game_components.PopupUI;
import game.utilities.ActionUtilities;
import game.utilities.ImageUtilities;

public class GetItemButton extends GameButton {
    public GetItemButton(PopupUI popupUI, Action flash, Item item, Rectangle rect) {
        super(null, rect);
        setAction(ActionUtilities.combineActions(item.getAquireItem(), new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                setIcon(null);
            }
        }, flash));
        setIcon(ImageUtilities.resize(item.getImageIcon(), 64, 64));
        setRolloverIcon(ImageUtilities.resize(item.getRolloverIcon(), 64, 64));
        setToolTipText(item.getToolTip());
    }

}