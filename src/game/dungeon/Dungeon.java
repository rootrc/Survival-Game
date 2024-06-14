package game.dungeon;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import game.dungeon.inventory.Inventory;
import game.dungeon.room.Room;
import game.dungeon.room.entity.Player;
import game.dungeon.room.room_UI.RoomMenu;
import game.dungeon.room_factory.RoomFactory;
import game.game_components.GameComponent;
import game.game_components.GamePanel;

public class Dungeon extends GamePanel {
    public final static int TILESIZE = 16;
    public final static int maxScreenRow = 48;
    public final static int maxScreenCol = 64;

    private Room room;
    private Inventory inventory;
    private Player player;
    private RoomFactory roomFactory;

    private static UILayer UI;
    private RoomMenu roomMenu;

    private final Action nextRoom = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {
            remove(room);
            room = roomFactory.getNextRoom(room);
            add(room, -1);
            revalidate();
        }
    };
    private final Action pause = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {

            if (getUI(0) != roomMenu && getUI(1) != roomMenu) {
                addUI(roomMenu);
                roomMenu.enterPanel();
            } else {
                roomMenu.exitPanel();
            }
        }
    };

    private final Action restart = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            restart();
            enterFrame();
        }
    };

    public Dungeon(Action changePanel) {
        super(changePanel);
        inventory = new Inventory(8);
        player = new Player(nextRoom, inventory);
        roomFactory = new RoomFactory(player);
        room = roomFactory.getStartingRoom(1);
        add(room);
        add(inventory);
        roomMenu = new RoomMenu(pause, restart, changePanel);
        getInputMap(2).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "pause");
        getInputMap(2).put(KeyStroke.getKeyStroke("pressed P"), "pause");
        getActionMap().put("pause", pause);

        UI = new UILayer();
        add(UI);
    }

    public void restart() {
        remove(room);
        remove(inventory);
        remove(UI);
        inventory = new Inventory(8);
        player = new Player(nextRoom, inventory);
        roomFactory = new RoomFactory(player);
        room = roomFactory.getStartingRoom(1);
        add(room);
        add(inventory);
        UI.removeAll();
        add(UI);
    }

    public static GameComponent getUI(int n) {
        if (UI.getComponentCount() <= n) {
            return null;
        }
        return (GameComponent) UI.getComponent(n);
    }

    public static void addUI(GameComponent gameComponent) {
        UI.add(gameComponent);
    }

    public static void removeUI(GameComponent gameComponent) {
        UI.remove(gameComponent);
    }

}