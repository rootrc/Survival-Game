package core.dungeon.dungeon_ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import core.Game;
import core.game_components.GameComponent;

public class WarningDisplay extends GameComponent {
    private static final int FONT_HEIGHT = 56;
    public static int textSpeed = 1;
    private static Font font;
    private String message1;
    private String message2;
    private int speed;

    public WarningDisplay() {
        super(Game.SCREEN_WIDTH, 2 * FONT_HEIGHT);
        setLocation(Game.SCREEN_WIDTH, (Game.SCREEN_HEIGHT - 2 * FONT_HEIGHT) / 2);
        message1 = "";
        message2 = "";
        if (font == null) {
            try {
                font = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/big_pixel.otf"))
                        .deriveFont((float) FONT_HEIGHT);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FontFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if (getX() >= Game.SCREEN_WIDTH) {
            return;
        }
        int stringWidth = Math.max(getFontMetrics(font).stringWidth(message1), getFontMetrics(font).stringWidth(message2));
        if ((Game.SCREEN_WIDTH - stringWidth) / 2 <= getX() + stringWidth / 8
        && getX() - stringWidth / 8 <= (Game.SCREEN_WIDTH - stringWidth) / 2) {
            speed = textSpeed;
        } else {
            speed = 32;
        }
        moveX(speed);
    }

    @Override
    public void drawComponent(Graphics2D g2d) {
        g2d.setFont(font);
        g2d.setColor(new Color(255, 39, 0));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
        (float) Math.max(0, Math.min(1, 0.8))));
        int stringWidth = Math.max(getFontMetrics(font).stringWidth(message1), getFontMetrics(font).stringWidth(message2));
        g2d.drawString(message1, (stringWidth - getFontMetrics(font).stringWidth(message1)) / 2, FONT_HEIGHT);
        g2d.drawString(message2, (stringWidth - getFontMetrics(font).stringWidth(message2)) / 2, 2 * FONT_HEIGHT);
    }

    private void setMessage(String message1, String message2) {
        if (textSpeed == 0) {
            return;
        }
        this.message1 = message1.toUpperCase();
        this.message2 = message2.toUpperCase();
        setX(-getFontMetrics(font).stringWidth(message1));
    }

    private void setMessage(String message) {
        setMessage(message, "");
    }

    public void starting() {
        setMessage("explore the cave", "find the bottom");
    }

    public void oneHealthWarning() {
        setMessage("one health!");
    }
    
    public void death() {
        setMessage("you died!");
    }
    
    public void oneMinuteWarning() {
        setMessage("one minute left");
    }
    
    public void timeOut() {
        setMessage("time's up!");
    }
}
