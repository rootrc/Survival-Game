package game.dungeon.room.tile;

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

    public TileGridCeiling(int N, int M, Tile[][][] tileGrid, Player player) {
        super(Dungeon.TILESIZE * M, Dungeon.TILESIZE * N);
        this.tileGrid = tileGrid;
        this.player = player;
        this.N = N;
        this.M = M;
        buildImage();
    }

    public void update() {
    }

    public void drawComponent(Graphics2D g2d) {
        if (Game.DEBUG) {
            g2d.drawImage(image[0], 0, 0, null);
            return;
        }
        g2d.drawImage(image[player.getLayer()], 0, 0, null);
    }

    private void buildImage() {
        image = new BufferedImage[2];
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        image[0] = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(), Transparency.BITMASK);
        Graphics2D g2d = image[0].createGraphics();
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < M; c++) {
                if (tileGrid[0][r][c] == null) {
                    continue;
                }
                g2d.drawImage(tileGrid[0][r][c].getImage(), Dungeon.TILESIZE * (c), Dungeon.TILESIZE * r, null);
            }
        }
        image[1] = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(), Transparency.BITMASK);
        g2d = image[1].createGraphics();
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < M; c++) {
                if (tileGrid[1][r][c] == null) {
                    continue;
                }
                g2d.drawImage(tileGrid[1][r][c].getImage(), Dungeon.TILESIZE * (c), Dungeon.TILESIZE * r, null);
            }
        }
        g2d.dispose();
    }
}
