package core.game_panel;

import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Action;

import core.Game;
import core.dungeon.settings.KeyBinds;
import core.game_components.GameComponent;
import core.game_components.GamePanel;
import core.game_components.UIButton;
import core.game_components.UILayer;
import core.utilities.Easing;
import core.utilities.ImageUtilities;

public class Rules extends GamePanel {
    RulesImage rulesImage;

    public Rules(Game game, UILayer UILayer) {
        super(game, UILayer);
        getInputMap(2).put(KeyBinds.ESC, "exit");
        getActionMap().put("exit", UILayer.createAndOpenConfirmUI(game.changePanel("mainMenu")));
        rulesImage = new RulesImage();
        add(rulesImage);
        add(new UIButton(rulesImage.next, new Rectangle(800, 672, 192, 64),
                ImageUtilities.getImage("UI", "NextButton")));
    }

    private static class RulesImage extends GameComponent {
        private static final int TOTAL_IMAGES = 3;
        private static BufferedImage image;
        private int imageIdx;
        private Easing easing;
        private int cnt;

        private final Action next = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                cnt = 60;
                int oldImageIdx = imageIdx;
                imageIdx++;
                if (imageIdx == TOTAL_IMAGES) {
                    imageIdx = 0;
                }
                easing.set(new Point(-getWidth() * oldImageIdx, 0), new Point(-getWidth() * imageIdx, 0));
            }
        };

        public RulesImage() {
            super(1000, 700);
            if (image == null) {
                GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                image = gd.getDefaultConfiguration().createCompatibleImage(getWidth() * TOTAL_IMAGES, getHeight());
                Graphics2D g2d = image.createGraphics();
                for (int i = 0; i < TOTAL_IMAGES; i++) {
                    g2d.drawImage(ImageUtilities.getImage("rules&credits", "rules" + i), getWidth() * i, 0, null);
                }
                g2d.dispose();
            }
            setLocation((Game.SCREEN_WIDTH - getWidth()) / 2, 0);
            easing = new Easing(60);
        }

        public void update() {
            if (cnt != 0) {
                cnt--;
            }
        }

        public void drawComponent(Graphics2D g2d) {
            g2d.drawImage(image, (int) easing.easeInOutQuad(60 - cnt).getX(), 0, null);
        }
    }
}