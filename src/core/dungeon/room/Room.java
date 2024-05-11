package core.dungeon.room;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.KeyStroke;

import core.dungeon.RoomConnecter;
import core.dungeon.mechanics.LightingEngine;
import core.dungeon.mechanics.collision.CollisionChecker;
import core.dungeon.room.objects.Ladder;
import core.dungeon.room.objects.RoomObjectManager;
import core.dungeon.room.objects.entity.Player;
import core.dungeon.room.objects.objectUtilities.RoomObject;
import core.dungeon.room.tile.TileGrid;
import core.window.RoomMouseListener;
import core.window.SubScreen;

public class Room extends SubScreen {
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
        addMouseListener(new RoomMouseListener());

        for (char c : "WASD".toCharArray()) {
            getInputMap(2).put(KeyStroke.getKeyStroke("pressed " + c), "acc " + c);
            getActionMap().put("acc " + c, player.accelerate);
            getInputMap(2).put(KeyStroke.getKeyStroke("released " + c), "decel " + c);
            getActionMap().put("decel " + c, player.decelerate); 
        }
    }

    // TEMP
    public Room(TileGrid tileGrid, CollisionChecker collision, RoomObjectManager objectManager) {
        super(tileGrid.getWidth(), tileGrid.getHeight());
        // this.tileGrid = tileGrid;
        this.collision = collision;
        this.objectManager = objectManager;
        addObject(tileGrid);
        addObject(objectManager);
        addMouseListener(new RoomMouseListener());
    }

    public void setPlayer(Ladder ladder) {
        player.set((int) ladder.getPlayerPlacementX(), (int) ladder.getPlayerPlacementY(), collision);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        draw(g2d);
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