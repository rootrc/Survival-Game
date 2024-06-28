package game.dungeon.room_factory;

import java.util.HashMap;

import javax.swing.KeyStroke;

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

public class RoomFactory extends Factory<Room> {
    private HashMap<Integer, Room> rooms;
    private TileGridFactory tileFactory;
    private CollisionCheckerFactory collisionFactory;
    private RoomObjectManagerFactory objectManagerFactory;
    private DungeonData dungeonData;
    private DungeonLayoutGenerator dungeonGenerator;
    private Player player;
    private LightingEngine lighting;

    public RoomFactory(Player player) {
        this.player = player;
        dungeonData = new DungeonData();
        lighting = new LightingEngine(player);
        rooms = new HashMap<>();
        tileFactory = new TileGridFactory();
        collisionFactory = new CollisionCheckerFactory();
        objectManagerFactory = new RoomObjectManagerFactory(player);
        dungeonGenerator = new DungeonLayoutGenerator();
    }

    public Room getStartingRoom(int id) {
        RoomFileData file = new RoomFileData(id);
        TileGrid tileGrid = tileFactory.createGrid(file);
        CollisionChecker collision = collisionFactory.getCollisionChecker(tileGrid);
        player.set(312, 100, collision);
        Room room = new Room(id, player, lighting, tileGrid, collision,
                objectManagerFactory.getRoomObjectManager(file));
        if (!Game.DEBUG) {
            room.setLocation(Game.screenWidth / 2 - player.getX(), Game.screenHeight / 2 - player.getY());
        }
        putRoom(id, room);
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
        } else if (previousRoom.getConnectedLadder(player.getLadder()).getDirection() == -1){
            player.setDirection(0);
        }
        return nextRoom;
    }

    private void setTransition(Room previousRoom, Room nextRoom) {
        nextRoom.setLocation(previousRoom.getX(), previousRoom.getY());
    }

    private Room createRoom(RoomFileData file, int id, Room previousRoom) {
        TileGrid tileGrid = tileFactory.createGrid(file);
        CollisionChecker collision = collisionFactory.getCollisionChecker(tileGrid);
        Room room = new Room(id, player, lighting, tileGrid, collision,
                objectManagerFactory.getRoomObjectManager(file));
        setKeyBinds(room);
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
        Room room = rooms.get(id);
        room.add(player);
        room.add(lighting);
        return room;
    }

}
