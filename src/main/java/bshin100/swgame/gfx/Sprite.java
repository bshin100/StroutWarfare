package bshin100.swgame.gfx;

import bshin100.swgame.util.Direction;
import bshin100.swgame.Game;
import bshin100.swgame.input.Keyboard;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Sprite Object Class
 * Created on 5/25/2018.
 * @author Brian Shin
 */
public class Sprite {

    boolean visible;
    private BufferedImage image;
    protected int x;
    protected int y;
    Direction d;
    protected Keyboard keyboard;

    public Sprite() {}

    public Sprite(BufferedImage image, int startX, int startY) {
        visible = true;
        this.image = image;
        this.x = startX;
        this.y = startY;
    }

    public void update() {
    }

    protected void move(int xd, int yd) {

        if (xd != 0 && yd != 0) {
            move(0, yd);
            move(xd, 0);
            return;
        }

        if (xd > 0) setDirection(Direction.RIGHT);
        if (xd < 0) setDirection(Direction.LEFT);
        if (yd > 0) setDirection(Direction.DOWN);
        if (yd < 0) setDirection(Direction.UP);

        if(!outOfBounds()) {
            setX(x += xd);
            setY(y += yd);
        } else if((getCollDir() != getDirection())) {
            setX(x += xd);
            setY(y += yd);
        }
    }

    public Rectangle getObjectBounds() {
        Rectangle bounds;
        int sSizeX = getImage().getWidth(null);
        int sSizeY = getImage().getHeight(null);

        bounds = new Rectangle(getX(), getY(), sSizeX, sSizeY);
        return bounds;
    }

    /**
     * Check if Sprite is out of game bounds.
     * @return Returns boolean true or false.
     */
    protected boolean outOfBounds() {
        return (getX() < 0 || getY() < 0 || getX() > (Game.WIDTH - 45) || getY() > (Game.HEIGHT - 45));
    }

    /**
     * @return Returns direction of collision with game bounds.
     */
    protected Direction getCollDir() {
        if(getX() < 0) {
            return Direction.LEFT;
        } else if(getY() < 0) {
            return Direction.UP;
        } else if(getX() > (Game.WIDTH - 45)) {
            return Direction.RIGHT;
        } else if(getY() > (Game.HEIGHT - 45)) {
            return Direction.DOWN;
        } else {
            return Direction.NONE;
        }
    }

    public void die() {
        visible = false;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void flipImageY() {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    protected void setDirection(Direction d) {
        this.d = d;
    }

    protected Direction getDirection() {
        return d;
    }
}