package core.game_panel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import core.Game;
import core.dungeon.settings.KeyBinds;
import core.game_components.GamePanel;
import core.game_components.UILayer;
import core.utilities.ImageUtilities;

public class Credits extends GamePanel {
    private BufferedImage image;
    public Credits(Game game, UILayer UILayer) {
        super(game, UILayer);
        if (image == null) {
            image = ImageUtilities.getImage("menu_images", "credits");
        }
        getInputMap(2).put(KeyBinds.ESC, "exit");
        getActionMap().put("exit", UILayer.createAndOpenConfirmUI(game.changePanel("mainMenu")));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(image, (Game.SCREEN_WIDTH - image.getWidth()) / 2, (Game.SCREEN_HEIGHT - image.getHeight()) / 2, null);
    }
}