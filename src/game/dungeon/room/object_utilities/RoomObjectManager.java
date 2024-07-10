package game.dungeon.room.object_utilities;

import java.awt.Component;
import java.awt.Graphics2D;

import game.dungeon.mechanics.CollisionHandler;
import game.dungeon.mechanics.HeightHandler;
import game.dungeon.room.entity.Player;
import game.game_components.GameComponent;

public class RoomObjectManager extends GameComponent {
    private Player player;
    private CollisionHandler collisionHandler;
    private HeightHandler heightHandler;

    public RoomObjectManager(int width, int height, Player player, CollisionHandler collisionHandler,
            HeightHandler heightHandler) {
        super(width, height);
        this.player = player;
        this.collisionHandler = collisionHandler;
        this.heightHandler = heightHandler;
    }

    public void drawComponent(Graphics2D g2d) {
    }

    public void update() {
        objectCollisions();
        objectInteractions();
        playerStairInteraction();
    }

    private void objectCollisions() {
        collisionHandler.handleCollision(player);
        for (Component object : getComponents()) {
            if (CollisionHandler.collides(player, (RoomObject) object)) {
                CollisionHandler.handleCollision(player, (RoomObject) object);
            }
        }
    }

    private void objectInteractions() {
        for (Component object : getComponents()) {
            if (player.interacts((RoomObject) object)) {
                player.setInteractable((RoomObject) object);
                return;
            }
        }
        player.setInteractable(null);
    }

    private void playerStairInteraction() {
        int temp = heightHandler.onStair(player);
        if (temp == -1) {
            collisionHandler.handleCollision(player);
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
        }
    }
}
