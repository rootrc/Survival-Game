package game.dungeon.settings;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

public class KeyBinds {
    private static final int defaultMask = 0;

    public static KeyStroke upPressed = KeyStroke.getKeyStroke(KeyEvent.VK_W, defaultMask, false);
    public static KeyStroke upReleased = KeyStroke.getKeyStroke(KeyEvent.VK_W, defaultMask, true);
    public static KeyStroke leftPressed = KeyStroke.getKeyStroke(KeyEvent.VK_A, defaultMask, false);
    public static KeyStroke leftReleased = KeyStroke.getKeyStroke(KeyEvent.VK_A, defaultMask, true);
    public static KeyStroke downPressed = KeyStroke.getKeyStroke(KeyEvent.VK_S, defaultMask, false);
    public static KeyStroke downReleased = KeyStroke.getKeyStroke(KeyEvent.VK_S, defaultMask, true);
    public static KeyStroke rightPressed = KeyStroke.getKeyStroke(KeyEvent.VK_D, defaultMask, false);
    public static KeyStroke rightReleased = KeyStroke.getKeyStroke(KeyEvent.VK_D, defaultMask, true);

    public static KeyStroke interact = KeyStroke.getKeyStroke(KeyEvent.VK_E, defaultMask, false);
    public static KeyStroke[] useItem = new KeyStroke[10];
    static {
        for (int i = 0; i < 10; i++) {
            useItem[i] = KeyStroke.getKeyStroke(KeyEvent.VK_0 + i, defaultMask, false);
        }
    }

    public static KeyStroke takeAll = KeyStroke.getKeyStroke(KeyEvent.VK_F, defaultMask, false);
    public static KeyStroke openUI = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, defaultMask, false);
    public static KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, defaultMask, false);

    public static KeyStroke debug = KeyStroke.getKeyStroke(KeyEvent.VK_F3, defaultMask, false);

    private KeyBinds() {

    }

}
