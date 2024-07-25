package game.dungeon.room.object_utilities;

import java.awt.Component;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Comparator;

import game.dungeon.mechanics.CollisionChecker;
import game.dungeon.mechanics.CollisionHandler;
import game.dungeon.mechanics.HeightHandler;
import game.dungeon.room.entity.Player;
import game.dungeon.room.object.Ladder;
import game.dungeon.room.tile.TileGrid;
import game.game_components.GameComponent;

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
                if (a.getY() + a.getHitBox().getY() < b.getY() + b.getHitBox().getY()) {
                    return 1;
                }
                if (a.getY() + a.getHitBox().getY() > b.getY() + b.getHitBox().getY()) {
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
        objectCollisions();
        objectInteractions();
        playerStairInteraction();
    }

    private void objectCollisions() {
        collisionChecker.handleCollision(player);
        for (Component object : getComponents()) {
            if (object == player) {
                continue;
            }
            if (CollisionHandler.collides(player, (RoomObject) object)) {
                CollisionHandler.handleCollision(player, (RoomObject) object);
            }
        }
    }

    private void objectInteractions() {
        player.clearInteractable();
        for (Component object : getComponents()) {
            if (object == player) {
                continue;
            }
            if (player.interacts((RoomObject) object)) {
                player.addInteractable((RoomObject) object);
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
