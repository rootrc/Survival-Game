package game.dungeon.room.object_utilities;

import java.util.HashMap;

import game.dungeon.room.entity.Entity;

public class DirectionUtilities {
    private static final HashMap<Integer, Integer> HashToDirection = new HashMap<>() {
        {
            put(-1, 0);
            put(9, 1);
            put(10, 2);
            put(11, 3);
            put(1, 4);
            put(-9, 5);
            put(-10, 6);
            put(-11, 7);
        }
    };
    private static final HashMap<Integer, Integer> directionToHash = new HashMap<>() {
        {
            put(0, -1);
            put(1, 9);
            put(2, 10);
            put(3, 11);
            put(4, 1);
            put(5, -9);
            put(6, -10);
            put(7, -11);
        }
    };

    public static int getDirection(double speedX, double speedY) {
        return HashToDirection.get((int) (10 * getDirectionOfSpeed(speedX) + getDirectionOfSpeed(speedY)));
    }

    public static int getDirection(Entity entity) {
        if (!entity.isMoving()) {
            return entity.getDirection();
        }
        return getDirection(entity.getSpeedX(), entity.getSpeedY());
    }

    public static int getHash(int direction) {
        return directionToHash.get(direction);
    }

    public static int getHash(Entity entity) {
        return getHash(entity.getDirection());
    }

    public static int getXDirection(int direction) {
        return (int) Math.round((getHash(direction) + getDirectionOfSpeed(getHash(direction))) / 10);
    }

    public static int getXDirection(Entity entity) {
        return getXDirection(entity.getDirection());
    }

    public static int getYDirection(int direction) {
        return (getHash(direction) + getDirectionOfSpeed(getHash(direction))) % 10
                - getDirectionOfSpeed(getHash(direction));
    }

    public static int getYDirection(Entity entity) {
        return getYDirection(entity.getDirection());
    }

    private static int getDirectionOfSpeed(double speed) {
        if (speed == 0) {
            return 0;
        }
        return (int) (speed / Math.abs(speed));
    }
}
