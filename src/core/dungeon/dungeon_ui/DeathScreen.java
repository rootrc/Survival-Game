package core.dungeon.dungeon_ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.Action;

import core.Game;
import core.game_components.PopupUI;
import core.game_components.UIButton;
import core.game_components.UILayer;
import core.utilities.ImageUtilities;

public class DeathScreen extends PopupUI {
    public DeathScreen(UILayer UILayer, Action restart, Action mainMenu, Action title) {
        super(UILayer, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, 1);
        add(new UIButton(restart, new Rectangle(getWidth() / 2 - 224, 168+256+64, 448, 64),
				ImageUtilities.getImage("UI", "RestartButton")));
		add(new UIButton(UILayer.createAndOpenConfirmUI(mainMenu), new Rectangle(getWidth() / 2 - 224, 248+256+64, 448, 64),
				ImageUtilities.getImage("UI", "MainMenuButton")));
		add(new UIButton(UILayer.createAndOpenConfirmUI(title), new Rectangle(getWidth() / 2 - 224, 328+256+64, 448, 64),
				ImageUtilities.getImage("UI", "TitleScreenButton")));
    }

    public void buildImage(MiniMap miniMap) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        BufferedImage image = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(),
                Transparency.TRANSLUCENT);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.white);
        g2d.drawString("Rooms Explored:" + miniMap.getExploredRoomCnt(), 100, 100);
        g2d.drawString("Max Depth:" + miniMap.getMaxDepth(), 100, 120);
        setImage(image);
    }
}
