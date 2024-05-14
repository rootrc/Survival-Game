package core.dungeon.room;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import core.dungeon.mechanics.LightingEngine;
import core.dungeon.mechanics.collision.CollisionChecker;
import core.dungeon.room.objects.Ladder;
import core.dungeon.room.objects.entity.Player;
import core.dungeon.room.objects.object_utilities.RoomObject;
import core.dungeon.room.objects.object_utilities.RoomObjectManager;
import core.dungeon.room.tile.TileGrid;
import core.dungeon.room_connection.RoomConnecter;
import core.utilities.GameButton;
import core.window.Game;
import core.window.GameComponent;

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
        addObject(tileGrid);
        addObject(objectManager);
        addObject(player);
        addObject(lighting);
        // Test
        // add(new GameButton(player.interact, 500, 500, 100, 100));
    }

    // TEMP
    public Room(TileGrid tileGrid, CollisionChecker collision, RoomObjectManager objectManager) {
        super(tileGrid.getWidth(), tileGrid.getHeight());
        // this.tileGrid = tileGrid;
        this.collision = collision;
        this.objectManager = objectManager;
        addObject(tileGrid);
        addObject(objectManager);
        // addMouseListener(new RoomMouseListener());
    }

    public void setPlayer(Ladder ladder) {
        player.set((int) ladder.getPlayerPlacementX(), (int) ladder.getPlayerPlacementY(), collision);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        draw(g2d);
        if (Game.DEBUG) {
            g2d.setColor(Color.white);
            g2d.drawString(String.valueOf(getId()), 0, 10);
        }
    }

    public int getId() {
        return id;
    }

    public RoomObject getRoomObject(int idx) {
        return objectManager.get(idx);
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