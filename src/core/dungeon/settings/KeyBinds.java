package core.dungeon.settings;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

public class KeyBinds {
    private static final int defaultMask = 0;

    public static final KeyStroke UP_PRESSED = KeyStroke.getKeyStroke(KeyEvent.VK_W, defaultMask, false);
    public static final KeyStroke UP_RELEASED = KeyStroke.getKeyStroke(KeyEvent.VK_W, defaultMask, true);
    public static final KeyStroke LEFT_PRESSED = KeyStroke.getKeyStroke(KeyEvent.VK_A, defaultMask, false);
    public static final KeyStroke LEFT_RELEASED = KeyStroke.getKeyStroke(KeyEvent.VK_A, defaultMask, true);
    public static final KeyStroke DOWN_PRESSED = KeyStroke.getKeyStroke(KeyEvent.VK_S, defaultMask, false);
    public static final KeyStroke DOWN_RELEASED = KeyStroke.getKeyStroke(KeyEvent.VK_S, defaultMask, true);
    public static final KeyStroke RIGHT_PRESSED = KeyStroke.getKeyStroke(KeyEvent.VK_D, defaultMask, false);
    public static final KeyStroke RIGHT_RELEASED = KeyStroke.getKeyStroke(KeyEvent.VK_D, defaultMask, true);

    public static final KeyStroke SLOW_DOWN_TOGGLE = KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT,
            InputEvent.SHIFT_DOWN_MASK, false);
    public static final KeyStroke DASH = KeyStroke.getKeyStroke(KeyEvent.VK_J, defaultMask, false);
    public static final KeyStroke INTERACT = KeyStroke.getKeyStroke(KeyEvent.VK_E, defaultMask, false);
    public static final KeyStroke[] NUMBER = new KeyStroke[10];
    static {
        for (int i = 0; i < 10; i++) {
            NUMBER[i] = KeyStroke.getKeyStroke(KeyEvent.VK_0 + i, defaultMask, false);
        }
    }

    public static final KeyStroke OPEN_UI = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, defaultMask, false);
    public static final KeyStroke ESC = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, defaultMask, false);

    public static final KeyStroke DEBUG = KeyStroke.getKeyStroke(KeyEvent.VK_F3, defaultMask, false);

    private KeyBinds() {

    }
}