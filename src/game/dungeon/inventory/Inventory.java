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

import game.dungeon.Dungeon;
import game.dungeon.room.object.TreasureChest;
import game.dungeon.room.room_UI.ChestUI;
import game.game_components.GameComponent;
import game.game_components.GamePanel;
import game.utilities.ActionUtilities;
import game.utilities.ImageUtilities;

public class Inventory extends GameComponent {
    private final static BufferedImage tab = ImageUtilities.getImage("item_images", "InventoryTab");
    private final static BufferedImage left = ImageUtilities.getImage("item_images", "InventoryLeft");
    private final static BufferedImage middle = ImageUtilities.getImage("item_images", "InventoryMiddle");
    private final static BufferedImage right = ImageUtilities.getImage("item_images", "InventoryRight");
    private final static int itemMargin = 8;
    private BufferedImage image;

    private int size;
    private int occupiedSlots = 1;
    private ItemSlot[] inventorySlots;

    private ItemFactory itemFactory;
    private Action openChest;

    public Inventory(Dungeon dungeon, int size) {
        super(left.getWidth() + middle.getWidth() * (size - 2) + right.getWidth(),
                middle.getHeight() + tab.getHeight());
        itemFactory = new ItemFactory(this);
        setLocation(GamePanel.screenWidth / 2 - getWidth() / 2, GamePanel.screenHeight - tab.getHeight());
        this.size = size;
        inventorySlots = new ItemSlot[size + 1];
        for (int i = 1; i <= size; i++) {
            inventorySlots[i] = new ItemSlot(
                    new Rectangle(middle.getWidth() * i - middle.getWidth() + itemMargin, itemMargin + tab.getHeight(),
                            middle.getHeight() - 2 * itemMargin, middle.getHeight() - 2 * itemMargin));
            add(inventorySlots[i]);
            getInputMap(2).put(
                    KeyStroke.getKeyStroke((new StringBuilder("pressed ").append(i % 10)).toString()), i);
        }
        getInputMap(2).put(KeyStroke.getKeyStroke("pressed TAB"), "toggle moveUp");
        getActionMap().put("toggle moveUp", moveUp);
        buildImage();

        openChest = ActionUtilities.openPopupUI(dungeon, new ChestUI(dungeon, itemFactory.getItem(5, 0), flash));
    }

    private boolean move;
    private int timer;

    public void update() {
        if (isMouseWithinComponent(20, 50) || move) {
            setY(Math.max(GamePanel.screenHeight - getHeight(), getY() - 8));
        } else {
            setY(Math.min(GamePanel.screenHeight - tab.getHeight(), getY() + 3));
        }
        timer = Math.max(timer - 1, -1);
        if (timer == 0) {
            move = false;
        }
    }

    private final static int flashTime = 80;

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
        if (occupiedSlots == size) {
            // TODO
            throw new UnsupportedOperationException("Feature Incomplete");
        }
        inventorySlots[occupiedSlots].setItem(item);
        getActionMap().put(occupiedSlots, inventorySlots[occupiedSlots].getAction());
        occupiedSlots++;
    }

    private final Action moveUp = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            move = !move;
        }
    };

    public void openChest(TreasureChest treasureChest) {
        openChest.actionPerformed(null);
    }
}