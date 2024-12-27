package core.dungeon.dungeon_ui;

import java.awt.Graphics2D;

import core.Game;
import core.game_components.GameComponent;

public class DungeonUI extends GameComponent {
    // private HealthBar healthBar;
    private Timer timer;
    private MiniMap miniMap;
    private WarningDisplay warningDisplay;

    public DungeonUI(HealthBar healthBar, Timer timer, MiniMap miniMap, WarningDisplay warningDisplay) {
        super(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        // this.healthBar = healthBar;
        this.timer = timer;
        this.miniMap = miniMap;
        this.warningDisplay = warningDisplay;
        add(healthBar);
        add(timer);
        add(miniMap);
        add(warningDisplay);
    }

    public Timer getTimer() {
        return timer;
    }

    public MiniMap getMiniMap() {
        return miniMap;
    }

    public WarningDisplay getWarningDisplay() {
        return warningDisplay;
    }

    public void update() {
    }

    public void drawComponent(Graphics2D g2d) {
    }
}