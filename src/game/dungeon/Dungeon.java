package game.dungeon;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import game.dungeon.inventory.Inventory;
import game.dungeon.room.Room;
import game.dungeon.room.entity.Player;
import game.dungeon.room.room_UI.PauseMenu;
import game.dungeon.room_factory.RoomFactory;
import game.utilities.game_components.GamePanel;

public class Dungeon extends GamePanel {
    private Room room;
    private Inventory inventory;
    private Player player;
    private RoomFactory roomFactory;

    private PauseMenu pauseMenu;

    private final Action removeRoomUI = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("PauseMenu")) {
                remove(pauseMenu);
            }
            revalidate();
        }
    };

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

            if (getComponents()[0] != pauseMenu) {
                add(pauseMenu);
                pauseMenu.enter();
                room.pause();
                inventory.pause();
            } else {
                pauseMenu.exit();
                if (pauseMenu.getComponentCount() == 5) {
                    pauseMenu.remove(pauseMenu.getComponent(0));
                }
                room.unpause();
                inventory.unpause();
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
        inventory = new Inventory(8);
        player = new Player(nextRoom, inventory);
        roomFactory = new RoomFactory(player);
        room = roomFactory.getStartingRoom(1);
        add(room);
        add(inventory);
        pauseMenu = new PauseMenu(removeRoomUI, pause, restart, changePanel);
        getInputMap(2).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "pause");
        getInputMap(2).put(KeyStroke.getKeyStroke("pressed P"), "pause");
        getActionMap().put("pause", pause);
    }

    public void restart() {
        removeAll();
        inventory = new Inventory(8);
        room = roomFactory.getStartingRoom(1);
        if (pauseMenu.getComponentCount() == 5) {
            pauseMenu.remove(pauseMenu.getComponent(0));
        }
        add(room);
        add(inventory);
    }

}