package game.dungeon.room_factory;

import java.util.HashMap;

import game.Game;
import game.dungeon.dungeon_ui.MiniMap;
import game.dungeon.mechanics.lighting.LightingEngine;
import game.dungeon.room.Room;
import game.dungeon.room.entity.Player;
import game.dungeon.room.object.Ladder;
import game.dungeon.room.object_utilities.RoomObjectManager;
import game.dungeon.room.tile.TileGrid;
import game.dungeon.room_connection.DungeonLayoutGenerator;
import game.game_components.Factory;
import game.game_components.UILayer;
import game.utilities.RNGUtilities;

public class RoomFactory extends Factory<Room> {
    private HashMap<Integer, Room> rooms;
    private TileGridFactory tileFactory;
    private RoomObjectManagerFactory roomObjectManagerFactory;
    private DungeonLayoutGenerator dungeonGenerator;
    private Player player;
    private UILayer UILayer;
    private MiniMap miniMap;

    public RoomFactory(Player player, UILayer UILayer, MiniMap miniMap) {
        this.player = player;
        this.UILayer = UILayer;
        this.miniMap = miniMap;
        rooms = new HashMap<>();
        tileFactory = new TileGridFactory();
        roomObjectManagerFactory = new RoomObjectManagerFactory(player);
        dungeonGenerator = new DungeonLayoutGenerator();
    }

    // TEMP
    public Room createRandomRoom(int N, int M) {
        TileGrid tileGrid = tileFactory.createRandomGrid(N, M, player);
        return new Room(tileGrid);
    }

    public Room getStartingRoom(int id) {
        player.set(312, 100);
        RoomFileData file = new RoomFileData(id);
        TileGrid tileGrid = tileFactory.createTileGrid(file, player);
        RoomObjectManager roomObjectManager = roomObjectManagerFactory.getRoomObjectManager(file, tileGrid);
        LightingEngine lightingEngine = new LightingEngine(player, tileGrid, roomObjectManager);
        Room room = new Room(id, player, lightingEngine, tileGrid, roomObjectManager, UILayer);
        if (!Game.DEBUG) {
            room.setLocation(Game.screenWidth / 2 - player.getX(), Game.screenHeight / 2 - player.getY());
        }
        putRoom(id, room);
        return rooms.get(id);
    }

    public Room getNextRoom(Room previousRoom, int depth, int[] depthMapCnt) {
        if (previousRoom.getConnectedRoomId(player.getLadder()) == -1) {
            previousRoom.addLadderConnection(player.getLadder(),
                    dungeonGenerator.getGeneratedId(player.getLadder(), depth + player.getDepthMovement(),
                            depthMapCnt));
        }
        int id = previousRoom.getConnectedRoomId(player.getLadder());
        Room nextRoom;
        if (rooms.containsKey(id)) {
            nextRoom = getRoom(id);
            if (previousRoom.getConnectedLadder(player.getLadder()) == null) {
                createLadderConnection(player.getLadder(), previousRoom, nextRoom);
            }
        } else {
            nextRoom = createRoom(new RoomFileData(id), id, previousRoom);
            putRoom(id, nextRoom);
            createLadderConnection(player.getLadder(), previousRoom, nextRoom);
            miniMap.updateNodeConnections(nextRoom, player.getLadder().getX());
        }
        miniMap.updateRoom(nextRoom);
        setTransition(previousRoom, nextRoom);
        nextRoom.setPlayer(previousRoom.getConnectedLadder(player.getLadder()));
        if (previousRoom.getConnectedLadder(player.getLadder()).getDirection() == Ladder.UP_DIRECTION) {
            player.setDirection(4);
        } else if (previousRoom.getConnectedLadder(player.getLadder()).getDirection() == Ladder.DOWN_DIRECTION) {
            player.setDirection(0);
        }
        return nextRoom;
    }

    private void setTransition(Room previousRoom, Room nextRoom) {
        if (Game.DEBUG) {
            return;
        }
        previousRoom.setFreeze(true);
        nextRoom.setLocation(Game.screenWidth / 2 - previousRoom.getConnectedLadder(player.getLadder()).getX(),
        Game.screenHeight / 2 - previousRoom.getConnectedLadder(player.getLadder()).getY() + 2048 * player.getDepthMovement());
    }

    private Room createRoom(RoomFileData file, int id, Room previousRoom) {
        TileGrid tileGrid = tileFactory.createTileGrid(file, player);
        RoomObjectManager roomObjectManager = roomObjectManagerFactory.getRoomObjectManager(file, tileGrid);
        LightingEngine lightingEngine = new LightingEngine(player, tileGrid, roomObjectManager);
        Room room = new Room(id, player, lightingEngine, tileGrid, roomObjectManager, UILayer);
        return room;
    }

    private void createLadderConnection(Ladder ladder0, Room previousRoom, Room nextRoom) {
        Ladder ladder1 = null;
        if (ladder0.getDirection() == Ladder.UP_DIRECTION) {
            while (ladder1 == null || nextRoom.getConnectedLadder(ladder1) != null) {
                ladder1 = (Ladder) nextRoom
                        .getRoomObject(nextRoom.getLadderUpCnt() + RNGUtilities.getInt(nextRoom.getLadderDownCnt()));
            }
        } else if (ladder0.getDirection() == Ladder.DOWN_DIRECTION) {
            while (ladder1 == null || nextRoom.getConnectedLadder(ladder1) != null) {
                ladder1 = (Ladder) nextRoom.getRoomObject(RNGUtilities.getInt(nextRoom.getLadderUpCnt()));
            }
        }
        nextRoom.addLadderConnection(ladder1, previousRoom.getId());
        nextRoom.addLadderConnection(ladder1, ladder0);
        previousRoom.addLadderConnection(ladder0, ladder1);
    }

    private void putRoom(int id, Room room) {
        rooms.put(id, room);
    }

    private Room getRoom(int id) {
        rooms.get(id).addRoomObject(player);
        return rooms.get(id);
    }

}
