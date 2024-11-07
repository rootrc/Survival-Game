package core.game_components;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import core.Game;
import core.utilities.ActionUtilities;
import core.utilities.ImageUtilities;

// A layer to put UIComponents
public class UILayer extends GameComponent {
    public UILayer() {
        super(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
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

    private static class ConfirmUI extends PopupUI {
        public ConfirmUI(UILayer UIlayer, Action action) {
            super(UIlayer, 480, 256, 4);
            add(new UIButton(ActionUtilities.combineActions(action, close),
                    new Rectangle(getWidth() / 2 - 164, 152, 160, 64),
                    ImageUtilities.getImage("UI", "YesButton")));
            add(new UIButton(close, new Rectangle(getWidth() / 2 + 4, 152, 160, 64),
                    ImageUtilities.getImage("UI", "NoButton")));
        }
    
        @Override
        public void drawComponent(Graphics2D g2d) {
            super.drawComponent(g2d);
            g2d.drawImage(ImageUtilities.getImage("UI", "ReallyButton"),
                    (getWidth() - ImageUtilities.getImage("UI", "ReallyButton").getWidth()) / 2, 56, null);
        }
    }
}