package core.dungeon;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import core.Game;
import core.dungeon.debug.DebugScreen;
import core.dungeon.dungeon_ui.DeathScreen;
import core.dungeon.dungeon_ui.DungeonUI;
import core.dungeon.dungeon_ui.HealthBar;
import core.dungeon.dungeon_ui.MiniMap;
import core.dungeon.dungeon_ui.PauseMenu;
import core.dungeon.dungeon_ui.Timer;
import core.dungeon.inventory.Inventory;
import core.dungeon.mechanics.SkillTree;
import core.dungeon.mechanics.particles.SnowParticles;
import core.dungeon.room.Room;
import core.dungeon.room.entity.Player;
import core.dungeon.room.tile.Tile;
import core.dungeon.room_factory.RoomFactory;
import core.dungeon.settings.DiffSettings;
import core.dungeon.settings.KeyBinds;
import core.game_components.GamePanel;
import core.game_components.UILayer;
import core.utilities.ActionUtilities;
import core.utilities.Easing;

public class Dungeon extends GamePanel {
    public static final int LAYER = 0;
    public static final int SETNUM = 0;
    private static final int STARTING_ROOM = 1;

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
    private DungeonUI dungeonUI;

    private Room removalRoom;
    private Easing easing;
    private int roomTransitionCnt;

    private DeathScreen deathScreen;
    private int deathCnt;

    private final Action nextRoom = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (removalRoom != null) {
                return;
            }

            if (Game.DEBUG) {
                remove(room);
            } else {
                removalRoom = room;
                if (player.getDepthMovement() == 1) {
                    easing.set(room.getLocation(), new Point(room.getX(), -removalRoom.getHeight()));
                } else if (player.getDepthMovement() == -1) {
                    easing.set(room.getLocation(), new Point(room.getX(), getHeight()));
                }
            }

            room = roomFactory.getNextRoom(room, depth, depthMapCnt);
            depth += player.getDepthMovement();

            add(room, -1);
            debugScreen.updateRoom(room);
            revalidate();
            Game.setFreezeFrame(10);
        }
    };

    private final Action playerDeath = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            deathCnt = 1;
            fadeOut(3);
            player.resetKeyboardActions();
            room.setFreeze(true);
            Game.setFreezeFrame(20);
        }
    };

    public Dungeon(Game game, UILayer UILayer) {
        super(game, UILayer);
        inventory = new Inventory(UILayer, DiffSettings.startingInventorySize);
        player = new Player(nextRoom, playerDeath, inventory);
        easing = new Easing(60);
        deathScreen = new DeathScreen(UILayer, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                reset();
                fadeIn();
            }
        }, ActionUtilities.combineActions(game.changePanel("mainMenu"), new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        }), game.changePanel("title"));

        MiniMap miniMap = new MiniMap();
        roomFactory = new RoomFactory(player, UILayer, miniMap);
        room = roomFactory.getStartingRoom(STARTING_ROOM);
        // room = roomFactory.createRandomRoom(21, 34);
        miniMap.setStartingRoom(room);
        dungeonUI = new DungeonUI(new HealthBar(player), new Timer(599), miniMap);
        snowParticles = new SnowParticles();
        debugScreen = new DebugScreen(UILayer, room);

        if (Game.DEBUG) {
            add(room);
            add(inventory);
        } else {
            add();
        }
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

        Room.setScreenShakeDuration(10);
        Room.setScreenShakeStrength(10);

        if (Game.DEBUG) {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    System.out.println(5 + " " + e.getY() / Tile.SIZE + " " + e.getX() / Tile.SIZE);
                }
            });
        }
    }

    @Override
    public void updateComponent() {
        if (removalRoom != null) {
            roomTransitionCnt++;
            removalRoom.setLocation(easing.easeInOutQuad(roomTransitionCnt));
            if (easing.getP1().equals(removalRoom.getLocation())) {
                remove(removalRoom);
                removalRoom.setFreeze(false);
                removalRoom = null;
                roomTransitionCnt = 0;
            }
        }
        if (deathCnt != 0) {
            deathCnt++;
            if (deathCnt == 5.0 / 2 * Game.UPS) {
                remove();
                add(deathScreen, -1);
                deathScreen.buildImage(dungeonUI.getMiniMap());
                fadeIn(4);
                deathCnt = 0;
            }
        }
        super.updateComponent();
    }

    public void reset() {
        remove();
        inventory = new Inventory(UILayer, DiffSettings.startingInventorySize);
        player = new Player(nextRoom, playerDeath, inventory);
        MiniMap miniMap = new MiniMap();
        roomFactory = new RoomFactory(player, UILayer, miniMap);
        room = roomFactory.getStartingRoom(STARTING_ROOM);
        miniMap.setStartingRoom(room);
        dungeonUI = new DungeonUI(new HealthBar(player), new Timer(599), miniMap);
        snowParticles = new SnowParticles();
        debugScreen = new DebugScreen(UILayer, room);
        add();
    }

    private void add() {
        add(room);
        add(snowParticles);
        add(dungeonUI);
        add(inventory);
        add(UILayer);
    }

    private void remove() {
        remove(room);
        remove(snowParticles);
        remove(dungeonUI);
        remove(inventory);

        remove(deathScreen);
        UILayer.removeAll();
    }
}