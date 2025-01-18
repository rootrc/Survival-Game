package core.game_panel;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import core.Game;
import core.dungeon.settings.KeyBinds;
import core.game_components.GameComponent;
import core.game_components.GamePanel;
import core.game_components.UILayer;
import core.utilities.ImageUtilities;

public class Credits extends GamePanel {
    public Credits(Game game, UILayer UILayer) {
        super(game, UILayer);
        getInputMap(2).put(KeyBinds.ESC, "exit");
        getActionMap().put("exit", UILayer.createAndOpenConfirmUI(game.changePanel("mainMenu")));
        add(new CreditsImage());
    }

    private static class CreditsImage extends GameComponent {
        private static BufferedImage image;

        public CreditsImage() {
            super(1000, 640);
            if (image == null) {
                image = ImageUtilities.getImage("rules&credits", "credits");
            }
            setLocation((Game.SCREEN_WIDTH - getWidth()) / 2, (Game.SCREEN_HEIGHT - getHeight()) / 2);
        }

        public void update() {
        }

        public void drawComponent(Graphics2D g2d) {
            g2d.drawImage(image, 0, 0, null);
        }
    }
}