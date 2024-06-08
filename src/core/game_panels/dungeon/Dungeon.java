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
        final Timer timer = new Timer(1000 / 60, null);

        public void actionPerformed(ActionEvent e) {

            final ActionListener enter = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    pauseMenu.setLocation(pauseMenu.getX() + 104, pauseMenu.getY());
                    if (pauseMenu.getX() == (GamePanel.screenWidth - pauseMenu.getWidth()) / 2) {
                        timer.stop();
                        timer.removeActionListener(this);
                    }
                }
            };

            final ActionListener exit = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    pauseMenu.setLocation(pauseMenu.getX() + 104, pauseMenu.getY());
                    if (pauseMenu.getX() == GamePanel.screenWidth) {
                        Dungeon.this.remove(pauseMenu);
                        timer.stop();
                        timer.removeActionListener(this);
                    }
                }
            };
            if (getComponents()[0] != pauseMenu) {
                 add(pauseMenu, 0);
                pauseMenu.setLocation(-pauseMenu.getWidth(), pauseMenu.getY());
                timer.addActionListener(enter);
                timer.start();
                room.pause();
                inventory.pause();
            } else {
                if (timer.isRunning()) {
                    return;
                }
                timer.addActionListener(exit);
                timer.start();
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

    public void update() {
        room.update();
        inventory.update();
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

}