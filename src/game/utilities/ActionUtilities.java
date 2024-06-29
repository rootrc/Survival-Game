package game.utilities;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

// Utilites for Actions
public class ActionUtilities {

    public static Action combineActions(Action a, Action b) {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                a.actionPerformed(e);
                b.actionPerformed(e);
            }
        };
    }

    public static Action combineActions(Action a, Action b, Action c) {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                a.actionPerformed(e);
                b.actionPerformed(e);
                c.actionPerformed(e);
            }
        };
    }
}
