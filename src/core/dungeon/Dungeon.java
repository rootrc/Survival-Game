package core.dungeon;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;

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
import core.dungeon.dungeon_ui.WarningDisplay;
import core.dungeon.items.Inventory;
import core.dungeon.items.ItemFactory;
import core.dungeon.mechanics.particles.SnowParticles;
import core.dungeon.room.Room;
import core.dungeon.room.entity.Player;
import core.dungeon.room_factory.RoomFactory;
import core.dungeon.settings.DiffSettings;
import core.dungeon.settings.KeyBinds;
import core.game_components.GamePanel;
import core.game_components.UILayer;
import core.utilities.Easing;

public class Dungeon extends GamePanel {
    public static final int LAYER = 0;
    public static final int SETNUM = 0;
    public static final int STARTING_ROOM = 1;

    private RoomFactory roomFactory;
    private Room room;

    private Player player;
    private Inventory inventory;

    private PauseMenu pauseMenu;
    private final DebugScreen debugScreen = new DebugScreen(UILayer);
    private DungeonUI dungeonUI;

    private SnowParticles snowParticles;

    private Room removalRoom;
    private Easing easing;
    private int roomTransitionCnt;

    private boolean alive;
    private DeathScreen deathScreen;
    private int deathCnt;

    private final Action nextRoom = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (removalRoom != null) {
                return;
            }
            if (Game.TEST) {
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
            if (!alive) {
                return;
            }
            alive = false;
            deathCnt = 1;
            fadeOut(2);
            player.resetKeyboardActions();
            dungeonUI.getWarningDisplay().death();
            setFadingEffectAlpha(60);
            setFadingEffectColor(Color.RED);
            room.setFreeze(true);
            Game.setFreezeFrame(20);
        }
    };

    public Dungeon(Game game, UILayer UILayer) {
        super(game, UILayer);
        easing = new Easing(60);
        deathScreen = new DeathScreen(UILayer, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                reset();
                fadeIn();
            }
        }, game.changePanel("mainMenu"));
        pauseMenu = new PauseMenu(UILayer, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                reset();
                fadeIn();
            }
        }, game.changePanel("mainMenu"));

        reset();

        getInputMap(2).put(KeyBinds.ESC, "pause");
        getActionMap().put("pause", UILayer.openPopupUI(pauseMenu));
        getInputMap(2).put(KeyBinds.DEBUG, "debug");
        getActionMap().put("debug", UILayer.openPopupUI(debugScreen));

        Room.setScreenShakeDuration(10);
        Room.setScreenShakeStrength(10);
    }

    @Override
    public void updateComponent() {
        if (dungeonUI.getTimer().getTime() == 0 && alive) {
            if (!alive) {
                return;
            }
            alive = false;
            deathCnt = 1;
            fadeOut(2);
            player.resetKeyboardActions();
            dungeonUI.getWarningDisplay().timeOut();
            setFadingEffectAlpha(60);
            setFadingEffectColor(Color.WHITE);
            room.setFreeze(true);
        }
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
        alive = true;
        inventory = new Inventory(UILayer, DiffSettings.startingInventorySize);
        WarningDisplay warningDisplay = new WarningDisplay();
        warningDisplay.starting();
        player = new Player(this, nextRoom, playerDeath, inventory, warningDisplay);
        dungeonUI = new DungeonUI(new HealthBar(player), new Timer(599, warningDisplay), new MiniMap(), warningDisplay);
        roomFactory = new RoomFactory(player, UILayer, dungeonUI.getMiniMap());
        room = roomFactory.getStartingRoom(STARTING_ROOM);
        inventory.setItemFactory(new ItemFactory(player, dungeonUI.getTimer()));
        dungeonUI.getMiniMap().setStartingRoom(room);
        snowParticles = new SnowParticles(player);
        debugScreen.updateRoom(room);
        debugScreen.setTimer(dungeonUI.getTimer());
        if (!Game.TEST) {
            add();
        } else {
            add(room);
            add(inventory);
        }
    }

    private void add() {
        add(room);
        add(snowParticles);
        add(dungeonUI);
        add(inventory);
        add(UILayer);
    }

    private void remove() {
        if (room == null) {
            return;
        }
        remove(room);
        remove(snowParticles);
        remove(dungeonUI);
        remove(inventory);

        remove(deathScreen);
        UILayer.removeAll();
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
}