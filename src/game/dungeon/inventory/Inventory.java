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

import game.Game;
import game.dungeon.items.Item;
import game.dungeon.items.ItemFactory;
import game.dungeon.room.object.TreasureChest;
import game.dungeon.room.room_UI.ChestUI;
import game.game_components.GameComponent;
import game.utilities.ActionUtilities;
import game.utilities.ImageUtilities;

public class Inventory extends GameComponent {
    private static final BufferedImage tab = ImageUtilities.getImage("item_images", "InventoryTab");
    private static final BufferedImage left = ImageUtilities.getImage("item_images", "InventoryLeft");
    private static final BufferedImage middle = ImageUtilities.getImage("item_images", "InventoryMiddle");
    private static final BufferedImage right = ImageUtilities.getImage("item_images", "InventoryRight");
    private static final int itemMargin = 8;
    private static final int flashTime = 80;

    private ItemFactory itemFactory;
    private BufferedImage image;
    private int size;
    private int occupiedSlots;
    private ItemSlot[] inventorySlots;

    private boolean newItem;

    private boolean move;
    private int timer;

    public Inventory(int size) {
        super(left.getWidth() + middle.getWidth() * (size - 2) + right.getWidth(),
                middle.getHeight() + tab.getHeight());
        this.size = size;
        itemFactory = new ItemFactory(this);
        setLocation(Game.screenWidth / 2 - getWidth() / 2, Game.screenHeight - tab.getHeight());
        inventorySlots = new ItemSlot[size + 1];
        for (int i = 1; i <= size; i++) {
            inventorySlots[i] = new ItemSlot(i,
                    new Rectangle(middle.getWidth() * i - middle.getWidth() + itemMargin, itemMargin + tab.getHeight(),
                            middle.getHeight() - 2 * itemMargin, middle.getHeight() - 2 * itemMargin));
            add(inventorySlots[i]);
        }
        occupiedSlots = 1;
        getInputMap(2).put(KeyStroke.getKeyStroke("pressed TAB"), "toggle moveUp");
        getActionMap().put("toggle moveUp", moveUp);
        buildImage();
    }

    public void update() {
        if (isMouseWithinComponent(20, 50) || move) {
            setY(Math.max(Game.screenHeight - getHeight(), getY() - 8));
        } else {
            setY(Math.min(Game.screenHeight - tab.getHeight(), getY() + 3));
        }
        timer = Math.max(timer - 1, -1);
        if (timer == 0) {
            move = false;
        }
    }

    private final Action flash = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (!move) {
                move = true;
                timer = flashTime;
            }
        }
    };

    private void buildImage() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        image = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(),
                Transparency.BITMASK);
        Graphics2D g2d = image.createGraphics();
        g2d.drawImage(tab, (getWidth() - tab.getWidth()) / 2, 0, null);
        g2d.drawImage(left, 0, tab.getHeight(), null);
        for (int i = left.getWidth(); i < getWidth() - right.getWidth(); i += middle.getWidth()) {
            g2d.drawImage(middle, i, tab.getHeight(), null);
        }
        g2d.drawImage(right, getWidth() - right.getWidth(), tab.getHeight(), null);
        g2d.dispose();
    }

    public void drawComponent(Graphics2D g2d) {
        g2d.drawImage(image, 0, 0, null);
    }

    public void addItem(Item item) {
        if (occupiedSlots > size) {
            return;
        }
        inventorySlots[occupiedSlots].setItem(item);
        occupiedSlots++;
        newItem = true;
    }

    public Item getItem(int idx) {
        return inventorySlots[idx].getItem();
    }

    public Item getItem() {
        return getItem(occupiedSlots - 1);
    }

    public boolean hasNewItem() {
        if (newItem) {
            newItem = false;
            return true;
        }
        return false;
    }

    private final Action moveUp = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            move = !move;
        }
    };

    public void openChest(TreasureChest treasureChest) {
        ActionUtilities.openPopupUI(new ChestUI(itemFactory.getItem(), flash)).actionPerformed(null);
    }
}