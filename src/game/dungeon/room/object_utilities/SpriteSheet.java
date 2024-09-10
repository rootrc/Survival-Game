package game.dungeon.room.object_utilities;

import java.awt.image.BufferedImage;

import game.utilities.ImageUtilities;

public class SpriteSheet {
    private BufferedImage[][] images;
    private int width;
    private int height;
    private int frames;
    private int frameLength;

    private int currentDirection;
    private int currentFrame;

    public SpriteSheet(BufferedImage bufferedImage, int frameCnt, int directionCnt, int frameLength) {
        this.frames = frameCnt;
        this.frameLength = frameLength;
        width = bufferedImage.getWidth() / frameCnt;
        height = bufferedImage.getHeight() / directionCnt;
        images = new BufferedImage[directionCnt][frameCnt];
        for (int r = 0; r < directionCnt; r++) {
            for (int c = 0; c < frameCnt; c++) {
                images[r][c] = ImageUtilities.getImage(bufferedImage, r, c, width, height);
            }
        }
    }
    
    public SpriteSheet(BufferedImage bufferedImage, int frames, int frameLength) {
        this(bufferedImage, frames, 1, frameLength);
    }
    
    public SpriteSheet(BufferedImage bufferedImage, int frames) {
        this(bufferedImage, frames, 1, 1);
    }

    public SpriteSheet(BufferedImage bufferedImage) {
        this(bufferedImage, 1, 1, 1);
    }

    public BufferedImage getImage() {
        return images[currentDirection][currentFrame];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setDirection(int direction) {
        currentDirection = direction;
    }

    public int getFrame() {
        return currentFrame;
    }

    public void setFrame(int frame) {
        currentFrame = frame;
    }

    public void nextFrame() {
        currentFrame = (currentFrame + 1) % frames;
    }

    private int cnt;
    public void next() {
        cnt++;
        if (cnt == frameLength) {
            cnt = 0;
            nextFrame();
        }
    }
    
    public void setFrameLength(int frameLength) {
        this.frameLength = frameLength;
    }

}
