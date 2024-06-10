package game.utilities;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import game.game_components.ConfirmUI;
import game.game_components.GameComponent;
import game.game_components.GamePanel;

public class ActionUtilities {

    public static Action openGameComponent(GamePanel gamePanel, GameComponent gameComponent) {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                gamePanel.add(gameComponent);
                gamePanel.revalidate();
            }
        };
    }

    public static Action closeGameComponent(GamePanel gamePanel, GameComponent gameComponent) {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                gamePanel.remove(gameComponent);
                gamePanel.revalidate();
            }
        };
    }

    public static Action createConfirmUI(GamePanel gamePanel, Action action, String actionCommand) {
        return new AbstractAction() {
            ConfirmUI confirmUI = new ConfirmUI(gamePanel, action, actionCommand);

            public void actionPerformed(ActionEvent e) {
                if (gamePanel.getComponent(0) instanceof ConfirmUI) {
                    if (gamePanel.getComponent(0) != confirmUI) {
                        ((ConfirmUI) gamePanel.getComponent(0)).exit();
                        gamePanel.add(confirmUI);
                        confirmUI.enter();
                    }
                } else {
                    gamePanel.add(confirmUI);
                    confirmUI.enter();
                }

            }
        };
    }

    public static Action removeConfirmUI(GamePanel gamePanel) {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (gamePanel.getComponent(0) instanceof ConfirmUI) {
                    ((ConfirmUI) gamePanel.getComponent(0)).exit();
                }
            }
        };
    }
}
