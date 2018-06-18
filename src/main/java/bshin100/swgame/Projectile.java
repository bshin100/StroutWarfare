package bshin100.swgame;

import bshin100.swgame.gfx.Sprite;
import bshin100.swgame.util.Direction;
import bshin100.swgame.util.Kinematics;
import bshin100.swgame.util.Vector2;

import java.awt.*;

/**
 * Projectile
 * Created on 6/12/2018.
 * @author Brian Shin
 */
public class Projectile extends Sprite {

    private Direction direction;

    public Projectile(int startX, int startY, Direction direction) {
        super();
        this.x = startX;
        this.y = startY;
        this.direction = direction;
    }

    public void update() {
        if(direction == Direction.RIGHT) {
            move(6, 0);
        } else if(direction == Direction.LEFT) {
            move(-6, 0);
        } else if(direction == Direction.RIGHT23) {
            Vector2 velocity = Kinematics.getVelocity(new Vector2(5.523,-2.344), new Vector2(0, 0), 1); // Rays. Sorry physics.
            move((int)velocity.x, (int)velocity.y);
        } else if(direction == Direction.LEFT23) {
            Vector2 velocity = Kinematics.getVelocity(new Vector2(-5.523,-2.344), new Vector2(0, 0), 1); // Rays. Sorry physics.
            move((int)velocity.x, (int)velocity.y);
        }
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

        if(!outOfBounds() && !Game.collision(this)) {
            setX(x += xd);
            setY(y += yd);
        }
    }

    public Rectangle getObjectBounds() {
        Rectangle bounds;
        //int sSizeX = getImage().getWidth(null);
        //int sSizeY = getImage().getHeight(null);
        //bounds = new Rectangle(getX(), getY(), sSizeX, sSizeY);
        bounds = new Rectangle(getX(), getY(), 16, 16);
        return bounds;
    }

    protected boolean outOfBounds() {
        return (getX() < 0 || getY() < 0 || getX() > (Game.WIDTH) || getY() > (Game.HEIGHT));
    }
}
