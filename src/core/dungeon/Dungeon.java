package core.dungeon;

import java.awt.Color;
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
import core.dungeon.items.Inventory;
import core.dungeon.items.ItemFactory;
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

    private Room room;
    private Inventory inventory;
    private Player player;
    private RoomFactory roomFactory;

    private PauseMenu pauseMenu;
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

            room = roomFactory.getNextRoom(room);
            add(room, -1);
            debugScreen.updateRoom(room);
            snowParticles.setDepth(room.getDepth());
            revalidate();
            Game.setFreezeFrame(10);
        }
    };

    private final Action playerDeath = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            deathCnt = 1;
            fadeOut(3);
            player.resetKeyboardActions();
            setFadingEffectAlpha(60);
            setFadingEffectColor(Color.RED);
            room.setFreeze(true);
            Game.setFreezeFrame(20);
        }
    };

    public Dungeon(Game game, UILayer UILayer) {
        super(game, UILayer);
        inventory = new Inventory(UILayer, DiffSettings.startingInventorySize);
        player = new Player(this, nextRoom, playerDeath, inventory);
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
        dungeonUI = new DungeonUI(new HealthBar(player), new Timer(599), miniMap);
        roomFactory = new RoomFactory(player, UILayer, miniMap);
        room = roomFactory.getStartingRoom(STARTING_ROOM);
        inventory.setItemFactory(new ItemFactory(player, dungeonUI.getTimer(), roomFactory));
        // room = roomFactory.createRandomRoom(21, 34);
        miniMap.setStartingRoom(room);
        snowParticles = new SnowParticles(player);
        debugScreen = new DebugScreen(UILayer, room);

        if (!Game.DEBUG) {
            add();
        } else {
            add(room);
            add(inventory);
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
        debugScreen = new DebugScreen(UILayer, room);

        getInputMap(2).put(KeyBinds.ESC, "pause");
        getActionMap().put("pause", UILayer.openPopupUI(pauseMenu));
        getInputMap(2).put(KeyBinds.DEBUG, "debug");
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
                deathScreen.build(this, dungeonUI.getMiniMap());
                fadeIn(4);
                deathCnt = 0;
            }
        }
        super.updateComponent();
    }

    public void reset() {
        remove();
        inventory = new Inventory(UILayer, DiffSettings.startingInventorySize);
        player = new Player(this, nextRoom, playerDeath, inventory);
        MiniMap miniMap = new MiniMap();
        dungeonUI = new DungeonUI(new HealthBar(player), new Timer(599), miniMap);
        roomFactory = new RoomFactory(player, UILayer, miniMap);
        room = roomFactory.getStartingRoom(STARTING_ROOM);
        inventory.setItemFactory(new ItemFactory(player, dungeonUI.getTimer(), roomFactory));
        miniMap.setStartingRoom(room);
        snowParticles = new SnowParticles(player);
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

    // TODO/EDIT
    public int getPoints() {
        int sum = 0;
        sum += (dungeonUI.getTimer().getStartTime() - dungeonUI.getTimer().getTime());
        sum += 20 * dungeonUI.getMiniMap().getExploredRoomCnt();
        sum += 100 * dungeonUI.getMiniMap().getMaxDepth();
        sum += 40 * inventory.getOccupiedSlots();
        sum *= 10;
        sum *= player.getStats().getPointMultiplier();
        if (dungeonUI.getTimer().getTime() == 0) {
            sum *= 1.5;
        }
        return sum;
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