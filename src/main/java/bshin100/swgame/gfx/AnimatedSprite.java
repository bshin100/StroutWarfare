package bshin100.swgame.gfx;

import bshin100.swgame.util.Direction;
import bshin100.swgame.input.Keyboard;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Animated Sprite Class
 * Created on 6/7/2018.
 * @author Brian Shin
 */
public class AnimatedSprite extends Sprite {

    protected int frameCount;
    protected int frameDelay; // frame delay 1-12 (may need adjustment)
    protected int currentFrame;
    protected int animationDirection;
    protected int totalFrames;
    protected boolean stopped;
    private List<Frame> frames = new ArrayList<Frame>();

    public AnimatedSprite(BufferedImage[] frames, int frameDelay, int startX, int startY) {
        this.frameDelay = frameDelay;
        this.stopped = true;
        for (int i = 0; i < frames.length; i++) {
            addFrame(frames[i], frameDelay);
        }
        this.frameCount = 0;
        this.frameDelay = frameDelay;
        this.currentFrame = 0;
        this.animationDirection = 1;
        this.totalFrames = this.frames.size();

        visible = true;
        this.x = startX;
        this.y = startY;
    }

    public AnimatedSprite(BufferedImage[] frames, int frameDelay, int startX, int startY, Keyboard keyboard) {
        this.frameDelay = frameDelay;
        this.stopped = true;
        for (int i = 0; i < frames.length; i++) {
            addFrame(frames[i], frameDelay);
        }
        this.frameCount = 0;
        this.frameDelay = frameDelay;
        this.currentFrame = 0;
        this.animationDirection = 1;
        this.totalFrames = this.frames.size();

        visible = true;
        d = Direction.UP;
        this.x = startX;
        this.y = startY;
        this.keyboard = keyboard;
    }

    public void update() {
        if (!stopped) {
            frameCount++;

            if (frameCount > frameDelay) {
                frameCount = 0;
                currentFrame += animationDirection;

                if (currentFrame > totalFrames - 1) {
                    currentFrame = 0;
                }
                else if (currentFrame < 0) {
                    currentFrame = totalFrames - 1;
                }
            }
        }
    }

    protected void start() {
        if (!stopped) {
            return;
        }

        if (frames.size() == 0) {
            return;
        }

        stopped = false;
    }

    protected void stop() {
        if (frames.size() == 0) {
            return;
        }

        stopped = true;
    }

    public void restart() {
        if (frames.size() == 0) {
            return;
        }

        stopped = false;
        currentFrame = 0;
    }

    public void reset() {
        this.stopped = true;
        this.frameCount = 0;
        this.currentFrame = 0;
    }

    private void addFrame(BufferedImage frame, int duration) {
        if (duration <= 0) {
            System.err.println("Invalid duration: " + duration);
            throw new RuntimeException("Invalid duration: " + duration);
        }

        frames.add(new Frame(frame, duration));
        currentFrame = 0;
    }

    public void setFrames(BufferedImage[] newFrames) {
        frames.clear();
        for (int i = 0; i < newFrames.length; i++) {
            addFrame(newFrames[i], frameDelay);
        }
        this.totalFrames = this.frames.size();
    }

    public BufferedImage getSprite() {
        return frames.get(currentFrame).getFrame();
    }

    public void flipImageY() {
        for(int i = 0; i <= frames.size() - 1; i++) {
            BufferedImage image = frames.get(i).getFrame();

            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-image.getWidth(null), 0);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            frames.set(i, new Frame(op.filter(image, null), frameDelay));
        }
    }

    public Rectangle getObjectBounds() {
        Rectangle bounds;
        int sSizeX = getSprite().getWidth(null);
        int sSizeY = getSprite().getHeight(null);

        bounds = new Rectangle(getX(), getY(), sSizeX, sSizeY);
        return bounds;
    }
}
