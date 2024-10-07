package core.dungeon.room.object_utilities;

import java.awt.image.BufferedImage;

import core.utilities.ImageUtilities;

public class SpriteSheet {
    private BufferedImage[][] images;
    private int width;
    private int height;
    private int frameCnt;
    private int directionCnt;
    private int frameLength;

    private int currentDirection;
    private int currentFrame;

    public SpriteSheet(BufferedImage bufferedImage, int frameCnt, int directionCnt, int frameLength) {
        this.frameCnt = frameCnt;
        this.directionCnt = directionCnt;
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

    public int getDirectionCnt() {
        return directionCnt;
    }
    
    public void setDirection(int direction) {
        currentDirection = direction;
    }

    public int getDirection() {
        return currentDirection;
    }

    public int getFrame() {
        return currentFrame;
    }
    
    public int getFrameCnt() {
        return frameCnt;
    }

    public void setFrame(int frame) {
        currentFrame = frame;
        cnt = 0;
    }

    public void nextFrame() {
        currentFrame = (currentFrame + 1) % frameCnt;
    }

    public int getFrameLength() {
        return frameLength;
    }

    private int cnt;
    public void next() {
        cnt++;
        if (cnt == frameLength) {
            cnt = 0;
            nextFrame();
        }
    }

    public boolean isStartofFrame() {
        return cnt == 0;
    }
    
    public void setFrameLength(int frameLength) {
        this.frameLength = frameLength;
    }

}
