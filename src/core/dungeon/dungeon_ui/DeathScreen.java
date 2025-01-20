package core.dungeon.dungeon_ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;

import core.Game;
import core.dungeon.Dungeon;
import core.game_components.GameComponent;
import core.game_components.UIButton;
import core.game_components.UILayer;
import core.utilities.ImageUtilities;

public class DeathScreen extends GameComponent {
    private static Font font;
    private BufferedImage image;

    public DeathScreen(UILayer UILayer, Action restart, Action mainMenu) {
        super(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        add(new UIButton(restart, new Rectangle(getWidth() / 2 - 224, 488, 448, 64),
                ImageUtilities.getImage("UI", "RestartButton")));
        add(new UIButton(mainMenu, new Rectangle(getWidth() / 2 - 224, 568, 448, 64),
                ImageUtilities.getImage("UI", "MenuButton")));
        add(new UIButton(UILayer.createAndOpenConfirmUI(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        }), new Rectangle(getWidth() / 2 - 224, 648, 448, 64), ImageUtilities.getImage("UI", "QuitButton")));
        if (font == null) {
            try {
                font = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/big_pixel.otf")).deriveFont(32.0f);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FontFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public void build(Dungeon dungeon, MiniMap miniMap) {
        buildImage(dungeon, miniMap);
    }

    private void buildImage(Dungeon dungeon, MiniMap miniMap) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        image = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(),
                Transparency.TRANSLUCENT);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.setFont(font);
        ArrayList<String> list = dungeon.getDeathScreenDisplay();
        for (int i = 0; i < list.size(); i++) {
            g2d.drawString(list.get(i), 275, 80 + 40 * i);
        }
        g2d.setFont(font.deriveFont(60.0f));
        g2d.drawString("Score:", 275, 170 + 40 * list.size());
        g2d.setColor(Color.RED);
        g2d.setFont(font.deriveFont(70.0f));
        g2d.drawString(String.valueOf(dungeon.getScore()), 550, 170 + 40 * list.size());
    }

    @Override
    public void update() {
    }

    @Override
    public void drawComponent(Graphics2D g2d) {
        g2d.drawImage(image, 0, 0, null);
    }
}