package core.game_panels.dungeon.mechanics.inventory.items;

import java.awt.event.ActionEvent;
import java.awt.Graphics2D;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import core.utilities.Drawable;
import core.utilities.ImageUtilities;

public class Item implements Drawable {
    private ImageIcon imageIcon;
    private ImageIcon rolloverIcon;
    private String name;
    private String description;

    public Item(int r, int c, String name, String description) {
        imageIcon = new ImageIcon(ImageUtilities.getImage("item_images", "itemTileSet", r, c, 2));
        rolloverIcon = new ImageIcon(ImageUtilities.getImage("item_images", "itemTileSet1", r, c, 2));
        this.name = name;
        this.description = description;
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

    public void draw(Graphics2D g2d) {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
