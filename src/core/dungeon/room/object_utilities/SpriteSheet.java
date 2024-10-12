package core.dungeon.room.object_utilities;

import java.awt.image.BufferedImage;

import core.utilities.ImageUtilities;

public class SpriteSheet {
    private BufferedImage[][] images;
    private int width;
    private int height;
    private int frameCnt;
    private int typeCnt;
    private int frameLength;

    private int type;
    private int currentFrame;

    public SpriteSheet(BufferedImage bufferedImage, int frameCnt, int typeCnt, int frameLength) {
        this.frameCnt = frameCnt;
        this.typeCnt = typeCnt;
        this.frameLength = frameLength;
        width = bufferedImage.getWidth() / frameCnt;
        height = bufferedImage.getHeight() / typeCnt;
        images = new BufferedImage[typeCnt][frameCnt];
        for (int r = 0; r < typeCnt; r++) {
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
        return images[type][currentFrame];
    }

    public void set(SpriteSheet spriteSheet) {
        spriteSheet.setFrame(currentFrame);
        spriteSheet.setType(type);
        spriteSheet.setCnt(cnt);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTypeCnt() {
        return typeCnt;
    }
    
    public void setType(int type) {
        this.type = (type + typeCnt) % typeCnt;
    }

    public int getType() {
        return type;
    }

    public int getFrame() {
        return currentFrame;
    }
    
    public int getFrameCnt() {
        return frameCnt;
    }

    public void setFrame(int frame) {
        currentFrame = (frame + frameCnt) % frameCnt;
        cnt = 0;
    }

    public void previousFrame() {
        currentFrame = (currentFrame - 1 + frameCnt) % frameCnt;
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

    public void prev() {
        cnt++;
        if (cnt == frameLength) {
            cnt = 0;
            previousFrame();
        }
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
    
    public void setFrameLength(int frameLength) {
        this.frameLength = frameLength;
    }

}
