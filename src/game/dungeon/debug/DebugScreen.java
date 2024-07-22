package game.dungeon.debug;

import java.awt.Font;
import java.awt.Graphics2D;

import game.dungeon.room.Room;
import game.dungeon.settings.KeyBinds;
import game.game_components.PopupUI;
import game.game_components.UILayer;

public class DebugScreen extends PopupUI {
    private Room room;

    public DebugScreen(UILayer UILayer, Room room) {
        super(UILayer, 640, 640, 4);
        this.room = room;
        getInputMap(2).put(KeyBinds.debug, "close");
    }

    public void updateRoom(Room room) {
        this.room = room;
    }

    @Override
    public void drawComponent(Graphics2D g2d) {
        super.drawComponent(g2d);
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        g2d.drawString(new StringBuilder("Room Id: ").append(room.getId()).toString(), 64, 64);
        g2d.drawString(new StringBuilder("Entities: ").append(room.getEntityCount()).toString(), 64, 64 + 32);
        g2d.drawString(new StringBuilder("Particles: ").append(room.getParticleCount()).toString(), 64, 64 + 2 * 32);
        g2d.drawString(new StringBuilder("Player Location: ").append(room.getPlayer().getX()).append(", ")
                .append(room.getPlayer().getY()).toString(), 64, 64 + 3 * 32);
    }

}
