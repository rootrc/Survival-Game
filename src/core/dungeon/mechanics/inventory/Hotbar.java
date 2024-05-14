package core.dungeon.mechanics.inventory;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;

import core.dungeon.mechanics.inventory.items.Item;
import core.utilities.ImageUtilities;
import core.utilities.Pair;
import core.window.GameComponent;

public class Hotbar extends GameComponent {
    private BufferedImage background;
    private int size;
    private final ArrayList<Pair<Item, Integer>> items = new ArrayList<>();

    public Hotbar(int size) {
        super(48 + 50 + 42 * (size - 2), 56);
        this.size = size;
        buildImage();
    }

    private void buildImage() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        background = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(),
                Transparency.BITMASK);
        Graphics2D g2d = background.createGraphics();
        g2d.drawImage(ImageUtilities.getImage("items", "HotbarLeft"), 0, 0, null);
        for (int i = 48; i < getWidth() - 50; i += 42) {
            g2d.drawImage(ImageUtilities.getImage("items", "HotbarMiddle"), i, 0, null);
        }
        g2d.drawImage(ImageUtilities.getImage("items", "HotbarRight"), getWidth() - 50, 0, null);
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

    public void addItem(Item item, int cnt) {
        items.add(new Pair<Item, Integer>(item, cnt));
    }

    public final Action selectItem = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            System.out.println(e.getActionCommand());
        }
    };
}