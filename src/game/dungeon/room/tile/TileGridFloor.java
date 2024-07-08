package game.dungeon.room.tile;

import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import game.dungeon.Dungeon;
import game.game_components.GameComponent;

public class TileGridFloor extends GameComponent {
    private BufferedImage image;
    private int N, M, layers;
    private Tile[][][] tileGrid;

    public TileGridFloor(int N, int M, Tile[][][] tileGrid) {
        super(Dungeon.TILESIZE * M, Dungeon.TILESIZE * N);
        this.tileGrid = tileGrid;
        layers = tileGrid.length;
        this.N = N;
        this.M = M;
        buildImage();
    }

    public void update() {
    }

    public void drawComponent(Graphics2D g2d) {
        g2d.drawImage(image, 0, 0, null);
    }

    private void buildImage() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        image = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(), Transparency.OPAQUE);
        Graphics2D g2d = image.createGraphics();
        for (int i = 0; i < layers; i++) {
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < M; c++) {
                    if (tileGrid[i][r][c] == null) {
                        continue;
                    }
                    tileGrid[i][r][c].draw(g2d, r, c);
                }
            }
        }
        g2d.dispose();
    }

    public Tile[][][] getTileGridArray() {
        return tileGrid;
    }

}
