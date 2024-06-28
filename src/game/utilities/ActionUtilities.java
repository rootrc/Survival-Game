package game.utilities;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;

import game.dungeon.Dungeon;
import game.dungeon.inventory.Inventory;
import game.dungeon.items.Item;
import game.game_components.ConfirmUI;
import game.game_components.PopupUI;

public class ActionUtilities {

    public static Action addItem(Inventory inventory, Item item) {
        return new AbstractAction() {
            private boolean added = false;

            public void actionPerformed(ActionEvent e) {
                if (added) {
                    return;
                }
                inventory.addItem(item);
                added = true;
            }
        };
    }

    public static Action combineActions(Action a, Action b) {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                a.actionPerformed(e);
                b.actionPerformed(e);
            }
        };
    }

    public static Action combineActions(Action a, Action b, Action c) {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                a.actionPerformed(e);
                b.actionPerformed(e);
                c.actionPerformed(e);
            }
        };
    }
}
