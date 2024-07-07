package game.game_components;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import game.Game;

// A layer to put UIComponents
public class UILayer extends GameComponent {
    public UILayer() {
        super(Game.screenWidth, Game.screenHeight);
    }

    public void update() {
    }

    public void drawComponent(Graphics2D g2d) {
    }

    public void remove(GameComponent gameComponent) {
        super.remove(gameComponent);
        if (getComponentCount() == 0) {
            return;
        }
        if (getComponent(0) instanceof ConfirmUI) {
            ((ConfirmUI) getComponent(0)).exitPanel();
        }
    }

    public Action openPopupUI(PopupUI popupUI) {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                popupUI.enterPanel();
            }
        };
    }

    public Action createAndOpenConfirmUI(Action action) {
        return new AbstractAction() {
            private ConfirmUI confirmUI = new ConfirmUI(UILayer.this, action);

            public void actionPerformed(ActionEvent e) {
                if (getComponentCount() == 0) {
                    confirmUI.enterPanel();
                    return;
                }
                if (getComponent(0) instanceof ConfirmUI) {
                    if (getComponent(0) != confirmUI) {
                        ((ConfirmUI) getComponent(0)).exitPanel();
                        confirmUI.enterPanel();
                    }
                } else {
                    confirmUI.enterPanel();
                }
            }
        };
    }
}