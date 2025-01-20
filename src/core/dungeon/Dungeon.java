package core.dungeon;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

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
        // Description: An action that transitions the player from one room to another
        // Parameters: An ActionEvent
        // Return: Nothing
        public void actionPerformed(ActionEvent e) {
            if (removalRoom != null) {
                return;
            }
            if (Game.TEST) {
                remove(room);
            } else {
                // Displays previous room as transitioning
                removalRoom = room;
                if (player.getDepthMovement() == 1) {
                    easing.set(room.getLocation(), new Point(room.getX(), -removalRoom.getHeight()));
                } else if (player.getDepthMovement() == -1) {
                    easing.set(room.getLocation(), new Point(room.getX(), getHeight()));
                }
            }

            // Adding new room
            room = roomFactory.getNextRoom(room);
            add(room, -1);
            debugScreen.updateRoom(room);
            snowParticles.setDepth(room.getDepth());
            revalidate();
            Game.setFreezeFrame(10);
        }
    };

    private final Action playerDeath = new AbstractAction() {
        // Description: An action that's run when the player dies and transitions to death screen
        // Parameters: An ActionEvent
        // Return: Nothing
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

    // Description: The constructor of the class
    // Parameters: A Game object and UILayer object
    // Return: Nothing
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
        
        // Sets keybinds
        getInputMap(2).put(KeyBinds.ESC, "pause");
        getActionMap().put("pause", UILayer.openPopupUI(pauseMenu));
        getInputMap(2).put(KeyBinds.DEBUG, "debug");
        getActionMap().put("debug", UILayer.openPopupUI(debugScreen));

        Room.setScreenShakeDuration(10);
        Room.setScreenShakeStrength(10);
    }
    
    // Description: Updates the dungeon
    // Parameters: Nothing
    // Return: Nothing
    @Override
    public void updateComponent() {
        if (dungeonUI.getTimer().getTime() == 0 && alive) { // If time has run out
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
        if (removalRoom != null) { // If in the middle of a transition
            roomTransitionCnt++;
            removalRoom.setLocation(easing.easeInOutQuad(roomTransitionCnt));
            if (easing.getP1().equals(removalRoom.getLocation())) {
                remove(removalRoom);
                removalRoom.setFreeze(false);
                removalRoom = null;
                roomTransitionCnt = 0;
            }
        }
        if (deathCnt != 0) { // If transition to death screen is complete
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

    // Description: Resets the dungeon
    // Parameters: Nothing
    // Return: Nothing
    public void reset() {
        remove();
        alive = true;
        inventory = new Inventory(UILayer, DiffSettings.startingInventorySize);
        WarningDisplay warningDisplay = new WarningDisplay();
        warningDisplay.starting();
        player = new Player(this, nextRoom, playerDeath, inventory, warningDisplay);
        dungeonUI = new DungeonUI(new HealthBar(player), new Timer(599, warningDisplay), new MiniMap(), warningDisplay);
        roomFactory = new RoomFactory(player, dungeonUI.getMiniMap());
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

    // Description: Adds all the components to the dungeon
    // Parameters: Nothing
    // Return: Nothing
    private void add() {
        add(room);
        add(snowParticles);
        add(dungeonUI);
        add(inventory);
        add(UILayer);
    }

    // Description: Removes all the components to the dungeon
    // Parameters: Nothing
    // Return: Nothing
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

    // Description: Generates and returns a list of strings to be displayed on death screen based on room data
    // Parameters: Nothing
    // Return: A list of all the strings
    public ArrayList<String> getDeathScreenDisplay() {
        ArrayList<String> list = new ArrayList<>();
        int time = (dungeonUI.getTimer().getStartTime() - dungeonUI.getTimer().getTime());
        list.add("Time Survived: " + String.format("%d:%02d", ((time / 60) % 60), (time % 60)));
        list.add("Rooms Discovered: " + dungeonUI.getMiniMap().getExploredRoomCnt());
        list.add("Depth Traversed: " + (dungeonUI.getMiniMap().getMaxDepth() + 1));
        list.add("Items Acquired: " + inventory.getOccupiedSlots());
        list.add("Interactions: " + player.getInteractionCnt());
        int pointMultiplier = (int) (100 * player.getStats().getPointMultiplier());
        if (DiffSettings.difficulty == 0) {
            pointMultiplier /= 2;
        } else if (DiffSettings.difficulty == 2) {
            pointMultiplier *= 2;
        }
        list.add("Point Multiplier: " + pointMultiplier + '%');
        return list;
    }

    // Description: Caculates the score earned and returns it
    // Parameters: Nothing
    // Return: An int, the score
    public int getScore() {
        int sum = 0;
        sum += (dungeonUI.getTimer().getStartTime() - dungeonUI.getTimer().getTime());
        sum += 30 * (dungeonUI.getMiniMap().getExploredRoomCnt() - 1);
        sum += 100 * dungeonUI.getMiniMap().getMaxDepth();
        sum += 100 * inventory.getOccupiedSlots();
        sum += 5 * player.getInteractionCnt();
        sum *= player.getStats().getPointMultiplier();
        if (dungeonUI.getTimer().getTime() == 0) {
            sum *= 1.5;
        }
        sum *= 10;
        if (DiffSettings.difficulty == 0) {
            sum /= 2;
        } else if (DiffSettings.difficulty == 2) {
            sum *= 2;
        }
        return sum;
    }
}