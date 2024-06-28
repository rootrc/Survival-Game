package game.dungeon;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;

import game.Game;
import game.game_components.ConfirmUI;
import game.game_components.GameComponent;
import game.game_components.PopupUI;

public class UILayer extends GameComponent {

    public UILayer() {
        super(Game.screenWidth, Game.screenHeight);
    }

    public void update() {
    }

    public void drawComponent(Graphics2D g2d) {
    }

     public Action openPopupUI(PopupUI popupUI) {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                add(popupUI);
                popupUI.enterPanel();
            }
        };
    }

    public Action closePopupUI(PopupUI popupUI) {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                remove(popupUI);
            }
        };
    }

    public Action closeJComponent(JComponent a, JComponent b) {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                a.remove(b);
            }
        };
    }

    public Action createConfirmUI(Action action, String actionCommand) {
        return new AbstractAction() {
            private ConfirmUI confirmUI = new ConfirmUI(UILayer.this, action, actionCommand);

            public void actionPerformed(ActionEvent e) {
                if (getComponent(0) instanceof ConfirmUI) {
                    if (getComponent(0) != confirmUI) {
                        ((ConfirmUI) getComponent(0)).exitPanel();
                        add(confirmUI);
                        confirmUI.enterPanel();
                    }
                } else {
                    add(confirmUI);
                    confirmUI.enterPanel();
                }

            }
        };
    }

    public Action removeConfirmUI() {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (getComponent(0) instanceof ConfirmUI) {
                    ((ConfirmUI) getComponent(0)).exitPanel();
                }
            }
        };
    }
}