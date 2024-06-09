package game.dungeon.room.room_UI;

import java.awt.Rectangle;

import javax.swing.Action;

import game.utilities.ImageUtilities;
import game.utilities.game_components.PopupUI;
import game.utilities.game_components.UIButton;

public class ConfirmUI extends PopupUI {

    public ConfirmUI(Action removeRoomUI, Action action, String actionCommand, int ancestorWidth, int ancestorHeight) {
        super(352, 256, removeRoomUI, 4, ancestorWidth, ancestorHeight);
        add(new UIButton(action, actionCommand, new Rectangle(getWidth() / 2 - 100, 56, 200, 64),
                ImageUtilities.getImage("UI", "YesButton")));
        add(new UIButton(close, "close", new Rectangle(getWidth() / 2 - 100, 136, 200, 64),
                ImageUtilities.getImage("UI", "NoButton")));
    }
}
