package game.dungeon.room.room_UI;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import game.game_components.PopupUI;

public class Note extends PopupUI {
    private int i;
    private BufferedImage[] images;

    public Note(int idx) {
        super(640, 480, 4);
        String right = new StringBuilder(idx).append("right").toString();
        String left = new StringBuilder(idx).append("left").toString();
        getInputMap(2).put(KeyStroke.getKeyStroke((new StringBuilder("pressed ").append(idx % 10)).toString()), right);
        getInputMap(2).put(KeyStroke.getKeyStroke("pressed D"), right);
        getInputMap(2).put(KeyStroke.getKeyStroke("pressed A"), left);
        getActionMap().put(right, rightImage);
        getActionMap().put(left, leftImage);
        // Test
        int size = 10;
        images = new BufferedImage[size];
    }

    @Override
    public void drawComponent(Graphics2D g2d) {
        super.drawComponent(g2d);
        g2d.drawImage(images[i], 0, 0, null);
    }

    private final Action rightImage = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (i == images.length - 1) {
                exitPanel();
            } else {
                i++;
            }
        }
    };

    private final Action leftImage = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (i == 0) {
                exitPanel();
            } else {
                i--;
            }
        }
    };
}
