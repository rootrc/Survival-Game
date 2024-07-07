package game.dungeon;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import game.Game;
import game.dungeon.inventory.Inventory;
import game.dungeon.room.Room;
import game.dungeon.room.entity.Player;
import game.dungeon.room.room_UI.PauseMenu;
import game.dungeon.room_factory.RoomFactory;
import game.game_components.GamePanel;
import game.game_components.UILayer;
import game.utilities.ActionUtilities;

public class Dungeon extends GamePanel {
    public final static int TILESIZE = 16;
    public final static int maxScreenRow = 48;
    public final static int maxScreenCol = 64;

    private final static int startingRoom = 1;
    private final static int startingInventorySize = 8;

    private Room room;
    private Inventory inventory;
    private Player player;
    private RoomFactory roomFactory;

    private PauseMenu pauseMenu;

    private final Action nextRoom = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            remove(room);
            room = roomFactory.getNextRoom(room);
            add(room, -1);
            revalidate();
        }
    };

    public Dungeon(Game game, UILayer UILayer) {
        super(game, UILayer);
        inventory = new Inventory(UILayer, startingInventorySize);
        player = new Player(nextRoom, inventory);
        roomFactory = new RoomFactory(player, UILayer);
        room = roomFactory.getStartingRoom(startingRoom);
        // room = roomFactory.createRandomRoom(63, 57);
        add(room);
        add(inventory);
        pauseMenu = new PauseMenu(UILayer, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                reset();
                fadeIn();
            }
        }, ActionUtilities.combineActions(game.changePanel("mainMenu"), new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        }), game.changePanel("title"));
        getInputMap(2).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "pause");
        getInputMap(2).put(KeyStroke.getKeyStroke("pressed P"), "pause");
        getActionMap().put("pause", UILayer.openPopupUI(pauseMenu));
    }

    public final void reset() {
        remove(room);
        remove(inventory);
        remove(UILayer);
        inventory = new Inventory(UILayer, startingInventorySize);
        player = new Player(nextRoom, inventory);
        roomFactory = new RoomFactory(player, UILayer);
        room = roomFactory.getStartingRoom(startingRoom);
        add(room);
        add(inventory);
        UILayer.removeAll();
        add(UILayer);
    }

}