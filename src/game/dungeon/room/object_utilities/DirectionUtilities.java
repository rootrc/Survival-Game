package game.dungeon.room.object_utilities;

import java.util.HashMap;

import game.dungeon.room.entity.Entity;

public class DirectionUtilities {
    private static final HashMap<Integer, Integer> HashToDirection8 = new HashMap<>() {
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
    private static final HashMap<Integer, Integer> direction8ToHash = new HashMap<>() {
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

    private DirectionUtilities() {
    }

    public static int getDirection(Entity entity) {
        if (!entity.isMoving()) {
            return entity.getDirection();
        }
        return getDirection(entity.getSpeedX(), entity.getSpeedY());
    }

    private static int getDirection(double speedX, double speedY) {
        return HashToDirection8.get((int) (10 * Math.signum(speedX) + Math.signum(speedY)));
    }

    private static int getHash(int direction) {
        return direction8ToHash.get(direction);
    }

    public static int getXMovement(int direction) {
        return (int) Math.round((getHash(direction) + Math.signum(getHash(direction))) / 10);
    }

    public static int getYMovement(int direction) {
        return (getHash(direction) + (int) Math.signum(getHash(direction))) % 10
                - (int) Math.signum(getHash(direction));
    }

    public static int getMovingDirection(boolean movingUp, boolean movingLeft, boolean movingDown,
            boolean movingRight) {
        if (movingUp && movingDown || movingLeft && movingRight) {
            return -1;
        } else if (movingRight && movingUp) {
            return 1;
        } else if (movingDown && movingRight) {
            return 3;
        } else if (movingLeft && movingDown) {
            return 5;
        } else if (movingUp && movingLeft) {
            return 7;
        } else if (movingUp) {
            return 0;
        } else if (movingRight) {
            return 2;
        } else if (movingDown) {
            return 4;
        } else if (movingLeft) {
            return 6;
        }
        return -1;
    }

    public static int reverseDirection(int direction) {
        return (direction + 4) % 8;
    }
}
