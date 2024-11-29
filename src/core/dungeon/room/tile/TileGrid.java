package core.dungeon.room.tile;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import core.Game;
import core.dungeon.Dungeon;
import core.dungeon.mechanics.HeightHandler;
import core.dungeon.mechanics.PathFinder;
import core.dungeon.mechanics.collision.CollisionChecker;
import core.dungeon.room.entity.Player;
import core.game_components.GameComponent;
import core.utilities.ImageUtilities;

public class TileGrid extends GameComponent {
    private int N, M;
    private TileGridFloor tileGridFloor;
    private TileGridCeiling tileGridCeiling;
    private CollisionChecker collisionChecker;
    private HeightHandler heightHandler;
    private PathFinder pathFinder;

    public TileGrid(Tile[][][][] tileGridArray, Player player, CollisionChecker collisionChecker,
            HeightHandler heightHandler) {
        super(Tile.SIZE * tileGridArray[0][0][0].length, Tile.SIZE * tileGridArray[0][0].length);
        N = tileGridArray[0][0].length;
        M = tileGridArray[0][0][0].length;
        this.collisionChecker = collisionChecker;
        this.heightHandler = heightHandler;

        tileGridFloor = new TileGridFloor(tileGridArray[0]);
        tileGridCeiling = new TileGridCeiling(tileGridArray[1], player, heightHandler);
        pathFinder = new PathFinder(collisionChecker.getCollisionArray());
    }

    public void update() {
    }

    public void drawComponent(Graphics2D g2d) {
    }

    public int getN() {
        return N;
    }

    public int getM() {
        return M;
    }

    public void setPlayer(Player player) {
        tileGridCeiling.setOpacity(player);
    }

    public TileGridFloor getTileGridFloor() {
        return tileGridFloor;
    }

    public TileGridCeiling getTileGridCeiling() {
        return tileGridCeiling;
    }

    public CollisionChecker getCollisionChecker() {
        return collisionChecker;
    }

    public HeightHandler getHeightHandler() {
        return heightHandler;
    }

    public PathFinder getPathFinder() {
        return pathFinder;
    }

    private static class TileGridCeiling extends GameComponent {
        private BufferedImage[] image;
        private int N, M;
        private Tile[][][] tileGridArray;
        private Player player;
        private float opacity = 0;
        private HeightHandler heightHandler;

        TileGridCeiling(Tile[][][] tileGridArray, Player player, HeightHandler heightHandler) {
            super(Tile.SIZE * tileGridArray[0][0].length, Tile.SIZE * tileGridArray[0].length);
            N = tileGridArray[0].length;
            M = tileGridArray[0][0].length;
            this.tileGridArray = tileGridArray;
            this.player = player;
            this.heightHandler = heightHandler;
            buildImage();
        }

        public void setOpacity(Player player) {
            if (heightHandler.getLayer(player) == HeightHandler.BOTTOM && !player.getStats().canSeeAbove()) {
                opacity = 1;
            } else {
                opacity = 0;
            }
        }

        public void update() {
            if (heightHandler.getLayer(player) == HeightHandler.BOTTOM && !player.getStats().canSeeAbove()) {
                opacity = Math.min(1, opacity + 0.1f);
            } else {
                opacity = Math.max(0, opacity - 0.1f);
            }
        }

        public void drawComponent(Graphics2D g2d) {
            if (Game.DEBUG) {
                g2d.drawImage(image[Dungeon.LAYER], 0, 0, null);
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
            image[0] = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(),
                    Transparency.BITMASK);
            image[1] = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(),
                    Transparency.BITMASK);
            Graphics2D g2d = image[0].createGraphics();
            Graphics2D g2d2 = image[1].createGraphics();
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < M; c++) {
                    if (tileGridArray[0][r][c] != null) {
                        tileGridArray[0][r][c].draw(g2d, r, c);
                    }
                    if (tileGridArray[1][r][c] != null) {
                        tileGridArray[1][r][c].draw(g2d2, r, c);
                    }
                }
            }
            g2d.dispose();
            g2d2.dispose();
        }
    }

    private static class TileGridFloor extends GameComponent {
        private BufferedImage image;
        private int N, M, layers;
        private Tile[][][] tileGridArray;

        TileGridFloor(Tile[][][] tileGridArray) {
            super(Tile.SIZE * tileGridArray[0][0].length, Tile.SIZE * tileGridArray[0].length);
            layers = tileGridArray.length;
            N = tileGridArray[0].length;
            M = tileGridArray[0][0].length;
            this.tileGridArray = tileGridArray;
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
                        if (tileGridArray[i][r][c] == null) {
                            continue;
                        }
                        tileGridArray[i][r][c].draw(g2d, r, c);
                    }
                }
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.12f));
            g2d.drawImage(ImageUtilities.getSimplexNoiseFilter(getWidth(), getHeight()), 0, 0, null);
            g2d.dispose();
        }
    }
}