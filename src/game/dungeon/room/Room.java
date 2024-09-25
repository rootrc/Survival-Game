package game.dungeon.room;

import java.awt.Graphics2D;

import game.Game;
import game.dungeon.mechanics.lighting.LightingEngine;
import game.dungeon.mechanics.particles.WalkingParticles;
import game.dungeon.room.entity.Player;
import game.dungeon.room.object.Ladder;
import game.dungeon.room.object_utilities.RoomObject;
import game.dungeon.room.object_utilities.RoomObjectManager;
import game.dungeon.room.tile.TileGrid;
import game.dungeon.room_connection.RoomConnecter;
import game.game_components.GameComponent;
import game.game_components.UILayer;
import game.utilities.RNGUtilities;

public class Room extends GameComponent {
    private int id;
    private Player player;
    private LightingEngine lightingEngine;
    private TileGrid tileGrid;
    private RoomConnecter connecter;
    private RoomObjectManager roomObjectManager;
    private UILayer UILayer;
    private WalkingParticles walkingParticles;

    private boolean isFrozen;

    public Room(int id, Player player, LightingEngine lightingEngine, TileGrid tileGrid,
            RoomObjectManager roomObjectManager,
            UILayer UIlayer) {
        super(tileGrid.getWidth(), tileGrid.getHeight());
        this.id = id;
        this.player = player;
        this.lightingEngine = lightingEngine;
        this.tileGrid = tileGrid;
        this.roomObjectManager = roomObjectManager;
        this.UILayer = new UILayer();
        connecter = new RoomConnecter();
        walkingParticles = new WalkingParticles(tileGrid, player);
        add(tileGrid.getTileGridFloor());
        add(walkingParticles);
        add(roomObjectManager);
        addRoomObject(player);
        add(tileGrid.getTileGridCeiling());
        add(lightingEngine);
    }

    // TEMP
    public Room(TileGrid tileGrid) {
        super(tileGrid.getWidth(), tileGrid.getHeight());
        this.tileGrid = tileGrid;
        add(tileGrid.getTileGridFloor());
        add(tileGrid.getTileGridCeiling());
    }

    public void drawComponent(Graphics2D g2d) {

    }

    @Override
    public void updateComponent() {
        if (UILayer.getComponentCount() != 0 || isFrozen) {
            return;
        }
        super.updateComponent();
    }

    public void update() {
        if (Game.DEBUG) {
            return;
        }
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

        if (screenShakeCnt != 0) {
            moveX(RNGUtilities.getInt(-screenShakeStrength, screenShakeStrength + 1));
            moveY(RNGUtilities.getInt(-screenShakeStrength, screenShakeStrength + 1));
            screenShakeCnt--;
            screenShakeStrength--;
        }
    }

    public void setPlayer(Ladder ladder) {
        player.set(ladder.getPlayerPlacementX(), ladder.getPlayerPlacementY());
        tileGrid.setPlayer(player);
    }

    public int getId() {
        return id;
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

    private static int screenShakeCnt;
    private static int screenShakeStrength;

    public static void setScreenShakeDuration(int screenShakeDuration) {
        screenShakeCnt = screenShakeDuration;
    }

    public static void setScreenShakeStrength(int screenShakeStrength) {
        Room.screenShakeStrength = screenShakeStrength;
    }

    // For Debug Screen
    public int getEntityCount() {
        return roomObjectManager.getComponentCount() + 1;
    }

    public Player getPlayer() {
        return player;
    }
}