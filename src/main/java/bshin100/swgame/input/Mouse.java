package bshin100.swgame.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Mouse Listener Class
 * Created on 6/5/2018.
 * @author Brian Shin
 */
public class Mouse extends MouseAdapter {

    public boolean clicked;
    public int mousePosX, mousePosY;

    public Mouse() {}

    public void update() {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        clicked = true;
        mousePosX = e.getX();
        mousePosY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        clicked = false;
        mousePosX = 0;
        mousePosY = 0;
    }
}
