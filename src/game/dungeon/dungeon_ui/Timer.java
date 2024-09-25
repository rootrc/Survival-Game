package game.dungeon.dungeon_ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import game.Game;
import game.game_components.GameComponent;

public class Timer extends GameComponent {
    private int startNanoTime;
    private int startTime;

    private int timeInSeconds;
    private String displayTime;
    private static Font font;

    public Timer() {
        super(160, 96);
        setLocation((Game.SCREEN_WIDTH - getWidth()) / 2, 16);
        setTime(0);
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/amiga.ttf")).deriveFont(36.0f);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        timeInSeconds = Math.max(startTime - (int) (System.nanoTime() / 1000000000 - startNanoTime), 0);
        displayTime = String.format("%01d:%02d", ((timeInSeconds / 60) % 60), ((timeInSeconds) % 60));
    }

    @Override
    public void drawComponent(Graphics2D g2d) {
        if (Game.DEBUG) {
            return;
        }
        // TEMP
        g2d.setColor(Color.blue);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        g2d.setColor(Color.white);
        g2d.setFont(font);
        g2d.drawString(displayTime, (getWidth() - 120) / 2, (getHeight() + g2d.getFontMetrics().getHeight()) / 2);
    }

    public void setTime(int startTime) {
        this.startTime = startTime;
        startNanoTime = (int) (System.nanoTime() / 1000000000);
        displayTime = String.format("%01d:%02d", ((startTime / 60) % 60), ((startTime) % 60));
    }

    public boolean isFinished() {
        return timeInSeconds == 0;
    }
}