package core.dungeon.items;

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
import core.dungeon.room.object.TreasureChest;
import core.dungeon.room.room_UI.ChestUI;
import core.dungeon.settings.DiffSettings;
import core.dungeon.settings.KeyBinds;
import core.game_components.GameButton;
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

    // private boolean newItem;

    private boolean move;
    private int timer;

    private final Action moveUp = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            move = !move;
        }
    };

    public Inventory(UILayer UILayer) {
        super(LEFT.getWidth() + MIDDLE.getWidth() * (DiffSettings.startingInventorySize - 2) + RIGHT.getWidth(),
                MIDDLE.getHeight() + TAB.getHeight());
        this.UILayer = UILayer;
        size = DiffSettings.startingInventorySize;
        setLocation(Game.SCREEN_WIDTH / 2 - getWidth() / 2, Game.SCREEN_HEIGHT - TAB.getHeight());
        inventorySlots = new ItemSlot[size + 1];
        for (int i = 1; i <= size; i++) {
            inventorySlots[i] = new ItemSlot(i,
                    new Rectangle(MIDDLE.getWidth() * (i - 1) + ITEM_MARGIN, ITEM_MARGIN + TAB.getHeight(),
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

    public void setItemFactory(ItemFactory itemFactory) {
        this.itemFactory = itemFactory;
    }

    public boolean addItem(Item item) {
        if (occupiedSlots > size) {
            return false;
        }
        inventorySlots[occupiedSlots].setItem(item);
        occupiedSlots++;
        itemFactory.removeItem(item);
        return true;
    }

    public void removeItem(int idx) {
        inventorySlots[idx].moveByIndex(occupiedSlots - idx - 1);
        for (int i = idx + 1; i < occupiedSlots; i++) {
            inventorySlots[i].moveByIndex(-1);
        }
        occupiedSlots--;
    }

    public void openChest(TreasureChest treasureChest) {
        ChestUI chestUI = new ChestUI(UILayer, this, itemFactory.getThreeItems(), new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!move) {
                    move = true;
                    timer = FLASHTIME;
                }
            }
        });
        chestUI.enterPanel();
    }

    public int getOccupiedSlots() {
        return occupiedSlots;
    }

    private class ItemSlot extends GameButton {
        private int idx;
        private Item item;

        private final Action removeItem = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                UILayer.createAndOpenConfirmUI(new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                        removeItem();
                    }
                }).actionPerformed(e);
            }
        };

        public ItemSlot(int idx, Rectangle rect) {
            super(null, rect);
            this.idx = idx;
        }

        public void setItem(Item item) {
            this.item = item;
            item.getItemAction().doEffect();
            setAction(removeItem);
            setIcon(ImageUtilities.resize(item.getImageIcon(), 32, 32));
            setRolloverIcon(ImageUtilities.resize(item.getImageIcon(), 32, 32));
            setToolTipText(item.getToolTip());
        }

        public void removeItem() {
            item.getItemAction().doReversedEffect();
            item = null;
            setAction(null);
            setIcon(null);
            setRolloverIcon(null);
            Inventory.this.removeItem(idx);
        }

        public void moveByIndex(int x) {
            setLocation(getX() + x * MIDDLE.getWidth(), getY());
            idx += x;
        }
    }
}