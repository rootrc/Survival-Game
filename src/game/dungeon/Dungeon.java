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
import game.game_components.GamePanel;

public class Dungeon extends GamePanel {
    public final static int TILESIZE = 16;
    public final static int maxScreenRow = 48;
    public final static int maxScreenCol = 64;

    private Room room;
    private Inventory inventory;
    private Player player;
    private RoomFactory roomFactory;

    private UILayer UILayer;
    private PauseMenu roomMenu;

    private final Action nextRoom = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            remove(room);
            room = roomFactory.getNextRoom(room);
            add(room, -1);
            revalidate();
        }
    };

    public Dungeon(Action changePanel) {
        super(changePanel);
        UILayer = new UILayer();
        inventory = new Inventory(UILayer, 8);
        player = new Player(nextRoom, inventory);
        roomFactory = new RoomFactory(player, UILayer);
        room = roomFactory.getStartingRoom(1);
        // room = roomFactory.createRandomRoom(40, 30);
        add(room);
        add(inventory);
        roomMenu = new PauseMenu(UILayer, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                reset();
                enterFrame();
            }
        }, changePanel);
        getInputMap(2).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "pause");
        getInputMap(2).put(KeyStroke.getKeyStroke("pressed P"), "pause");
        getActionMap().put("pause", UILayer.openPopupUI(roomMenu));
        add(UILayer);
    }

    public final void reset() {
        remove(room);
        remove(inventory);
        remove(UILayer);
        inventory = new Inventory(UILayer, 8);
        player = new Player(nextRoom, inventory);
        roomFactory = new RoomFactory(player, UILayer);
        room = roomFactory.getStartingRoom(1);
        add(room);
        add(inventory);
        UILayer.removeAll();
        add(UILayer);
    }

}