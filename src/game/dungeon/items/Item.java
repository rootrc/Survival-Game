package game.dungeon.items;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import game.dungeon.inventory.Inventory;
import game.utilities.ImageUtilities;

public class Item {
    private ImageIcon imageIcon;
    private ImageIcon rolloverIcon;
    private Action acquireItem;
    private String name;
    private String description;
    private Action useItem;

    public Item(Inventory inventory, int r, int c, String name, String description) {
        imageIcon = new ImageIcon(ImageUtilities.getImage("item_images", "itemTileSet", r, c, 32, 32));
        rolloverIcon = new ImageIcon(ImageUtilities.getImage("item_images", "itemTileSet1", r, c, 32, 32));
        this.name = name;
        this.description = description;
        acquireItem = new AbstractAction() {
            private boolean added = false;

            public void actionPerformed(ActionEvent e) {
                if (added) {
                    return;
                }
                inventory.addItem(Item.this);
                added = true;
            }
        };
    }
        
    public void enable(int idx) {
        // TODO
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    public ImageIcon getRolloverIcon() {
        return rolloverIcon;
    }

    public Action getAquireItem() {
        return acquireItem;
    }

    protected void setUseItem(Action action) {
        useItem = action;
    }

    public Action getUseItem() {
        return useItem;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getToolTip() {
        return new StringBuilder("<html>").append(name).append("<br>").append(description).append("</html>").toString();
    }
}
