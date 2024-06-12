package game.dungeon.room.object_utilities;

import java.awt.Component;
import java.awt.Graphics2D;

import game.Game;
import game.dungeon.room.entity.Player;
import game.game_components.GameComponent;

public class RoomObjectManager extends GameComponent {
    private Player player;

    public RoomObjectManager(Player player) {
        super(4 * Game.screenWidth, 4 * Game.screenHeight);
        this.player = player;
    }

    public void drawComponent(Graphics2D g2d) {
        
    }

    public void update() {
        objectInteractions();
        for (Component object : getComponents()) {
            if (player.collides((RoomObject) object)) {
                player.collide((RoomObject) object);
            }
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
