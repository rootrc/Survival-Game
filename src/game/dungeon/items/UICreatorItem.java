package game.dungeon.items;


import game.dungeon.inventory.Inventory;
import game.dungeon.room.room_UI.Note;
import game.utilities.ActionUtilities;

public class UICreatorItem extends Item {

    public UICreatorItem(Inventory inventory, int r, int c, String name, String description) {
        super(inventory, r, c, name, description);
    }

    public void enable(int idx) {
        setUseItem(ActionUtilities.openPopupUI(new Note(idx)));
    }
}
