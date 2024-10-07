package core.dungeon.room.room_UI;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import core.dungeon.items.Item;
import core.game_components.GameButton;
import core.game_components.PopupUI;
import core.utilities.ActionUtilities;
import core.utilities.ImageUtilities;

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