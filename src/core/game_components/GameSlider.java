package core.game_components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

import core.utilities.ImageUtilities;

public class GameSlider extends JSlider {
    public GameSlider() {
        super(0, 2);
        setBounds(100, 100, 200, 100);
        // snapToTicks = true;
        setMajorTickSpacing(10);
        setMinorTickSpacing(1);
        setPaintTicks(true);
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(0, new JLabel("EASY"));
        labelTable.put(1, new JLabel("MEDIUM"));
        labelTable.put(2, new JLabel("HARD"));
        // setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        setLabelTable(labelTable);
        setPaintLabels(true);
        setBackground(Color.BLACK);
        // setEnabled(false);
    }

    @Override
    public void updateUI() {
        setUI(new CustomSliderUI(this));
    }

    private static class CustomSliderUI extends BasicSliderUI {

        private static final int TRACK_HEIGHT = 8;
        private static final int TRACK_WIDTH = 8;
        private static final int TRACK_ARC = 5;
        private static final Dimension THUMB_SIZE = new Dimension(20, 20);
        private final RoundRectangle2D.Float trackShape = new RoundRectangle2D.Float();

        public CustomSliderUI(JSlider b) {
            super(b);
        }

        @Override
        protected void calculateTrackRect() {
            super.calculateTrackRect();
            if (isHorizontal()) {
                trackRect.y = trackRect.y + (trackRect.height - TRACK_HEIGHT) / 2;
                trackRect.height = TRACK_HEIGHT;
            } else {
                trackRect.x = trackRect.x + (trackRect.width - TRACK_WIDTH) / 2;
                trackRect.width = TRACK_WIDTH;
            }
            trackShape.setRoundRect(trackRect.x - 1, trackRect.y, trackRect.width + 1, trackRect.height, TRACK_ARC, TRACK_ARC);
        }

        @Override
        protected void calculateThumbLocation() {
            super.calculateThumbLocation();
            if (isHorizontal()) {
                thumbRect.y = trackRect.y + (trackRect.height - thumbRect.height) / 2;
            } else {
                thumbRect.x = trackRect.x + (trackRect.width - thumbRect.width) / 2;
            }
        }

        @Override
        protected Dimension getThumbSize() {
            return THUMB_SIZE;
        }

        private boolean isHorizontal() {
            return slider.getOrientation() == JSlider.HORIZONTAL;
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            super.paint(g, c);
        }

        @Override
        public void paintTrack(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            Shape clip = g2d.getClip();

            boolean horizontal = isHorizontal();
            boolean inverted = slider.getInverted();

            // Paint shadow.
            g2d.setColor(new Color(170, 170, 170));
            g2d.fill(trackShape);
            
            // Paint track background.
            g2d.setColor(new Color(200, 200, 200));
            g2d.setClip(trackShape);
            trackShape.y += 1;
            g2d.fill(trackShape);
            trackShape.y = trackRect.y;

            g2d.setClip(clip);

            // Paint selected track.
            if (horizontal) {
                boolean ltr = slider.getComponentOrientation().isLeftToRight();
                if (ltr)
                    inverted = !inverted;
                int thumbPos = thumbRect.x + thumbRect.width / 2;
                if (inverted) {
                    g2d.clipRect(0, 0, thumbPos, slider.getHeight());
                } else {
                    g2d.clipRect(thumbPos, 0, slider.getWidth() - thumbPos, slider.getHeight());
                }

            } else {
                int thumbPos = thumbRect.y + thumbRect.height / 2;
                if (inverted) {
                    g2d.clipRect(0, 0, slider.getHeight(), thumbPos);
                } else {
                    g2d.clipRect(0, thumbPos, slider.getWidth(), slider.getHeight() - thumbPos);
                }
            }
            g2d.setColor(Color.BLUE);
            g2d.fill(trackShape);
            g2d.setClip(clip);
        }

        @Override
        public void paintThumb(Graphics g) {
            g.setColor(new Color(246, 146, 36));
            g.fillRect(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
        }

        @Override
        public void paintFocus(Graphics g) {
        }
    }
}
