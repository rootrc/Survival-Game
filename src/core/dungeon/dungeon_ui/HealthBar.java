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
    private static final int LANTERN_CNT = 8;
    private static final int LANTERN_DIST = 32;

    private Player player;
    private int obtainedLanterns;
    private SpriteSheet[] lanterns;
    private int cnt;

    public HealthBar(Player player, int obtainedLanterns) {
        super(LANTERN_DIST * LANTERN_CNT, 52);
        this.player = player;
        this.obtainedLanterns = obtainedLanterns;
        setLocation(32, 24);
        lanterns = new SpriteSheet[LANTERN_CNT];
        for (int i = 0; i < LANTERN_CNT; i++) {
            lanterns[i] = new SpriteSheet(ImageUtilities.getImage("UI", "lanternHealth"), 4, 7, 60);
        }
        for (int i = 0; i < obtainedLanterns; i++) {
            lanterns[i].setType(1);
        }
    }

    public void update() {
        cnt++;
        for (int i = 0; i < obtainedLanterns; i++) {
            int nextType = (int) (5 * Math.min(Math.max(player.getHealth() - i, 0), 1)) + 1;
            int curType = lanterns[i].getType();
            if (curType != nextType && cnt >= 4) {
                lanterns[i].setType(lanterns[i].getType() + (int) Math.signum(nextType - curType));
                cnt = 0;
            }
        }
        for (int i = 0; i < LANTERN_CNT; i++) {
            lanterns[i].next();
        }
    }

    public void drawComponent(Graphics2D g2d) {
        g2d.drawImage(getImage(), 0, 0, null);
    }

    private BufferedImage getImage() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        BufferedImage image = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(),
                Transparency.BITMASK);
        Graphics2D g2d = image.createGraphics();
        for (int i = 0; i < LANTERN_CNT; i++) {
            g2d.drawImage(lanterns[i].getImage(), LANTERN_DIST * i, 0, null);
        }
        return image;
    }

    public void addLantern() {
        if (obtainedLanterns == LANTERN_CNT) {
            return;
        }
        obtainedLanterns++;
        lanterns[obtainedLanterns - 1].setType(1);
    }
}
