package core.dungeon;

import java.util.HashMap;

import core.dungeon.room.objects.Ladder;

public class RoomConnecter {
    HashMap<Ladder, Integer> ladderToRoom;
    HashMap<Ladder, Ladder> ladderToLadder;

    public RoomConnecter() {
        ladderToRoom = new HashMap<>();
        ladderToLadder = new HashMap<>();
    }

    public void addLadder(Ladder ladder, int id) {
        ladderToRoom.put(ladder, id);
    }

    public void addLadder(Ladder ladder, Ladder ladder2) {
        ladderToLadder.put(ladder, ladder2);
    }

    public int getRoomId(Ladder ladder) {
        if (!ladderToRoom.containsKey(ladder)) {
            return -1;
        }
        return ladderToRoom.get(ladder);
    }

    public Ladder getLadder(Ladder ladder) {
        if (!ladderToLadder.containsKey(ladder)) {
            return null;
        }
        return ladderToLadder.get(ladder);
    }
}
