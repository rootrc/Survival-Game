package core.dungeon.room.object_utilities;

import java.awt.Component;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Comparator;

import core.dungeon.mechanics.HeightHandler;
import core.dungeon.mechanics.collision.CollisionChecker;
import core.dungeon.mechanics.collision.CollisionHandler;
import core.dungeon.room.entity.Player;
import core.dungeon.room.object.Ladder;
import core.dungeon.room.tile.TileGrid;
import core.game_components.GameComponent;

public class RoomObjectManager extends GameComponent {
    private Player player;
    private CollisionChecker collisionChecker;
    private HeightHandler heightHandler;
    private int ladderUpCnt;
    private int ladderDownCnt;

    public RoomObjectManager(Player player, TileGrid tileGrid) {
        super(tileGrid.getWidth(), tileGrid.getHeight());
        this.player = player;
        collisionChecker = tileGrid.getCollisionChecker();
        heightHandler = tileGrid.getHeightHandler();
    }

    public void drawComponent(Graphics2D g2d) {
        // Bandaid solution, but it works
        RoomObject[] temp = Arrays.copyOf(getComponents(), getComponentCount(), RoomObject[].class);
        Arrays.sort(temp, new Comparator<RoomObject>() {
            public int compare(RoomObject a, RoomObject b) {
                if (a.isOnFloor() && !b.isOnFloor()) {
                    return 1;
                } else if (!a.isOnFloor() && b.isOnFloor()) {
                    return -1;
                }
                double ay = a.getHeight() / 2;
                double by = b.getHeight() / 2;
                if (a.getHitbox() != null) {
                    ay = a.getHitbox().getY();
                }
                if (b.getHitbox() != null) {
                    by = b.getHitbox().getY();
                }
                if (a.getY() + ay < b.getY() + by) {
                    return 1;
                }
                if (a.getY() + ay > b.getY() + by) {
                    return -1;
                }
                return 0;
            }
        });
        removeAll();
        for (RoomObject roomObject : temp) {
            add(roomObject, -1);
        }
    }

    public void update() {
        for (int i = 1; i <= Math.abs(player.getSpeedX()); i ++) {
            player.moveX((int) Math.signum(player.getSpeedX()));
            objectCollisions();
        }
        for (int i = 1; i <= Math.abs(player.getSpeedY()); i++) {
            player.moveY((int) Math.signum(player.getSpeedY()));
            objectCollisions();
        }
        objectCollisions();
        objectInteractions();
        playerStairInteraction();
    }

    private void objectCollisions() {
        collisionChecker.handleCollision(player);
        for (Component roomObject : getComponents()) {
            if (roomObject == player) {
                continue;
            }
            if (CollisionHandler.collides(player, (RoomObject) roomObject)) {
                ((RoomObject) roomObject).collides(player);
                CollisionHandler.handleCollision(player, (RoomObject) roomObject);
            }
            if (roomObject == null) {
                continue;
            }
            for (Component subRoomObject : ((RoomObject) roomObject).getComponents()) {
                ((RoomObject) subRoomObject).moveX(roomObject.getX());
                ((RoomObject) subRoomObject).moveY(roomObject.getY());
                if (CollisionHandler.collides(player, (RoomObject) subRoomObject)) {
                    ((RoomObject) subRoomObject).collides(player);
                    CollisionHandler.handleCollision(player, (RoomObject) subRoomObject);
                }
                ((RoomObject) subRoomObject).moveX(-roomObject.getX());
                ((RoomObject) subRoomObject).moveY(-roomObject.getY());
            }
        }
    }

    private void objectInteractions() {
        player.clearInteractable();
        for (Component roomObject : getComponents()) {
            if (roomObject == player) {
                continue;
            }
            if (player.interacts((RoomObject) roomObject)) {
                player.addInteractable((RoomObject) roomObject);
            }
        }
    }

    private void playerStairInteraction() {
        int temp = heightHandler.getLocation(player);
        if (temp == -1) {
            collisionChecker.handleCollision(player);
            return;
        }
        switch (temp) {
            case HeightHandler.STAIR_UP:
                player.moveY(Math.abs(player.getSpeedY()) / 2.0);
                break;
            case HeightHandler.STAIR_DOWN:
                player.moveY(-Math.abs(player.getSpeedY()) / 2.0);
                break;
            case HeightHandler.STAIR_LEFT:
                player.moveX(Math.abs(player.getSpeedX()) / 2.0);
                // player.moveY(player.getSpeedX() / 2.0);
                break;
            case HeightHandler.STAIR_RIGHT:
                player.moveX(-Math.abs(player.getSpeedX()) / 2.0);
                // player.moveY(-player.getSpeedX() / 2.0);
                break;
            case HeightHandler.SLIDE_WALL:
                if (player.getSpeedY() >= 0) {
                    player.moveY(Math.abs(player.getSpeedY()) + 2);
                } else {
                    player.moveY(Math.abs(player.getSpeedY()));
                }
                break;
        }
    }

    public void add(RoomObject roomObject) {
        if (roomObject instanceof Ladder) {
            if (((Ladder) roomObject).getDirection() == Ladder.UP_DIRECTION) {
                ladderUpCnt++;
            } else if (((Ladder) roomObject).getDirection() == Ladder.DOWN_DIRECTION) {
                ladderDownCnt++;
            }
        }
        add(roomObject, -1);
    }

    public RoomObject[] getRoomObjects() {
        return Arrays.copyOf(getComponents(), getComponentCount(), RoomObject[].class);
    }

    public int getLadderUpCnt() {
        return ladderUpCnt;
    }

    public int getLadderDownCnt() {
        return ladderDownCnt;
    }
}
