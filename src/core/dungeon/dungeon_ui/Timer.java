package core.dungeon.dungeon_ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import core.Game;
import core.game_components.GameComponent;
import core.utilities.Easing;

public class Timer extends GameComponent {
    private static Font font;
    private static final int framesToSwitchTime = Game.UPS / 4;

    private BufferedImage image;

    private int startNanoTime;
    private int startTime;

    private int timeInSeconds;
    private String prevDisplayTime;
    private String displayTime;

    private int analogCnt;
    private int blinkingCnt;
    private WarningDisplay warningDisplay;

    public Timer(int startTime, WarningDisplay warningDisplay) {
        super(148, 66);
        setLocation((Game.SCREEN_WIDTH - getWidth()) / 2, 32);
        this.startTime = startTime;
        this.warningDisplay = warningDisplay;
        startNanoTime = (int) (System.nanoTime() / 1000000000);
        displayTime = String.format("%02d:%02d", ((startTime / 60) % 60), ((startTime) % 60));
        if (font != null) {
            return;
        }
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/big_pixel.otf")).deriveFont(40.0f);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        timeInSeconds = Math.max(startTime - (int) (System.nanoTime() / 1000000000 - startNanoTime), 0);
        if (!displayTime.equals(prevDisplayTime)) {
            prevDisplayTime = displayTime;
        }
        displayTime = String.format("%02d:%02d", ((timeInSeconds / 60) % 60), ((timeInSeconds) % 60));
        buildImage();
    }

    @Override
    public void drawComponent(Graphics2D g2d) {
        g2d.drawImage(image, 0, 0, null);
    }

    private void buildImage() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        image = gd.getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(), Transparency.BITMASK);
        Graphics2D g2d = image.createGraphics();
        BufferedImage number = buildNumber(g2d.getFontMetrics(font));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(2, 2, number.getWidth() + 8, number.getHeight() + 8);
        g2d.drawImage(number, number.getWidth() - number.getWidth() + 5, 5, null);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(1, 1, number.getWidth() + 10, number.getHeight() + 10);
    }

    private BufferedImage buildNumber(FontMetrics fontMetrics) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        BufferedImage image = gd.getDefaultConfiguration().createCompatibleImage(fontMetrics.stringWidth(displayTime),
                fontMetrics.getHeight(), Transparency.BITMASK);
        Graphics2D g2d = image.createGraphics();
        g2d.setFont(font);
        if (timeInSeconds <= 3 || (timeInSeconds <= 60 && timeInSeconds % 2 == 0)) {
            g2d.setColor(Color.RED);
        } else if (blinkingCnt != 0) {
            blinkingCnt--;
            if (blinkingCnt < 32 || (blinkingCnt / 16) % 2 == 0) {
                g2d.setColor(Color.RED);
                if (blinkingCnt == 8) {
                    blinkingCnt = 0;
                }
            } else {
                g2d.setColor(Color.WHITE);
            }
        } else {
            g2d.setColor(Color.WHITE);
        }
        int y0 = fontMetrics.getHeight();
        if (displayTime.equals(prevDisplayTime) && analogCnt == -1) {
            g2d.drawString(displayTime, 0, y0);
            return image;
        }
        if (!displayTime.equals(prevDisplayTime)) {
            if (timeInSeconds == 60) {
                warningDisplay.oneMinuteWarning();
            } else if (timeInSeconds % 60 == 59 && timeInSeconds != 59) {
                blinkingCnt = 144;
            }
            analogCnt = framesToSwitchTime;
        }
        String displayTimePlusASecond = String.format("%02d:%02d", (((timeInSeconds + 1) / 60) % 60),
                ((timeInSeconds + 1) % 60));
        for (int i = displayTime.length() - 1; i > 0; i--) {
            if (displayTime.charAt(i - 1) == ':' || displayTime.charAt(i - 1) != displayTimePlusASecond.charAt(i - 1)) {
                continue;
            }
            int displayTimeY = y0
                    + (int) (Easing.easeInQuad(1 - (double) analogCnt / framesToSwitchTime) * fontMetrics.getHeight());
            int displayTimePlusASecondY = y0
                    - (int) (Easing.easeOutQuad((double) analogCnt / framesToSwitchTime) * fontMetrics.getHeight());
            if (i == 2 || i == 4) {
                if (i == 2) {
                    i++;
                }
                g2d.drawString(displayTime.substring(0, i), 0, y0);

                g2d.drawString(displayTimePlusASecond.substring(i, displayTime.length()),
                        g2d.getFontMetrics().stringWidth(displayTime.substring(0, i)), displayTimeY);

                g2d.drawString(displayTime.substring(i, displayTime.length()),
                        g2d.getFontMetrics().stringWidth(displayTime.substring(0, i)), displayTimePlusASecondY);
            } else {
                g2d.drawString(displayTime.substring(0, i), 0, y0);
                g2d.drawString(displayTime.substring(2, 3), 54, y0);

                g2d.drawString(displayTimePlusASecond.substring(i, 2),
                        g2d.getFontMetrics().stringWidth(displayTime.substring(0, i)), displayTimeY);
                g2d.drawString(displayTimePlusASecond.substring(3, displayTime.length()),
                        g2d.getFontMetrics().stringWidth(displayTime.substring(0, 3)), displayTimeY);

                g2d.drawString(displayTime.substring(i, 2),
                        g2d.getFontMetrics().stringWidth(displayTime.substring(0, i)), displayTimePlusASecondY);
                g2d.drawString(displayTime.substring(3, displayTime.length()),
                        g2d.getFontMetrics().stringWidth(displayTime.substring(0, 3)), displayTimePlusASecondY);
            }
            break;
        }
        analogCnt--;
        return image;
    }

    public int getTime() {
        return timeInSeconds;
    }

    public boolean isZero() {
        return timeInSeconds == 0;
    }

    public int getStartTime() {
        return startTime;
    }

    public void addStartTime(int delta) {
        startTime += delta;
    }

}