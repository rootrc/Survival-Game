package game.dungeon.room.tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import game.dungeon.Dungeon;
import game.game_components.GameImageLabel;

public class TileGrid extends GameImageLabel {
    private BufferedImage image;
    private int N, M, layers;
    private int width, height;
    private Tile[][][] tileGrid;

    public TileGrid(Tile[][][] tileGrid) {
        super();
        this.tileGrid = tileGrid;
        layers = tileGrid.length;
        N = tileGrid[0].length;
        M = tileGrid[0][0].length;
        height = Dungeon.TILESIZE * N;
        width = Dungeon.TILESIZE * M;
        createImage();
        setIcon(image);
    }

    private void createImage() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        image = gd.getDefaultConfiguration().createCompatibleImage(width, height, Transparency.OPAQUE);
        Graphics2D g2d = image.createGraphics();
        g2d.setBackground(Color.darkGray);
        g2d.clearRect(0, 0, width, height);
        for (int i = 0; i < layers; i++) {
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < M; c++) {
                    if (tileGrid[i][r][c] == null) {
                        continue;
                    }
                    g2d.drawImage(tileGrid[i][r][c].getImage(), Dungeon.TILESIZE * (c), Dungeon.TILESIZE * r, null);
                }
            }
        }
        g2d.dispose();
    }

    public Tile[][][] getTileGrid() {
        return tileGrid;
    }

    public int getN() {
        return N;
    }

    public int getM() {
        return M;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
