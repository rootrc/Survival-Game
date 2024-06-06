package core.dungeon.room.room_utilities.roomfactory;

import java.util.HashMap;

import javax.swing.KeyStroke;

import core.dungeon.mechanics.LightingEngine;
import core.dungeon.mechanics.collision.CollisionChecker;
import core.dungeon.room.Room;
import core.dungeon.room.objects.Ladder;
import core.dungeon.room.objects.entity.Player;
import core.dungeon.room.objects.object_utilities.RoomObjectManager;
import core.dungeon.room.tile.TileGrid;
import core.dungeon.room_connection.DungeonData;
import core.dungeon.room_connection.DungeonLayoutGenerator;
import core.utilities.Factory;
import core.window.GamePanel;

public class RoomFactory extends Factory<Room> {
    private HashMap<Integer, Room> rooms;
    private TileGridFactory tileFactory;
    private CollisionCheckerFactory collisionFactory;
    private RoomObjectManagerFactory objectManagerFactory;
    private DungeonData dungeonData;
    private DungeonLayoutGenerator dungeonGenerator;
    private Player player;
    private LightingEngine lighting;

    public RoomFactory(DungeonData dungeonData, Player player, LightingEngine lighting) {
        this.dungeonData = dungeonData;
        this.player = player;
        this.lighting = lighting;
        rooms = new HashMap<>();
        tileFactory = new TileGridFactory();
        collisionFactory = new CollisionCheckerFactory();
        objectManagerFactory = new RoomObjectManagerFactory();
        dungeonGenerator = new DungeonLayoutGenerator();
    }

    public Room getStartingRoom(int id) {
        if (rooms.containsKey(id)) {
            return rooms.get(id);
        }
        RoomFileData file = new RoomFileData(id);
        TileGrid tileGrid = tileFactory.createGrid(file);
        CollisionChecker collision = collisionFactory.getCollisionChecker(tileGrid);
        player.set(200, 300, collision);
        rooms.put(id, new Room(id, player, lighting, tileGrid, collision,
                objectManagerFactory.getRoomObjectManager(player, file)));
        setKeyBinds(rooms.get(id));
        return rooms.get(id);
    }

    public Room getNextRoom(Room previousRoom) {
        dungeonData.changeDepth(player.getLadder());
        if (previousRoom.getConnectedRoomId(player.getLadder()) == -1) {
            previousRoom.addLadderConnection(player.getLadder(),
                    dungeonGenerator.getGeneratedId(player.getLadder(), dungeonData));
        }
        RoomFileData file = new RoomFileData(previousRoom.getConnectedRoomId(player.getLadder()));
        if (rooms.containsKey(file.getId())) {
            Room nextRoom = rooms.get(file.getId());
            if (previousRoom.getConnectedLadder(player.getLadder()) == null) {
                createLadderConnection(player.getLadder(), previousRoom, nextRoom, file);
            }
            setTransition(previousRoom, nextRoom);
            nextRoom.setPlayer(previousRoom.getConnectedLadder(player.getLadder()));
            return rooms.get(file.getId());
        }
        Room nextRoom = createRoom(file, file.getId());
        setKeyBinds(nextRoom);
        rooms.put(file.getId(), nextRoom);
        createLadderConnection(player.getLadder(), previousRoom, nextRoom, file);
        setTransition(previousRoom, nextRoom);
        nextRoom.setPlayer(previousRoom.getConnectedLadder(player.getLadder()));
        return rooms.get(file.getId());
    }

    private void setTransition(Room previousRoom, Room nextRoom) {
        // double x = GamePanel.screenWidth / 2 - previousRoom.getConnectedLadder(player.getLadder()).getX();
        // double y = GamePanel.screenHeight / 2 - previousRoom.getConnectedLadder(player.getLadder()).getY();
        nextRoom.setLocation((int) (previousRoom.getX()), (int) (previousRoom.getY()));
    }

    private Room createRoom(RoomFileData file, int id) {
        TileGrid tileGrid = tileFactory.createGrid(file);
        CollisionChecker collision = collisionFactory.getCollisionChecker(tileGrid);
        Room room = new Room(id, player, lighting, tileGrid, collision,
                objectManagerFactory.getRoomObjectManager(player, file));
        return room;
    }

    private void setKeyBinds(Room room) {
        for (char c : "WASD".toCharArray()) {
            room.getInputMap(2).put(KeyStroke.getKeyStroke(
                    new StringBuilder("pressed ").append(c).toString()),
                    new StringBuilder("acc ").append(c).toString());
            room.getInputMap(2).put(KeyStroke.getKeyStroke(
                    new StringBuilder("released ").append(c).toString()),
                    new StringBuilder("decel ").append(c).toString());
            room.getActionMap().put(new StringBuilder("acc ").append(c).toString(), player.accelerate);
            room.getActionMap().put(new StringBuilder("decel ").append(c).toString(), player.decelerate);
        }
        room.getInputMap(2).put(KeyStroke.getKeyStroke("E"), "interact");
        room.getActionMap().put("interact", player.interact);
    }

    private void createLadderConnection(Ladder ladder0, Room previousRoom, Room nextRoom, RoomFileData file) {
        Ladder ladder1 = null;
        if (ladder0.getDirection() == 1) {
            while (ladder1 == null || nextRoom.getConnectedLadder(ladder1) != null) {
                ladder1 = (Ladder) nextRoom
                        .getRoomObject((int) (file.getLadderUp() + (file.getLadderDown() * Math.random())));
            }
        } else {
            while (ladder1 == null || nextRoom.getConnectedLadder(ladder1) != null) {
                ladder1 = (Ladder) nextRoom.getRoomObject((int) (file.getLadderUp() * Math.random()));
            }
        }
        nextRoom.addLadderConnection(ladder1, previousRoom.getId());
        nextRoom.addLadderConnection(ladder1, ladder0);
        previousRoom.addLadderConnection(ladder0, ladder1);
        dungeonData.updateLadderConnections(ladder0, previousRoom.getId());
        dungeonData.updateLadderConnections(ladder1, nextRoom.getId());
    }

    public Room createRandomRoom(int N, int M) {
        TileGrid tileGrid = tileFactory.createRandomGrid(N, M);
        return new Room(tileGrid, collisionFactory.getCollisionChecker(tileGrid), new RoomObjectManager(null));
    }

}
