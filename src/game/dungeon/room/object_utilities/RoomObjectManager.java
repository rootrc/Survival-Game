package game.dungeon.room.object_utilities;

import java.awt.Component;
import java.awt.Graphics2D;

import game.dungeon.mechanics.CollisionHandler;
import game.dungeon.room.entity.Player;
import game.game_components.GameComponent;

public class RoomObjectManager extends GameComponent {
    private Player player;
    private CollisionHandler collisionHandler;

    public RoomObjectManager(int width, int height, Player player, CollisionHandler collisionHandler) {
        super(width, height);
        this.player = player;
        this.collisionHandler = collisionHandler;
    }

    public void drawComponent(Graphics2D g2d) {    
    }

    public void update() {
        objectInteractions();
        for (Component object : getComponents()) {
            if (CollisionHandler.collides(player, (RoomObject) object)) {
                CollisionHandler.handleCollision(player, (RoomObject) object);
            }
            collisionHandler.handleCollision(player);
            // if (!player.getCollision().canMove(player, 0, 0)) {
            //     System.out.println("hi");
            // }
        }
    }

    public void objectInteractions() {
        for (Component object : getComponents()) {
            if (player.interacts((RoomObject) object)) {
                player.setInteractable((RoomObject) object);
                return;
            }
        }
        player.setInteractable(null);
    }
}
