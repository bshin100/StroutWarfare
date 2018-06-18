package bshin100.swgame;

import bshin100.swgame.gfx.AnimatedSprite;
import bshin100.swgame.input.Keyboard;
import bshin100.swgame.util.Direction;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Tank Class
 * Created on 6/5/2018.
 * @author Brian Shin
 */
public class Tank extends AnimatedSprite {


    private int totalDistance;
    private int playerNum;
    private boolean moving;
    private boolean dying;
    private boolean dead;
    private Direction cannonDirection;
    ArrayList<Projectile> projectiles;

    public Tank(BufferedImage[] frames, int frameDelay, int startX, int startY, Keyboard keyboard, int playerNum) {
        super(frames, frameDelay, startX, startY, keyboard);
        this.playerNum = playerNum;
        moving = false;
        dying = false;
        projectiles = new ArrayList<>();
    }

    public void update() {
        //System.out.println(Game.getCollisionDir(this));
        if(!moving && !dying) stop();
        if(playerNum == 1 && !dying) {
            if (keyboard.up) { move(0, -1); moving = true; start(); setDirection(Direction.UP); }
            if (keyboard.down) { move(0, 1); moving = true; start(); setDirection(Direction.DOWN);}
            if (keyboard.left) { move(-1, 0); moving = true; start(); setDirection(Direction.LEFT);}
            if (keyboard.right) { move(1, 0); moving = true; start(); setDirection(Direction.RIGHT);}
            if (keyboard.angleUp) { setCannonDirection(Direction.RIGHT23); }
            if (keyboard.angleDown) { setCannonDirection(Direction.RIGHT); }
            //if (keyboard.angleUp && cannonDirection == Direction.RIGHT23) { setCannonDirection(Direction.RIGHT45); }

            if(keyboard.fire && !moving) { shoot(); }
            moving = false;

        } else if(playerNum == 2 && !dying){
            if (keyboard.up2) { move(0, -1); moving = true; start(); setDirection(Direction.UP);}
            if (keyboard.down2) { move(0, 1); moving = true; start(); setDirection(Direction.DOWN);}
            if (keyboard.left2) { move(-1, 0); moving = true; start(); setDirection(Direction.LEFT);}
            if (keyboard.right2) { move(1, 0); moving = true; start(); setDirection(Direction.RIGHT);}
            if (keyboard.angleUp2) { setCannonDirection(Direction.LEFT23); }
            if (keyboard.angleDown2) { setCannonDirection(Direction.LEFT); }
            //if (keyboard.angleUp2 && cannonDirection == Direction.LEFT23) { setCannonDirection(Direction.LEFT45); }

            if(keyboard.fire2 && !moving) { shoot(); }
            moving = false;

        }
        totalDistance = 0; // TESTING

        if(dying && Game.tankExplode != null) {
            if(!dead) {
                setFrames(Game.tankExplode);
                if(playerNum == 2) flipImageY();
            }
            dead = true;
            frameDelay = 4;

            frameCount++;
            if (frameCount > frameDelay) {
                frameCount = 0;
                currentFrame += animationDirection;
                if (currentFrame > totalFrames - 1) {
                    currentFrame = totalFrames - 1; // Run through once and stop at last frame.
                    setVisible(false);
                }
                else if (currentFrame < 0) { currentFrame = totalFrames - 1; }
            }
        }

        if (!stopped && !dying) {
            frameCount++;
            if (frameCount > frameDelay) {
                frameCount = 0;
                currentFrame += animationDirection;
                if (currentFrame > totalFrames - 1) { currentFrame = 0; }
                else if (currentFrame < 0) { currentFrame = totalFrames - 1; }
            }
        }

        if(projectiles.size() > 2) {
            for(int i = 1; i <= projectiles.size() - 1; i++) {
                projectiles.remove(projectiles.get(i));
            }
        }
    }

    private void shoot() {
        int muzzleX, muzzleY;

        muzzleX = getX() + getSprite().getWidth() - 10; // Default Direction.RIGHT
        muzzleY = getY() + getSprite().getHeight() / 2 - 4;
        if(cannonDirection == Direction.RIGHT23) {
            muzzleY = getY() + getSprite().getHeight() / 2 - 18;
        }
        if(cannonDirection == Direction.LEFT) {
            muzzleX = getX() - 5;// - getSprite().getWidth();
        } else if(cannonDirection == Direction.LEFT23) {
            muzzleX = getX() - 5;
            muzzleY = getY() + getSprite().getHeight() / 2 - 18;
        }

        projectiles.add(new Projectile(muzzleX, muzzleY, cannonDirection));
    }

    protected void move(int xd, int yd) {

        if (xd != 0 && yd != 0) {
            move(0, yd);
            move(xd, 0);
            return;
        }

        /*if (xd > 0) setDirection(Direction.RIGHT);
        if (xd < 0) setDirection(Direction.LEFT);
        if (yd > 0) setDirection(Direction.DOWN);
        if (yd < 0) setDirection(Direction.UP);*/

        if(!outOfBounds() && totalDistance <= 1000) {
            setX(x += xd);
            setY(y += yd);
            totalDistance += (int)(Math.sqrt(Math.pow(xd, 2) + Math.pow(yd, 2)));
        } else if((getCollDir() != getDirection()) && totalDistance <= 1000 ) {
            setX(x += xd);
            setY(y += yd);
            totalDistance += (int)(Math.sqrt(Math.pow(xd, 2) + Math.pow(yd, 2)));
        }
    }

    public void die() {
        dying = true;
        //setVisible(false);
    }

    void setCannonDirection(Direction direction) {
        this.cannonDirection = direction;
    }

    Direction getCannonDirection() {
        return this.cannonDirection;
    }
}
