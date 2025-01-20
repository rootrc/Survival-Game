package core.game_panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;

import core.Game;
import core.dungeon.dungeon_ui.WarningDisplay;
import core.dungeon.settings.DiffSettings;
import core.dungeon.settings.KeyBinds;
import core.game_components.GamePanel;
import core.game_components.GameSlider;
import core.game_components.UIButton;
import core.game_components.UILayer;
import core.utilities.ActionUtilities;
import core.utilities.ImageUtilities;

public class Options extends GamePanel {
    private static Font font;
    private GameSlider difficulty;
    private GameSlider audio;
    private GameSlider screenShakeDuration;
    private GameSlider screenShakeStrength;
    private GameSlider textSpeed;

    private final Action saveSettings = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            switch (difficulty.getValue()) {
                case 0:
                    DiffSettings.setEasy();
                    break;
                case 1:
                    DiffSettings.setMedium();
                    break;
                case 2:
                    DiffSettings.setHard();
                    break;
            }
            switch (audio.getValue()) {
                case 0:
                    Game.audioVolume.setValue(-80f);
                    break;
                case 1:
                    Game.audioVolume.setValue(-25f);
                    break;
                case 2:
                    Game.audioVolume.setValue(-20f);
                    break;
                case 3:
                    Game.audioVolume.setValue(-15f);
                    break;
                case 4:
                    Game.audioVolume.setValue(-10f);
                    break;
                case 5:
                    Game.audioVolume.setValue(0f);
                    break;
            }
            Game.screenShakeDurationMulti = screenShakeDuration.getValue() / 4.0;
            Game.screenShakeStrengthMulti = screenShakeStrength.getValue() / 4.0;
            WarningDisplay.textSpeed = textSpeed.getValue();
        }
    };

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Difficulty", 108, 98);
        g2d.drawString("Audio Level", 108, 228);
        g2d.drawString("Screen Shake Duration", 108, 358);
        g2d.drawString("Screen Shake Strength", 108, 488);
        g2d.drawString("Text Speed", 108, 618);
    }

    public Options(Game game, UILayer UILayer) {
        super(game, UILayer);
        getInputMap(2).put(KeyBinds.ESC, "exit");
        getActionMap().put("exit", UILayer
                .createAndOpenConfirmUI(ActionUtilities.combineActions(game.changePanel("mainMenu"), saveSettings)));
        add(new UIButton(game.changePanel("credits"), new Rectangle(704, 672, 288, 64),
                ImageUtilities.getImage("UI", "CreditsButton")));
        difficulty = new GameSlider(new String[] { "EASY", "MEDIUM", "HARD" },
                new Rectangle(86, 116, 528, 50));
        difficulty.setValue(1);
        audio = new GameSlider(new String[] { "0", "", "", "", "", "5" },
                new Rectangle(100, 246, 500, 50));
        audio.setValue(4);
        screenShakeDuration = new GameSlider(new String[] { "0", "", "", "", "1" },
                new Rectangle(100, 376, 500, 50));
        screenShakeDuration.setValue(4);
        screenShakeStrength = new GameSlider(new String[] { "0", "", "", "", "1" },
                new Rectangle(100, 506, 500, 50));
        screenShakeStrength.setValue(4);
        textSpeed = new GameSlider(new String[] { "0", " ", "2", " ", "4" },
                new Rectangle(100, 636, 500, 50));
        textSpeed.setValue(1);
        add(difficulty);
        add(audio);
        add(screenShakeDuration);
        add(screenShakeStrength);
        add(textSpeed);
        saveSettings.actionPerformed(null);
        if (font == null) {
            try {
                font = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/big_pixel.otf")).deriveFont(32.0f);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FontFormatException e) {
                e.printStackTrace();
            }
        }
    }
}