package game.dungeon;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import game.Game;
import game.dungeon.debug.DebugScreen;
import game.dungeon.dungeon_ui.MiniMap;
import game.dungeon.dungeon_ui.Timer;
import game.dungeon.inventory.Inventory;
import game.dungeon.mechanics.SkillTree;
import game.dungeon.mechanics.particles.SnowParticles;
import game.dungeon.room.Room;
import game.dungeon.room.entity.Player;
import game.dungeon.room.room_UI.PauseMenu;
import game.dungeon.room_factory.RoomFactory;
import game.dungeon.settings.DiffSettings;
import game.dungeon.settings.KeyBinds;
import game.game_components.GamePanel;
import game.game_components.UILayer;
import game.utilities.ActionUtilities;
import game.utilities.AnimationUtilities;

public class Dungeon extends GamePanel {
    public static final int TILESIZE = 16;
    public static final int maxScreenRow = 48;
    public static final int maxScreenCol = 64;

    private static final int startingRoom = 1;

    private int depth = 0;
    private int depthMapCnt[] = new int[12];

    private Room room;
    private Inventory inventory;
    private Player player;
    private RoomFactory roomFactory;

    private PauseMenu pauseMenu;
    private SkillTree skillTree;
    private DebugScreen debugScreen;

    private SnowParticles snowParticles;
    private Timer timer;
    private MiniMap miniMap;

    private Room removalRoom;
    private int direction;
    private int cnt;

    private final Action nextRoom = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (removalRoom != null) {
                return;
            }
            
            if (Game.DEBUG) {
                remove(room);
            } else {
                removalRoom = room;
                direction = -player.getDepthMovement();
            }

            room = roomFactory.getNextRoom(room, depth, depthMapCnt);
            depth += player.getDepthMovement();

            add(room, -1);
            debugScreen.updateRoom(room);
            revalidate();
        }
    };

    public Dungeon(Game game, UILayer UILayer) {
        super(game, UILayer);
        inventory = new Inventory(UILayer, DiffSettings.startingInventorySize);
        player = new Player(nextRoom, inventory);
        miniMap = new MiniMap();

        roomFactory = new RoomFactory(player, UILayer, miniMap);
        room = roomFactory.getStartingRoom(startingRoom);
        // room = roomFactory.getStartingRoom(46);
        // room = roomFactory.createRandomRoom(21, 34);

        miniMap.setStartingRoom(room);
        timer = new Timer();
        timer.setTime(599);
        snowParticles = new SnowParticles();
        debugScreen = new DebugScreen(UILayer, room);

        add(room);
        add(snowParticles);
        add(timer);
        add(miniMap);
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
        skillTree = new SkillTree(UILayer);
        debugScreen = new DebugScreen(UILayer, room);

        getInputMap(2).put(KeyBinds.escape, "pause");
        getActionMap().put("pause", UILayer.openPopupUI(pauseMenu));
        getInputMap(2).put(KeyBinds.openSkillTree, "skills");
        getActionMap().put("skills", UILayer.openPopupUI(skillTree));
        getInputMap(2).put(KeyBinds.debug, "debug");
        getActionMap().put("debug", UILayer.openPopupUI(debugScreen));

        if (Game.DEBUG) {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    System.out.println(5 + " " + e.getY() / TILESIZE + " " + e.getX() / TILESIZE);
                }
            });
        }
    }

    @Override
    public void updateComponent() {
        if (removalRoom != null) {
            cnt++;
            removalRoom.moveY(direction * 48 * AnimationUtilities.easeInOutQuad(cnt / 60.0));
            if ((direction == -1 && removalRoom.getY() + removalRoom.getHeight() < 0)
                    || (direction == 1 && removalRoom.getY() > getHeight())) {
                remove(removalRoom);
                removalRoom.setFreeze(false);
                removalRoom = null;
                cnt = 10;
            }
        }
        super.updateComponent();
    }

    public void reset() {
        remove(room);
        remove(snowParticles);
        remove(timer);
        remove(miniMap);
        remove(inventory);

        inventory = new Inventory(UILayer, DiffSettings.startingInventorySize);
        player = new Player(nextRoom, inventory);
        miniMap = new MiniMap();
        roomFactory = new RoomFactory(player, UILayer, miniMap);
        room = roomFactory.getStartingRoom(startingRoom);
        miniMap.setStartingRoom(room);
        timer = new Timer();
        timer.setTime(599);
        snowParticles = new SnowParticles();
        debugScreen = new DebugScreen(UILayer, room);

        add(room);
        add(snowParticles);
        add(timer);
        add(miniMap);
        add(inventory);
        UILayer.removeAll();
        add(UILayer);
    }

}