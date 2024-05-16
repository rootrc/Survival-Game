package core.dungeon.mechanics.inventory;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.KeyStroke;

import core.utilities.ImageUtilities;
import core.window.GameComponent;

public class Hotbar extends GameComponent {
    private BufferedImage background;
    // private int size;
    public ItemSlot[] inventorySlots;

    public Hotbar(int size) {
        super(48 + 50 + 42 * (size - 2), 56);
        // this.size = size;
        inventorySlots = new ItemSlot[size + 1];
        for (int i = 1; i <= size; i++) {
            inventorySlots[i] = new ItemSlot(new Rectangle(42 * i - 34, 8, 40, 40));
            add(inventorySlots[i]);
            getInputMap(2).put(KeyStroke.getKeyStroke("pressed " + String.valueOf(i % 10)),i);
        }
        buildImage();

    }

    private void buildImage() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        background = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(),
                Transparency.BITMASK);
        Graphics2D g2d = background.createGraphics();
        g2d.drawImage(ImageUtilities.getImage("item_UI", "HotbarLeft"), 0, 0, null);
        for (int i = 48; i < getWidth() - 50; i += 42) {
            g2d.drawImage(ImageUtilities.getImage("item_UI", "HotbarMiddle"), i, 0, null);
        }
        g2d.drawImage(ImageUtilities.getImage("item_UI", "HotbarRight"), getWidth() - 50, 0, null);
        g2d.dispose();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        draw(g2d);
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.drawImage(background, 0, 0, null);
        // super.draw(g2d);
    }

    // public void addItem(Item item, int cnt) {
    //     items.add(new Pair<Item, Integer>(item, cnt));
    // }
}