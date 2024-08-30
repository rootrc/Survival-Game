package game.dungeon.mechanics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.Action;

import game.dungeon.settings.KeyBinds;
import game.game_components.GameButton;
import game.game_components.PopupUI;
import game.game_components.UILayer;

public class SkillTree extends PopupUI {
    private static final int SKILL_SIZE = 64;

    public SkillTree(UILayer UILayer) {
        super(UILayer, 800, 640, 8);
        getInputMap(2).put(KeyBinds.openSkillTree, "close");
        add(new SkillButton(null, new Rectangle((getWidth() - SKILL_SIZE) / 2 + 3 * SKILL_SIZE, (getHeight() - SKILL_SIZE) / 2, SKILL_SIZE, SKILL_SIZE)));
        add(new SkillButton(null, new Rectangle((getWidth() - SKILL_SIZE) / 2 - 3 * SKILL_SIZE, (getHeight() - SKILL_SIZE) / 2, SKILL_SIZE, SKILL_SIZE)));
        add(new SkillButton(null, new Rectangle((getWidth() - SKILL_SIZE) / 2, (getHeight() - SKILL_SIZE) / 2  + 3 * SKILL_SIZE, SKILL_SIZE, SKILL_SIZE)));
        add(new SkillButton(null, new Rectangle((getWidth() - SKILL_SIZE) / 2, (getHeight() - SKILL_SIZE) / 2  - 3 * SKILL_SIZE, SKILL_SIZE, SKILL_SIZE)));
        // TODO
    }

    @Override
    public void drawComponent(Graphics2D g2d) {
        super.drawComponent(g2d);
        g2d.drawRect((getWidth() - SKILL_SIZE) / 2, (getHeight() - SKILL_SIZE) / 2, SKILL_SIZE, SKILL_SIZE);
    }

    private class SkillButton extends GameButton {
        public SkillButton(Action action, Rectangle rect) {
            super(action, rect);
        }

        private ArrayList<SkillButton> nextSkill;
        
        // TEMP
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        
    }
}