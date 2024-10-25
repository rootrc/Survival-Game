package core.dungeon.inventory;

import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Action;

import core.Game;
import core.dungeon.items.Item;
import core.dungeon.items.ItemFactory;
import core.dungeon.room.object.TreasureChest;
import core.dungeon.room.room_UI.ChestUI;
import core.dungeon.settings.KeyBinds;
import core.game_components.GameComponent;
import core.game_components.UILayer;
import core.utilities.ImageUtilities;

public class Inventory extends GameComponent {
    private static final BufferedImage TAB = ImageUtilities.getImage("item_images", "InventoryTab");
    private static final BufferedImage LEFT = ImageUtilities.getImage("item_images", "InventoryLeft");
    private static final BufferedImage MIDDLE = ImageUtilities.getImage("item_images", "InventoryMiddle");
    private static final BufferedImage RIGHT = ImageUtilities.getImage("item_images", "InventoryRight");
    private static final int ITEM_MARGIN = 8;
    private static final int FLASHTIME = 80;

    private UILayer UILayer;
    private ItemFactory itemFactory;
    private BufferedImage image;
    private int size;
    private int occupiedSlots;
    private ItemSlot[] inventorySlots;

    private boolean newItem;

    private boolean move;
    private int timer;

    public Inventory(UILayer UILayer, int size) {
        super(LEFT.getWidth() + MIDDLE.getWidth() * (size - 2) + RIGHT.getWidth(),
                MIDDLE.getHeight() + TAB.getHeight());
        this.UILayer = UILayer;
        this.size = size;
        itemFactory = new ItemFactory();
        setLocation(Game.SCREEN_WIDTH / 2 - getWidth() / 2, Game.SCREEN_HEIGHT - TAB.getHeight());
        inventorySlots = new ItemSlot[size + 1];
        for (int i = 1; i <= size; i++) {
            inventorySlots[i] = new ItemSlot(i,
                    new Rectangle(MIDDLE.getWidth() * i - MIDDLE.getWidth() + ITEM_MARGIN, ITEM_MARGIN + TAB.getHeight(),
                            MIDDLE.getHeight() - 2 * ITEM_MARGIN, MIDDLE.getHeight() - 2 * ITEM_MARGIN));
            add(inventorySlots[i]);
        }
        occupiedSlots = 1;
        getInputMap(2).put(KeyBinds.OPEN_UI, "toggle moveUp");
        getActionMap().put("toggle moveUp", moveUp);
        buildImage();
    }

    public void update() {
        if (getParent() == null) {
            return;
        }
        if (isMouseWithinComponent(20, 50) || move) {
            setY(Math.max(Game.SCREEN_HEIGHT - getHeight(), getY() - 8));
        } else {
            setY(Math.min(Game.SCREEN_HEIGHT - TAB.getHeight(), getY() + 3));
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
                timer = FLASHTIME;
            }
        }
    };

    public void drawComponent(Graphics2D g2d) {
        g2d.drawImage(image, 0, 0, null);
    }

    private void buildImage() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        image = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(),
                Transparency.BITMASK);
        Graphics2D g2d = image.createGraphics();
        g2d.drawImage(TAB, (getWidth() - TAB.getWidth()) / 2, 0, null);
        g2d.drawImage(LEFT, 0, TAB.getHeight(), null);
        for (int i = LEFT.getWidth(); i < getWidth() - RIGHT.getWidth(); i += MIDDLE.getWidth()) {
            g2d.drawImage(MIDDLE, i, TAB.getHeight(), null);
        }
        g2d.drawImage(RIGHT, getWidth() - RIGHT.getWidth(), TAB.getHeight(), null);
        g2d.dispose();
    }

    public boolean addItem(Item item) {
        if (occupiedSlots > size) {
            return false;
        }
        inventorySlots[occupiedSlots].setItem(item);
        occupiedSlots++;
        newItem = true;
        return true;
    }

    // public Item getItem(int idx) {
    //     return inventorySlots[idx].getItem();
    // }

    // public Item getItem() {
    //     return getItem(occupiedSlots - 1);
    // }

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
        // Temp
        Item item = itemFactory.getItem(0);
        ChestUI chestUI = new ChestUI(UILayer, this, item, flash);
        chestUI.enterPanel();
    }
}