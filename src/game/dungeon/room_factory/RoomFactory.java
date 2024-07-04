package game.dungeon.room_factory;

import java.util.HashMap;

import game.Game;
import game.dungeon.mechanics.CollisionChecker;
import game.dungeon.mechanics.LightingEngine;
import game.dungeon.room.Room;
import game.dungeon.room.entity.Player;
import game.dungeon.room.object.Ladder;
import game.dungeon.room.tile.TileGrid;
import game.dungeon.room_connection.DungeonData;
import game.dungeon.room_connection.DungeonLayoutGenerator;
import game.game_components.Factory;
import game.game_components.UILayer;

public class RoomFactory extends Factory<Room> {
    private HashMap<Integer, Room> rooms;
    private TileGridFactory tileFactory;
    private CollisionCheckerFactory collisionFactory;
    private RoomObjectManagerFactory objectManagerFactory;
    private DungeonData dungeonData;
    private DungeonLayoutGenerator dungeonGenerator;
    private Player player;
    private LightingEngine lighting;
    private UILayer UILayer;

    public RoomFactory(Player player, UILayer UILayer) {
        this.player = player;
        dungeonData = new DungeonData();
        this.UILayer = UILayer;
        lighting = new LightingEngine(player);
        rooms = new HashMap<>();
        tileFactory = new TileGridFactory();
        collisionFactory = new CollisionCheckerFactory();
        objectManagerFactory = new RoomObjectManagerFactory(player);
        dungeonGenerator = new DungeonLayoutGenerator();
    }

    // TEMP
    public Room createRandomRoom(int N, int M) {
        TileGrid tileGrid = tileFactory.createRandomGrid(N, M, player);
        return new Room(tileGrid, collisionFactory.getCollisionChecker(N, M, tileGrid), objectManagerFactory.getRoomObjectManager(tileGrid));
    }

    public Room getStartingRoom(int id) {
        RoomFileData file = new RoomFileData(id);
        TileGrid tileGrid = tileFactory.createTileGrid(file, player);
        CollisionChecker collision = collisionFactory.getCollisionChecker(file, tileGrid);
        player.set(312, 100, collision);
        Room room = new Room(id, player, lighting, tileGrid, collision,
                objectManagerFactory.getRoomObjectManager(file, tileGrid), UILayer);
        if (!Game.DEBUG) {
            room.setLocation(Game.screenWidth / 2 - player.getX(), Game.screenHeight / 2 - player.getY());
        }
        putRoom(id, room);
        return rooms.get(id);
    }

    public Room getNextRoom(Room previousRoom) {
        dungeonData.changeDepth(player.getLadder());
        if (previousRoom.getConnectedRoomId(player.getLadder()) == -1) {
            previousRoom.addLadderConnection(player.getLadder(),
                    dungeonGenerator.getGeneratedId(player.getLadder(), dungeonData));
        }
        RoomFileData file = new RoomFileData(previousRoom.getConnectedRoomId(player.getLadder()));
        Room nextRoom;
        if (rooms.containsKey(file.getId())) {
            nextRoom = getRoom(file.getId());
            if (previousRoom.getConnectedLadder(player.getLadder()) == null) {
                createLadderConnection(player.getLadder(), previousRoom, nextRoom, file);
            }
        } else {
            nextRoom = createRoom(file, file.getId(), previousRoom);
            putRoom(file.getId(), nextRoom);
            createLadderConnection(player.getLadder(), previousRoom, nextRoom, file);
        }
        setTransition(previousRoom, nextRoom);
        nextRoom.setPlayer(previousRoom.getConnectedLadder(player.getLadder()));
        if (previousRoom.getConnectedLadder(player.getLadder()).getDirection() == 1) {
            player.setDirection(4);
        } else if (previousRoom.getConnectedLadder(player.getLadder()).getDirection() == -1) {
            player.setDirection(0);
        }
        return nextRoom;
    }

    private void setTransition(Room previousRoom, Room nextRoom) {
        nextRoom.setLocation(previousRoom.getX(), previousRoom.getY());
    }

    private Room createRoom(RoomFileData file, int id, Room previousRoom) {
        TileGrid tileGrid = tileFactory.createTileGrid(file, player);
        CollisionChecker collision = collisionFactory.getCollisionChecker(file, tileGrid);
        Room room = new Room(id, player, lighting, tileGrid, collision,
                objectManagerFactory.getRoomObjectManager(file, tileGrid), UILayer);
        return room;
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

    private void putRoom(int id, Room room) {
        rooms.put(id, room);
    }

    private Room getRoom(int id) {
        rooms.get(id).add(player);
        rooms.get(id).add(lighting);
        rooms.get(id).add(UILayer);
        return rooms.get(id);
    }

}
