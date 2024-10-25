package core.dungeon.items;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.ImageIcon;

public class Item {
    private ImageIcon imageIcon;
    // private ImageIcon rolloverIcon;
    private Action action;
    private String name;
    private String description;
    private ArrayList<Integer> incompatible;

    public Item(BufferedImage image, String name, String description, ArrayList<Integer> incompatible) {
        this.imageIcon = new ImageIcon(image);
        this.name = name;
        this.description = description;
        this.incompatible = incompatible;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }
        
    public Action getAction() {
        return action;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    
    public ArrayList<Integer> getIncompatible() {
        return incompatible;
    }

    public String getToolTip() {
        return new StringBuilder("<html>").append(name).append("<br>").append(description).append("</html>").toString();
    }
}
