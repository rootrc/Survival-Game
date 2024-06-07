package core.game_panels.dungeon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import core.game_panels.dungeon.mechanics.inventory.InventoryManager;
import core.game_panels.dungeon.room.room_UI.PauseMenu;
import core.game_panels.dungeon.room.room_utilities.RoomManager;
import core.game_panels.dungeon.room_connection.DungeonData;
import core.window.GamePanel;

public class Dungeon extends GamePanel {
    private RoomManager room;
    private DungeonData dungeonData;
    private InventoryManager inventory;

    private PauseMenu pauseMenu;

    private final Action nextRoom = new AbstractAction() {
        private boolean movingRoom;

        public void actionPerformed(ActionEvent e) {
            if (!movingRoom) {
                movingRoom = true;
                remove(room.get());
                room.nextRoom();
                add(room.get());
                revalidate();
                Timer timer = new Timer(500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        movingRoom = false;
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
    };
    private final Action pause = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (getComponents()[0] == pauseMenu) {
                remove(pauseMenu);
                room.unpause();
            } else {
                add(pauseMenu, 0);
                room.pause();
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
        dungeonData = new DungeonData();
        inventory = new InventoryManager();
        room = new RoomManager(dungeonData, inventory, nextRoom);
        add(inventory.get());
        add(room.get());
        pauseMenu = new PauseMenu(pause, restart, changePanel);
        getInputMap(2).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "pause");
        getInputMap(2).put(KeyStroke.getKeyStroke("pressed P"), "pause");
        getActionMap().put("pause", pause);
    }

    public void restart() {
        remove(inventory.get());
        remove(room.get());
        dungeonData = new DungeonData();
        inventory = new InventoryManager();
        room = new RoomManager(dungeonData, inventory, nextRoom);
        add(inventory.get());
        add(room.get());
        pause.actionPerformed(null);
    }

    public void update() {
        room.update();
        inventory.update();
    }
}