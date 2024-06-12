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
import game.game_components.GamePanel;

public class Dungeon extends GamePanel {
    private Room room;
    private Inventory inventory;
    private Player player;
    private RoomFactory roomFactory;

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

            if (getComponent(0) != roomMenu && getComponent(1) != roomMenu) {
                add(roomMenu);
                roomMenu.enterPanel();
            } else {
                roomMenu.exitPanel();
            }
        }
    };

    private final Action restart = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            restart();
        }
    };

    public Dungeon(Action changePanel) {
        super(changePanel);
        inventory = new Inventory(this, 8);
        player = new Player(nextRoom, inventory);
        roomFactory = new RoomFactory(player);
        room = roomFactory.getStartingRoom(1);
        add(room);
        add(inventory);
        roomMenu = new RoomMenu(this, pause, restart, changePanel);
        getInputMap(2).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "pause");
        getInputMap(2).put(KeyStroke.getKeyStroke("pressed P"), "pause");
        getActionMap().put("pause", pause);
    }

    public void restart() {
        removeAll();
        inventory = new Inventory(this, 8);
        player = new Player(nextRoom, inventory);
        roomFactory = new RoomFactory(player);
        room = roomFactory.getStartingRoom(1);
        add(room);
        add(inventory);
    }

}