package core.dungeon.room.roomfactory;

import java.util.HashMap;

import javax.swing.KeyStroke;

import core.dungeon.DungeonData;
import core.dungeon.DungeonLayoutGenerator;
import core.dungeon.mechanics.LightingEngine;
import core.dungeon.mechanics.collision.CollisionChecker;
import core.dungeon.room.Room;
import core.dungeon.room.objects.Ladder;
import core.dungeon.room.objects.RoomObjectManager;
import core.dungeon.room.objects.entity.Player;
import core.dungeon.room.tile.TileGrid;
import core.utilities.Factory;

public class RoomFactory extends Factory<Room>{
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
            previousRoom.addLadderConnection(player.getLadder(), dungeonGenerator.getGeneratedId(player.getLadder(), previousRoom, dungeonData));
        }
        RoomFileData file = new RoomFileData(previousRoom.getConnectedRoomId(player.getLadder()));
        if (rooms.containsKey(file.getId())) {
            Room nextRoom = rooms.get(file.getId());
            if (previousRoom.getConnectedLadder(player.getLadder()) == null) {
                createLadderConnection(player.getLadder(), previousRoom, nextRoom, file);
            }
            nextRoom.setPlayer(previousRoom.getConnectedLadder(player.getLadder()));
            return rooms.get(file.getId());
        }
        Room nextRoom = createRoom(file, file.getId());
        setKeyBinds(nextRoom);
        rooms.put(file.getId(), nextRoom);
        createLadderConnection(player.getLadder(), previousRoom, nextRoom, file);
        nextRoom.setPlayer(previousRoom.getConnectedLadder(player.getLadder()));
        return rooms.get(file.getId());
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
            room.getInputMap(2).put(KeyStroke.getKeyStroke("pressed " + c), "acc " + c);
            room.getInputMap(2).put(KeyStroke.getKeyStroke("released " + c), "decel " + c);
            room.getActionMap().put("acc " + c, player.accelerate);
            room.getActionMap().put("decel " + c, player.decelerate); 
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
