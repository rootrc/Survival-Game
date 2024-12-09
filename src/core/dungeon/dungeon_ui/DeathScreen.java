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
import core.dungeon.Dungeon;
import core.game_components.GameComponent;
import core.game_components.UIButton;
import core.game_components.UILayer;
import core.utilities.ImageUtilities;

public class DeathScreen extends GameComponent {
    private BufferedImage image;

    public DeathScreen(UILayer UILayer, Action restart, Action mainMenu, Action title) {
        super(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        add(new UIButton(restart, new Rectangle(getWidth() / 2 - 224, 488, 448, 64),
                ImageUtilities.getImage("UI", "RestartButton")));
        add(new UIButton(UILayer.createAndOpenConfirmUI(mainMenu),
                new Rectangle(getWidth() / 2 - 224, 568, 448, 64),
                ImageUtilities.getImage("UI", "MainMenuButton")));
        add(new UIButton(UILayer.createAndOpenConfirmUI(title),
                new Rectangle(getWidth() / 2 - 224, 648, 448, 64),
                ImageUtilities.getImage("UI", "TitleScreenButton")));
    }

    public void build(Dungeon dungeon, MiniMap miniMap) {
        buildImage(dungeon, miniMap);
    }

    private void buildImage(Dungeon dungeon, MiniMap miniMap) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        image = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(),
                Transparency.TRANSLUCENT);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.WHITE);
        g2d.drawString("Rooms Explored:" + miniMap.getExploredRoomCnt(), 100, 100);
        g2d.drawString("Max Depth:" + miniMap.getMaxDepth(), 100, 120);
        g2d.drawString("Points:" + dungeon.getPoints(), 100, 140);
    }

    @Override
    public void update() {
    }

    @Override
    public void drawComponent(Graphics2D g2d) {
        g2d.drawImage(image, 0, 0, null);
    }
}