package game.dungeon.room.tile;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import game.Game;
import game.dungeon.Dungeon;
import game.dungeon.room.entity.Player;
import game.game_components.GameComponent;

public class TileGridCeiling extends GameComponent {
    private BufferedImage[] image;
    private int N, M;
    private Tile[][][] tileGrid;
    private Player player;
    private float opacity = 0;

    public TileGridCeiling(int N, int M, Tile[][][] tileGrid, Player player) {
        super(Dungeon.TILESIZE * M, Dungeon.TILESIZE * N);
        this.tileGrid = tileGrid;
        this.player = player;
        this.N = N;
        this.M = M;
        buildImage();
    }

    public void setOpacity(Player player) {
        if (player.getLayer() == 1) {
            opacity = 1;
        } else {
            opacity = 0;
        }
    }

    public void update() {
        if (player.getLayer() == 1) {
            opacity = Math.min(1, opacity + 0.1f);
        } else {
            opacity = Math.max(0, opacity - 0.1f);
        }
    }

    public void drawComponent(Graphics2D g2d) {
        if (Game.DEBUG) {
            g2d.drawImage(image[0], 0, 0, null);
            return;
        }
        if (opacity != 1) {
            g2d.drawImage(image[0], 0, 0, null);
        }
        if (opacity != 0) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            g2d.drawImage(image[1], 0, 0, null);
        }
    }

    private void buildImage() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        image = new BufferedImage[2];
        image[0] = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(), Transparency.BITMASK);
        image[1] = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(), Transparency.BITMASK);
        Graphics2D g2d = image[0].createGraphics();
        Graphics2D g2d2 = image[1].createGraphics();
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < M; c++) {
                if (tileGrid[0][r][c] != null) {
                    tileGrid[0][r][c].draw(g2d, r, c);
                }
                if (tileGrid[1][r][c] != null) {
                    tileGrid[1][r][c].draw(g2d2, r, c);
                }
            }
        }
        g2d.dispose();
        g2d2.dispose();
    }
}
