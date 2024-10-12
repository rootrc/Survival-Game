package core.dungeon.dungeon_ui;

import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import core.dungeon.room.entity.Player;
import core.dungeon.room.object_utilities.SpriteSheet;
import core.game_components.GameComponent;
import core.utilities.ImageUtilities;

public class HealthBar extends GameComponent {
    private static final int lanternCnt = 5;

    private Player player;
    private SpriteSheet[] lanterns;

    public HealthBar(Player player) {
        super(214, 52);
        setLocation(32, 16);
        this.player = player;
        lanterns = new SpriteSheet[lanternCnt];
        for (int i = 0; i < lanternCnt; i++) {
            lanterns[i] = new SpriteSheet(ImageUtilities.getImage("UI", "lanternHealth"), 6, 3);
        }
    }

    public void update() {
        for (int i = 0; i < lanternCnt; i++) {
            int nextFrame = (int) (5 * Math.min(Math.max(player.getHealth() - i, 0), 1));
            int curFrame = lanterns[i].getFrame();
            if (curFrame == nextFrame) {
                continue;
            }
            if (nextFrame - curFrame > 0) {
                lanterns[i].next();
            } else {
                lanterns[i].prev();
            }
        }
    }

    public void drawComponent(Graphics2D g2d) {
        g2d.drawImage(getImage(), 0, 0, null);
    }

    private BufferedImage getImage() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        BufferedImage image = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(), Transparency.BITMASK);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        for (int i = 0; i < lanternCnt; i++) {
            g2d.drawImage(lanterns[i].getImage(), 48 * i, 0, null);
        }
        return image;
    }
}
