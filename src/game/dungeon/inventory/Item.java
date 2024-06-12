package game.dungeon.inventory;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import game.utilities.ActionUtilities;
import game.utilities.ImageUtilities;

public class Item {
    private ImageIcon imageIcon;
    private ImageIcon rolloverIcon;
    private Action acquireItem;
    private String name;
    private String description;

    public Item(Inventory inventory, int r, int c, String name, String description) {
        imageIcon = new ImageIcon(ImageUtilities.getImage("item_images", "itemTileSet", r, c, 2));
        rolloverIcon = new ImageIcon(ImageUtilities.getImage("item_images", "itemTileSet1", r, c, 2));
        this.name = name;
        this.description = description;
        acquireItem = ActionUtilities.addItem(inventory, this);
    }

    public final Action useItem = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            // TEMP
            System.out.println("Item Used");
        }
    };

    public ImageIcon getImageIcon() {
        return imageIcon;
    }
    public ImageIcon getRolloverIcon() {
        return rolloverIcon;
    }

    public Action getAquireItem() {
        return acquireItem;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
