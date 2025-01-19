package core.dungeon.debug;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import core.Game;
import core.dungeon.room.Room;
import core.dungeon.settings.KeyBinds;
import core.game_components.PopupUI;
import core.game_components.UILayer;

public class DebugScreen extends PopupUI {
    private Room room;

    public DebugScreen(UILayer UILayer) {
        super(UILayer, 640, 640, 4);
        getInputMap(2).put(KeyBinds.DEBUG, "close");

        getInputMap(2).put(KeyBinds.LIGHTING, "lighting");
        getActionMap().put("lighting", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                Game.LIGHTING ^= true;
            }            
        });
        getInputMap(2).put(KeyBinds.HITBOXES, "hitboxes");
        getActionMap().put("hitboxes", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                Game.HITBOXES ^= true;
            }            
        });

        getInputMap(2).put(KeyBinds.GOD_MODE, "godMode");
        getActionMap().put("godMode", new AbstractAction() {
            private int temp = 1000;
            public void actionPerformed(ActionEvent e) {
                room.getPlayer().getStats().addHealthRegenPerSecond(temp);
                temp *= -1;
            }            
        });

        getInputMap(2).put(KeyBinds.TIMER, "timer");
        getActionMap().put("timer", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // Game.HITBOXES ^= true;
            }            
        });
    }

    public void updateRoom(Room room) {
        this.room = room;
    }

    @Override
    public void drawComponent(Graphics2D g2d) {
        super.drawComponent(g2d);
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        g2d.drawString(new StringBuilder("Room Id: ").append(room.getId()).toString(), 64, 64);
        g2d.drawString(new StringBuilder("Entities: ").append(room.getEntityCnt()).toString(), 64, 64 + 32);
        g2d.drawString(new StringBuilder("Player Location: ").append(room.getPlayer().getX()).append(", ")
                .append(room.getPlayer().getY()).toString(), 64, 64 + 4 * 32);

        g2d.drawString("The following commands only work on this screen", 64, 64 + 6 * 32);
        g2d.drawString("Press L to toggle lighting", 64, 64 + 7 * 32);
        g2d.drawString("Press H to toggle hitboxes", 64, 64 + 8 * 32);
        g2d.drawString("Press G to toggle god mode (infinite regen)", 64, 64 + 8 * 32);
    }
}