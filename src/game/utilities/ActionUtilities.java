package game.utilities;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;

import game.dungeon.inventory.Inventory;
import game.dungeon.inventory.Item;
import game.game_components.ConfirmUI;
import game.game_components.GamePanel;
import game.game_components.PopupUI;

public class ActionUtilities {

    public static Action openPopupUI(GamePanel gamePanel, PopupUI popupUI) {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                gamePanel.add(popupUI);
                popupUI.enterPanel();
            }
        };
    }

    public static Action closePopupUI(GamePanel gamePanel, PopupUI popupUI) {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                gamePanel.remove(popupUI);
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

    public static Action createConfirmUI(GamePanel gamePanel, Action action, String actionCommand) {
        return new AbstractAction() {
            ConfirmUI confirmUI = new ConfirmUI(gamePanel, action, actionCommand);

            public void actionPerformed(ActionEvent e) {
                if (gamePanel.getComponent(0) instanceof ConfirmUI) {
                    if (gamePanel.getComponent(0) != confirmUI) {
                        ((ConfirmUI) gamePanel.getComponent(0)).exitPanel();
                        gamePanel.add(confirmUI);
                        confirmUI.enterPanel();
                    }
                } else {
                    gamePanel.add(confirmUI);
                    confirmUI.enterPanel();
                }

            }
        };
    }

    public static Action removeConfirmUI(GamePanel gamePanel) {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (gamePanel.getComponent(0) instanceof ConfirmUI) {
                    ((ConfirmUI) gamePanel.getComponent(0)).exitPanel();
                }
            }
        };
    }

    public static Action addItem(Inventory inventory, Item item) {
        return new AbstractAction() {
            boolean added = false;
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
}
