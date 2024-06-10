package game.dungeon.room.object_utilities;

import java.awt.Rectangle;

import game.game_components.GamePanel;

public class CollisionBox extends Rectangle {
    public CollisionBox(double x, double y, double width, double height) {
        super((int) (x * GamePanel.TILESIZE), (int) (y * GamePanel.TILESIZE),
                (int) (width * GamePanel.TILESIZE), (int) (height * GamePanel.TILESIZE));
    }

}
