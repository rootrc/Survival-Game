package game.dungeon.room;

import java.awt.Color;
import java.awt.Graphics2D;

import game.Game;
import game.dungeon.Dungeon;
import game.dungeon.mechanics.CollisionChecker;
import game.dungeon.mechanics.LightingEngine;
import game.dungeon.room.entity.Player;
import game.dungeon.room.object.Ladder;
import game.dungeon.room.object_utilities.RoomObject;
import game.dungeon.room.object_utilities.RoomObjectManager;
import game.dungeon.room.tile.TileGrid;
import game.dungeon.room_connection.RoomConnecter;
import game.game_components.GameComponent;

public class Room extends GameComponent {
    private int id;
    private Player player;
    // private TileGrid tileGrid;
    private RoomConnecter connecter;
    private RoomObjectManager objectManager;
    private CollisionChecker collision;

    public Room(int id, Player player, LightingEngine lighting, TileGrid tileGrid, CollisionChecker collision,
            RoomObjectManager objectManager) {
        super(tileGrid.getWidth(), tileGrid.getHeight());
        this.id = id;
        this.player = player;
        // this.tileGrid = tileGrid;
        connecter = new RoomConnecter();
        this.collision = collision;
        this.objectManager = objectManager;
        add(tileGrid);
        add(objectManager);
        add(player);
        add(lighting);
    }

    // TEMP
    public Room(TileGrid tileGrid, CollisionChecker collision, RoomObjectManager objectManager) {
        super(tileGrid.getWidth(), tileGrid.getHeight());
        // this.tileGrid = tileGrid;
        this.collision = collision;
        this.objectManager = objectManager;
        add(tileGrid);
        add(objectManager);
    }

    public void drawComponent(Graphics2D g2d) {

    }

    @Override
    public void updateComponent() {
        if (Dungeon.getUI(0) == null) {
            super.updateComponent();
        }
    }

    public void update() {
        if (!Game.DEBUG) {
            int tx = Game.screenWidth / 2 - (int) player.getX();
            int ty = Game.screenHeight / 2 - (int) player.getY();
            int dx = tx - getX();
            int dy = ty - getY();
            moveX(Math.min(dx / 16, 16));
            moveY(Math.min(dy / 16, 16));
        }
    }

    public void setPlayer(Ladder ladder) {
        player.set(ladder.getPlayerPlacementX(), ladder.getPlayerPlacementY(), collision);
    }

    public int getId() {
        return id;
    }

    public RoomObject getRoomObject(int idx) {
        return (RoomObject) objectManager.getComponent(idx);
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
}