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
import core.utilities.RNGUtilities;

public class RoomFactory extends Factory<Room> {
    private HashMap<Integer, Room> rooms;
    private RoomObjectManagerFactory roomObjectManagerFactory;
    private DungeonLayoutGenerator dungeonGenerator;
    private Player player;
    private MiniMap miniMap;

    // Description: Constructor of the class
    // Parameters: Player and Minimap objects
    // Return: Nothing
    public RoomFactory(Player player, MiniMap miniMap) {
        this.player = player;
        this.miniMap = miniMap;
        rooms = new HashMap<>();
        roomObjectManagerFactory = new RoomObjectManagerFactory(player);
        dungeonGenerator = new DungeonLayoutGenerator();
    }

    // Description: Gets the first room in the dungeon
    // Parameters: Id of the room
    // Return: The starting room
    public Room getStartingRoom(int id) {
        player.setLocation(312, 100);
        // Creates all the objects required to make room
        RoomFileData file = new RoomFileData(id, 0);
        TileGrid tileGrid = TileGridFactory.createTileGrid(file, player);
        RoomObjectManager roomObjectManager = roomObjectManagerFactory.getRoomObjectManager(file, tileGrid);
        LightingEngine lightingEngine = new LightingEngine(player, tileGrid, roomObjectManager);
        Room room = new Room(id, 0, player, lightingEngine, tileGrid, roomObjectManager);

        if (!Game.TEST) {
            room.setLocation(Game.SCREEN_WIDTH / 2 - player.getX(), Game.SCREEN_HEIGHT / 2 - player.getY());
        }
        putRoom(id, room);
        return rooms.get(id);
    }

    // Description: Gets the next room based on previous room and the ladder the
    // player is using
    // Parameters: Previous room
    // Return: The next room
    public Room getNextRoom(Room previousRoom) {
        if (player.getLadder() == null) { // For rare errors due to multi-threading
            return previousRoom;
        }
        if (previousRoom.getConnectedRoomId(player.getLadder()) == -1) { // If the ladder currently doesn't connect to
                                                                         // anything
            // Connect the ladder to a new semi-randomly chosen room
            previousRoom.addLadderConnection(player.getLadder(),
                    dungeonGenerator.getGeneratedId(player.getLadder().getDirection(),
                            previousRoom.getDepth() + player.getDepthMovement()));
        }
        // Get the id of connected room
        int id = previousRoom.getConnectedRoomId(player.getLadder());
        Room nextRoom;
        if (rooms.containsKey(id)) { // If the connected room already exists
            nextRoom = getRoom(id);
            if (previousRoom.getConnectedLadder(player.getLadder()) == null) { // Checks if ladder connects to another
                                                                               // ladder
                createLadderConnection(player.getLadder(), previousRoom, nextRoom);
            }
        } else { // If the connected room doesn't exist
            // Create the room and add connections
            nextRoom = createRoom(new RoomFileData(id, previousRoom.getDepth()), id, previousRoom);
            putRoom(id, nextRoom);
            createLadderConnection(player.getLadder(), previousRoom, nextRoom);
            miniMap.updateNodeConnections(nextRoom, player.getLadder().getX());
        }
        miniMap.updateRoom(nextRoom);
        setTransition(previousRoom, nextRoom);
        nextRoom.setPlayer(previousRoom.getConnectedLadder(player.getLadder()));
        // Sets direction of player based on ladder direction
        if (previousRoom.getConnectedLadder(player.getLadder()).getDirection() == Ladder.UP_DIRECTION) {
            player.setDirection(4);
        } else if (previousRoom.getConnectedLadder(player.getLadder()).getDirection() == Ladder.DOWN_DIRECTION) {
            player.setDirection(0);
        }
        return nextRoom;
    }

    // Description: Sets the transition between two rooms
    // Parameters: The previous room and next room
    // Return: Nothing
    private void setTransition(Room previousRoom, Room nextRoom) {
        if (Game.TEST) {
            return;
        }
        previousRoom.setFreeze(true);
        // This is so the "camera" can do the transition
        nextRoom.setLocation(Game.SCREEN_WIDTH / 2 - previousRoom.getConnectedLadder(player.getLadder()).getX(),
                Game.SCREEN_HEIGHT / 2 - previousRoom.getConnectedLadder(player.getLadder()).getY()
                        + 2048 * player.getDepthMovement());
    }

    // Description: Creates a room based on file data, id, and previous room
    // Parameters: Room file data, id, and previous room
    // Return: The room
    private Room createRoom(RoomFileData file, int id, Room previousRoom) {
        TileGrid tileGrid = TileGridFactory.createTileGrid(file, player);
        RoomObjectManager roomObjectManager = roomObjectManagerFactory.getRoomObjectManager(file, tileGrid);
        LightingEngine lightingEngine = new LightingEngine(player, tileGrid, roomObjectManager);
        Room room = new Room(id, previousRoom.getDepth() + player.getDepthMovement(), player, lightingEngine, tileGrid,
                roomObjectManager);
        return room;
    }

    // Description: Creates a ladder connection with a known starting ladder,
    // previous room, and next room
    // Parameters: Starting ladder, previous room, and next room
    // Return: Nothing
    private void createLadderConnection(Ladder ladder0, Room previousRoom, Room nextRoom) {
        Ladder ladder1 = null;
        if (ladder0.getDirection() == Ladder.UP_DIRECTION) { // If starting ladder is up
            while (ladder1 == null || nextRoom.getConnectedLadder(ladder1) != null) { // Randomly chooses down ladder
                                                                                      // that is not connected
                ladder1 = (Ladder) nextRoom
                        .getRoomObject(nextRoom.getLadderUpCnt() + RNGUtilities.getInt(nextRoom.getLadderDownCnt()));
            }
        } else if (ladder0.getDirection() == Ladder.DOWN_DIRECTION) { // If starting ladder is down
            while (ladder1 == null || nextRoom.getConnectedLadder(ladder1) != null) { // Randomly chooses up ladder that
                                                                                      // is not connected
                ladder1 = (Ladder) nextRoom.getRoomObject(RNGUtilities.getInt(nextRoom.getLadderUpCnt()));
            }
        }
        // adds ladder to ladder connections and room to room connections
        nextRoom.addLadderConnection(ladder1, previousRoom.getId());
        nextRoom.addLadderConnection(ladder1, ladder0);
        previousRoom.addLadderConnection(ladder0, ladder1);
    }

    // Description: Caches a room
    // Parameters: Room id, and the Room
    // Return: Nothing
    private void putRoom(int id, Room room) {
        rooms.put(id, room);
    }

    // Description: Get a cached room
    // Parameters: Room id
    // Return: The room
    private Room getRoom(int id) {
        rooms.get(id).addRoomObject(player);
        return rooms.get(id);
    }
}