package game.dungeon.inventory;

import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import game.game_components.GameComponent;
import game.game_components.GamePanel;
import game.utilities.ImageUtilities;

public class Inventory extends GameComponent {
    private final static BufferedImage tab = ImageUtilities.getImage("item_images", "InventoryTab");
    private final static BufferedImage left = ImageUtilities.getImage("item_images", "InventoryLeft");
    private final static BufferedImage middle = ImageUtilities.getImage("item_images", "InventoryMiddle");
    private final static BufferedImage right = ImageUtilities.getImage("item_images", "InventoryRight");
    private final static int itemMargin = 8;
    private BufferedImage background;
    // private int size;
    private ItemSlot[] inventorySlots;

    private ItemFactory itemFactory;

    public Inventory(int size) {
        super(left.getWidth() + middle.getWidth() * (size - 2) + right.getWidth(),
                middle.getHeight() + tab.getHeight());
        itemFactory = new ItemFactory();
        setLocation(GamePanel.screenWidth / 2 - getWidth() / 2, GamePanel.screenHeight - tab.getHeight());
        // this.size = size;
        inventorySlots = new ItemSlot[size + 1];
        for (int i = 1; i <= size; i++) {
            inventorySlots[i] = new ItemSlot(
                    new Rectangle(middle.getWidth() * i - middle.getWidth() + itemMargin, itemMargin + tab.getHeight(),
                            middle.getHeight() - 2 * itemMargin, middle.getHeight() - 2 * itemMargin));
            add(inventorySlots[i]);
            getInputMap(2).put(
                    KeyStroke.getKeyStroke((new StringBuilder("pressed ").append(i % 10)).toString()), i);
        }
        getInputMap(2).put(KeyStroke.getKeyStroke("pressed TAB"), "pressed moveUp");
        getActionMap().put("pressed moveUp", moveUp);
        getInputMap(2).put(KeyStroke.getKeyStroke("released TAB"), "released moveUp");
        getActionMap().put("released moveUp", stopMovingUp);
        buildImage();

         // Test
         inventorySlots[1].setItem(itemFactory.getItem(5, 0));
         getActionMap().put(1, inventorySlots[1].getAction());
    }

    public void update() {

    }

    private void buildImage() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        background = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(),
                Transparency.BITMASK);
        Graphics2D g2d = background.createGraphics();
        g2d.drawImage(tab, (getWidth() - tab.getWidth()) / 2, 0, null);
        g2d.drawImage(left, 0, tab.getHeight(), null);
        for (int i = left.getWidth(); i < getWidth() - right.getWidth(); i += middle.getWidth()) {
            g2d.drawImage(middle, i, tab.getHeight(), null);
        }
        g2d.drawImage(right, getWidth() - right.getWidth(), tab.getHeight(), null);
        g2d.dispose();
    }

    private boolean move;

    public void drawComponent(Graphics2D g2d) {
        g2d.drawImage(background, 0, 0, null);
        if (isMouseWithinComponent(20, 50) || move) {
            setY(Math.max(GamePanel.screenHeight - getHeight(), getY() - 2));
        } else {
            setY(Math.min(GamePanel.screenHeight - tab.getHeight(), getY() + 1));
        }
    }

    private final Action moveUp = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            move = true;
        }
    };

    private final Action stopMovingUp = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            move = false;
        }
    };

    // public void addItem(Item item, int cnt) {
    // items.add(new Pair<Item, Integer>(item, cnt));
    // }
}