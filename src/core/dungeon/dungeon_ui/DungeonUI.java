package core.dungeon.dungeon_ui;

import java.awt.Graphics2D;

import core.Game;
import core.game_components.GameComponent;

public class DungeonUI extends GameComponent {
    // private HealthBar healthBar;
    private Timer timer;
    private MiniMap miniMap;

    public DungeonUI(HealthBar healthBar, Timer timer, MiniMap miniMap) {
        super(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        // this.healthBar = healthBar;
        this.timer = timer;
        this.miniMap = miniMap;
        add(healthBar);
        add(timer);
        add(miniMap);
    }

    public Timer getTimer() {
        return timer;
    }

    public MiniMap getMiniMap() {
        return miniMap;
    }

    public void update() {
    }

    public void drawComponent(Graphics2D g2d) {
    }
}
