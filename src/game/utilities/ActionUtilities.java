package game.utilities;

import java.awt.Component;
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

    public static Action openPopupUI(PopupUI popupUI) {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                Dungeon.addUI(popupUI);
                popupUI.enterPanel();
            }
        };
    }

    public static Action closePopupUI(PopupUI popupUI) {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                Dungeon.removeUI(popupUI);
            }
        };
    }

    public static Action closeJComponent(JComponent a, JComponent b) {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                a.remove(b);
            }
        };
    }

    public static Action createConfirmUI(Action action, String actionCommand) {
        return new AbstractAction() {
            private ConfirmUI confirmUI = new ConfirmUI(action, actionCommand);

            public void actionPerformed(ActionEvent e) {
                if (Dungeon.getUI(0) instanceof ConfirmUI) {
                    if (Dungeon.getUI(0) != confirmUI) {
                        ((ConfirmUI) Dungeon.getUI(0)).exitPanel();
                        Dungeon.addUI(confirmUI);
                        confirmUI.enterPanel();
                    }
                } else {
                    Dungeon.addUI(confirmUI);
                    confirmUI.enterPanel();
                }

            }
        };
    }

    public static Action removeConfirmUI() {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (Dungeon.getUI(0) instanceof ConfirmUI) {
                    ((ConfirmUI) Dungeon.getUI(0)).exitPanel();
                }
            }
        };
    }

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
