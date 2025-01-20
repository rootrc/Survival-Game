package core.dungeon.room;

import java.awt.Graphics2D;

import core.Game;
import core.dungeon.mechanics.lighting.LightingEngine;
import core.dungeon.mechanics.particles.WalkingParticles;
import core.dungeon.room.entity.Player;
import core.dungeon.room.object.Ladder;
import core.dungeon.room.object_utilities.RoomObject;
import core.dungeon.room.object_utilities.RoomObjectManager;
import core.dungeon.room.tile.TileGrid;
import core.dungeon.room_connection.RoomConnecter;
import core.game_components.GameComponent;
import core.utilities.RNGUtilities;

public class Room extends GameComponent {
    private static int screenShakeCnt;
    private static int screenShakeStrength;

    private int id;
    private int depth;
    private Player player;
    private LightingEngine lightingEngine;
    private TileGrid tileGrid;
    private RoomConnecter connecter;
    private RoomObjectManager roomObjectManager;
    private WalkingParticles walkingParticles;

    private boolean isFrozen;

    // Description: The constructor of the class
    // Parameters: The room id, depth it's located at, the player character, the
    // room's lighting engine, tilegrid, and roomObject manager
    // Return: Nothing
    public Room(int id, int depth, Player player, LightingEngine lightingEngine, TileGrid tileGrid,
            RoomObjectManager roomObjectManager) {
        super(tileGrid.getWidth(), tileGrid.getHeight());
        this.id = id;
        this.depth = depth;
        this.player = player;
        this.lightingEngine = lightingEngine;
        this.tileGrid = tileGrid;
        this.roomObjectManager = roomObjectManager;
        connecter = new RoomConnecter();
        walkingParticles = new WalkingParticles(tileGrid, player);
        add(tileGrid.getTileGridFloor());
        add(walkingParticles);
        add(roomObjectManager);
        addRoomObject(player);
        add(tileGrid.getTileGridCeiling());
        add(lightingEngine);
    }

    // Description: Overrides update component so no updates occur when frozen (including components inside room)
    // Parameters: Nothing
    // Return: Nothing
    @Override
    public void updateComponent() {
        if (isFrozen) {
            return;
        }
        super.updateComponent();
    }

    // Description: Updates the Room
    // Parameters: Nothing
    // Return: Nothing
    public void update() {
        if (Game.TEST) {
            return;
        }
        // The "camera" which moves around the room depending on player
        int tx = Game.SCREEN_WIDTH / 2 - (int) player.getX();
        int ty = Game.SCREEN_HEIGHT / 2 - (int) player.getY();
        int dx = tx - getX();
        int dy = ty - getY();
        if (dx > 0) {
            moveX(Math.min(dx / 16, 48));
        } else {
            moveX(Math.max(dx / 16, -48));
        }
        if (dy > 0) {
            moveY(Math.min(dy / 16, 48));
        } else {
            moveY(Math.max(dy / 16, -48));
        }
        
        // ScreenShake
        if (screenShakeCnt != 0) {
            moveX(RNGUtilities.getInt(-screenShakeStrength, screenShakeStrength + 1));
            moveY(RNGUtilities.getInt(-screenShakeStrength, screenShakeStrength + 1));
            screenShakeCnt--;
            screenShakeStrength--;
        }
    }
    
    // Description: Draws the room, but empty since it's abstract method
    // Parameters: Graphics2D object
    // Return: Nothing
    public void drawComponent(Graphics2D g2d) {

    }

    // The following are lots of setters and getters and simple code

    public void setPlayer(Ladder ladder) {
        // Sets player based on ladder it came from
        player.setLocation(ladder.getPlayerPlacementX(), ladder.getPlayerPlacementY());
        tileGrid.setPlayer(player);
    }

    public int getId() {
        return id;
    }

    public int getDepth() {
        return depth;
    }

    public RoomObject getRoomObject(int idx) {
        return (RoomObject) roomObjectManager.getComponent(idx);
    }

    public void addRoomObject(RoomObject roomObject) {
        roomObjectManager.add(roomObject, -1);
    }

    public void addLadderConnection(Ladder ladder, int id) {
        connecter.addLadder(ladder, id);
    }

    public void addLadderConnection(Ladder ladder, Ladder ladder2) {
        connecter.addLadder(ladder, ladder2);
    }

    public int getConnectedRoomId(Ladder ladder) {
        return connecter.getRoomId(ladder);
    }

    public Ladder getConnectedLadder(Ladder ladder) {
        return connecter.getLadder(ladder);
    }

    public int getLadderUpCnt() {
        return roomObjectManager.getLadderUpCnt();
    }

    public int getLadderDownCnt() {
        return roomObjectManager.getLadderDownCnt();
    }

    public void setFreeze(boolean isFrozen) {
        this.isFrozen = isFrozen;
        lightingEngine.setPlayerPresent(!isFrozen);
    }

    public static void setScreenShakeDuration(int screenShakeDuration) {
        screenShakeCnt = (int) (screenShakeDuration * Game.screenShakeDurationMulti);
    }

    public static void setScreenShakeStrength(int screenShakeStrength) {
        Room.screenShakeStrength = (int) (screenShakeStrength * Game.screenShakeStrengthMulti);
    }

    // For Debug Screen, still setters and getters

    public Player getPlayer() {
        return player;
    }

    public int getEntityCnt() {
        return roomObjectManager.getComponentCount() + 1;
    }

}