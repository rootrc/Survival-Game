package game.dungeon;

import java.awt.Graphics2D;
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
import game.game_components.GameComponent;
import game.game_components.GamePanel;
import game.utilities.ActionUtilities;

public class Dungeon extends GamePanel {
    public final static int TILESIZE = 16;
    public final static int maxScreenRow = 48;
    public final static int maxScreenCol = 64;

    private Room room;
    private Inventory inventory;
    private Player player;
    private RoomFactory roomFactory;

    private static UILayer UI;
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
        inventory = new Inventory(8);
        player = new Player(nextRoom, inventory);
        roomFactory = new RoomFactory(player);
        room = roomFactory.getStartingRoom(1);
        add(room);
        add(inventory);
        roomMenu = new PauseMenu(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                restart();
                enterFrame();
            }
        }, changePanel);
        getInputMap(2).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "pause");
        getInputMap(2).put(KeyStroke.getKeyStroke("pressed P"), "pause");
        getActionMap().put("pause", ActionUtilities.openPopupUI(roomMenu));

        UI = new UILayer();
        add(UI);
    }

    public final void restart() {
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

    private class UILayer extends GameComponent {

        public UILayer() {
            super(Game.screenWidth, Game.screenHeight);
        }

        public void update() {
        }

        public void drawComponent(Graphics2D g2d) {
        }
    }
}