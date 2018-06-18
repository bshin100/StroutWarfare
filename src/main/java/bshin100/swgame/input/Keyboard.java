package bshin100.swgame.input;

import bshin100.swgame.Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Keyboard Listener Class
 * Created on 6/3/2018.
 * @author Brian Shin
 */
public class Keyboard implements KeyListener {

    public boolean up, down, left, right, fire, angleUp, angleDown,
            up2, down2, left2, right2, fire2, angleUp2, angleDown2, select;
    private boolean[] keys = new boolean[180]; // 128 keys

    public Keyboard() {}

    public boolean hasKeyBeenPressed(int keyCode) {
        return keys[keyCode];
    }

    public void update() {
        up = keys[KeyEvent.VK_W];
        down = keys[KeyEvent.VK_S];
        left = keys[KeyEvent.VK_A];
        right = keys[KeyEvent.VK_D];
        fire = keys[KeyEvent.VK_F];
        angleUp = keys[KeyEvent.VK_Q];
        angleDown = keys[KeyEvent.VK_E];

        up2 = keys[KeyEvent.VK_I];
        down2 = keys[KeyEvent.VK_K];
        left2 = keys[KeyEvent.VK_J];
        right2 = keys[KeyEvent.VK_L];
        fire2 = keys[KeyEvent.VK_H];
        angleUp2 = keys[KeyEvent.VK_U];
        angleDown2 = keys[KeyEvent.VK_O];
        select = keys[KeyEvent.VK_ENTER];
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        keys[keyEvent.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        keys[keyEvent.getKeyCode()] = false;
    }

}