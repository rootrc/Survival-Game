package core.dungeon.room_factory;

import java.util.HashMap;

import core.Game;
import core.dungeon.dungeon_ui.MiniMap;
import core.dungeon.mechanics.lighting.LightingEngine;
import core.dungeon.room.Room;
import core.dungeon.room.entity.Player;
import core.dungeon.room.object.Ladder;
import core.dungeon.room.object_utilities.RoomObjectManager;
import core.dungeon.room.tile.TileGrid;
import core.dungeon.room_connection.DungeonLayoutGenerator;
import core.game_components.Factory;
import core.game_components.UILayer;
import core.utilities.RNGUtilities;

public class RoomFactory extends Factory<Room> {
    private HashMap<Integer, Room> rooms;
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
        roomObjectManagerFactory = new RoomObjectManagerFactory(player);
        dungeonGenerator = new DungeonLayoutGenerator();
    }

    // TEMP
    public Room createRandomRoom(int N, int M) {
        TileGrid tileGrid = TileGridFactory.createRandomGrid(N, M, player);
        return new Room(tileGrid);
    }

    public Room getStartingRoom(int id) {
        player.set(312, 100);
        RoomFileData file = new RoomFileData(id, 0);
        TileGrid tileGrid = TileGridFactory.createTileGrid(file, player);
        RoomObjectManager roomObjectManager = roomObjectManagerFactory.getRoomObjectManager(file, tileGrid);
        LightingEngine lightingEngine = new LightingEngine(player, tileGrid, roomObjectManager);
        Room room = new Room(id, 0, player, lightingEngine, tileGrid, roomObjectManager, UILayer);
        if (!Game.DEBUG) {
            room.setLocation(Game.SCREEN_WIDTH / 2 - player.getX(), Game.SCREEN_HEIGHT / 2 - player.getY());
        }
        putRoom(id, room);
        return rooms.get(id);
    }

    public Room getNextRoom(Room previousRoom) {
        if (player.getLadder() == null) {
            return previousRoom;
        }
        if (previousRoom.getConnectedRoomId(player.getLadder()) == -1) {
            previousRoom.addLadderConnection(player.getLadder(),
                    dungeonGenerator.getGeneratedId(player.getLadder().getDirection(),
                            previousRoom.getDepth() + player.getDepthMovement()));
        }
        int id = previousRoom.getConnectedRoomId(player.getLadder());
        Room nextRoom;
        if (rooms.containsKey(id)) {
            nextRoom = getRoom(id);
            if (previousRoom.getConnectedLadder(player.getLadder()) == null) {
                createLadderConnection(player.getLadder(), previousRoom, nextRoom);
            }
        } else {
            nextRoom = createRoom(new RoomFileData(id, previousRoom.getDepth()), id, previousRoom);
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
        nextRoom.setLocation(Game.SCREEN_WIDTH / 2 - previousRoom.getConnectedLadder(player.getLadder()).getX(),
                Game.SCREEN_HEIGHT / 2 - previousRoom.getConnectedLadder(player.getLadder()).getY()
                        + 2048 * player.getDepthMovement());
    }

    private Room createRoom(RoomFileData file, int id, Room previousRoom) {
        TileGrid tileGrid = TileGridFactory.createTileGrid(file, player);
        RoomObjectManager roomObjectManager = roomObjectManagerFactory.getRoomObjectManager(file, tileGrid);
        LightingEngine lightingEngine = new LightingEngine(player, tileGrid, roomObjectManager);
        Room room = new Room(id, previousRoom.getDepth() + player.getDepthMovement(), player, lightingEngine, tileGrid,
                roomObjectManager, UILayer);
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