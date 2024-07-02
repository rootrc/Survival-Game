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
    private BufferedImage tileGridImage;
    private BufferedImage tileGrid1Image;
    private int N, M, layers;
    private int width, height;
    private Tile[][][] tileGrid;
    private Tile[][][] tileGrid1;
    private Player player;

    public TileGrid(Player player, Tile[][][] tileGrid, Tile[][][] tileGrid1) {
        super(0, 0);
        this.tileGrid = tileGrid;
        this.tileGrid1 = tileGrid1;
        this.player = player;
        layers = tileGrid.length;
        N = tileGrid[0].length;
        M = tileGrid[0][0].length;
        height = Dungeon.TILESIZE * N;
        width = Dungeon.TILESIZE * M;
        setSize(width, height);
        buildImage();
    }

    public void update() {
    }

    public void drawComponent(Graphics2D g2d) {
        if (player.getTileGridHeight() == 1) {
            g2d.drawImage(tileGridImage, 0, 0, null);
        } else {
            g2d.drawImage(tileGrid1Image, 0, 0, null);
        }
    }

    private void buildImage() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        tileGridImage = gd.getDefaultConfiguration().createCompatibleImage(width, height, Transparency.OPAQUE);
        Graphics2D g2d = tileGridImage.createGraphics();
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
        tileGrid1Image = gd.getDefaultConfiguration().createCompatibleImage(width, height, Transparency.OPAQUE);
        g2d = tileGrid1Image.createGraphics();
        g2d.setBackground(Color.darkGray);
        g2d.clearRect(0, 0, width, height);
        for (int i = 0; i < layers; i++) {
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < M; c++) {
                    if (tileGrid1[i][r][c] == null) {
                        continue;
                    }
                    g2d.drawImage(tileGrid1[i][r][c].getImage(), Dungeon.TILESIZE * (c), Dungeon.TILESIZE * r, null);
                }
            }
        }
        g2d.dispose();
    }

    public Tile[][][] getTileGrid(int height) {
        if (height == 0) {
            return tileGrid;
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
