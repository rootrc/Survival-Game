package core.utilities;

import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GameButton extends JButton {
    public GameButton(Action action, String actionCommand, Rectangle rect) {
        super(action);
        setBounds(rect.x, rect.y, rect.width, rect.height);
        setActionCommand(actionCommand);

        setLayout(null);
        setBorderPainted(false);
        setBorder(null);
        setFocusable(false);
        setMargin(new Insets(0, 0, 0, 0));

        setContentAreaFilled(false);

        // setIcon(new ImageIcon(ImageUtilities.getImage("items", "itemTileSet")));
        // setRolloverIcon();
        // setPressedIcon();
        // setDisabledIcon();

        // getModel().addChangeListener(new ChangeListener() {
        //     @Override
        //     public void stateChanged(ChangeEvent e) {
        //         ButtonModel model = (ButtonModel) e.getSource();
        //         if (model.isRollover()) {
        //             setIcon(getRolloverIcon());
        //         } else if (model.isPressed()) {
        //             setIcon(getPressedIcon());
        //         } else {
        //             setIcon(getIcon());
        //         }
        //     }
        // });
    }

}
