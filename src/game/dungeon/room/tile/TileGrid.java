package game.dungeon.room.tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import game.dungeon.Dungeon;
import game.dungeon.room.entity.Player;
import game.game_components.GameComponent;

public class TileGrid extends GameComponent {
    private BufferedImage[] image;
    private int N, M, layers;
    private int width, height;
    private Tile[][][][] tileGrids;
    private Player player;

    public TileGrid(int N, int M, Tile[][][][] tileGrids, Player player) {
        super(0, 0);
        this.tileGrids = tileGrids;
        this.player = player;
        layers = tileGrids[0].length;
        this.N = N;
        this.M = M;
        height = Dungeon.TILESIZE * N;
        width = Dungeon.TILESIZE * M;
        setSize(width, height);
        buildImages();
    }

    public void update() {
    }

    public void drawComponent(Graphics2D g2d) {
        g2d.drawImage(image[player.getTileGridHeight()], 0, 0, null);
    }

    private void buildImages() {
        image = new BufferedImage[2];
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        image[0] = gd.getDefaultConfiguration().createCompatibleImage(width, height, Transparency.OPAQUE);
        Graphics2D g2d = image[0].createGraphics();
        g2d.setBackground(Color.darkGray);
        g2d.clearRect(0, 0, width, height);
        for (int i = 0; i < layers; i++) {
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < M; c++) {
                    if (tileGrids[0][i][r][c] == null) {
                        continue;
                    }
                    g2d.drawImage(tileGrids[0][i][r][c].getImage(), Dungeon.TILESIZE * (c), Dungeon.TILESIZE * r, null);
                }
            }
        }
        image[1] = gd.getDefaultConfiguration().createCompatibleImage(width, height, Transparency.OPAQUE);
        g2d = image[1].createGraphics();
        g2d.setBackground(Color.darkGray);
        g2d.clearRect(0, 0, width, height);
        for (int i = 0; i < layers; i++) {
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < M; c++) {
                    if (tileGrids[1][i][r][c] == null) {
                        continue;
                    }
                    g2d.drawImage(tileGrids[1][i][r][c].getImage(), Dungeon.TILESIZE * (c), Dungeon.TILESIZE * r, null);
                }
            }
        }
        g2d.dispose();
    }

    public Tile[][][] getTileGrid(int height) {
        if (height == 0) {
            return tileGrids[0];
        } else if (height == 1) {
            return tileGrids[1];
        }
        return null;
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
