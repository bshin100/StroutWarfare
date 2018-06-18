package bshin100.swgame.util;

/**
 * Created on 6/13/2018.
 *
 * @author Brian Shin
 */
public class Vector2 {

    public double x = 0;
    public double y = 0;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2() {
        this(0, 0);
    }

    public static Vector2 add(Vector2 v1, Vector2 v2) {
        double vx = v1.x + v2.x;
        double vy = v1.y + v2.y;

        return new Vector2(vx, vy);
    }

    public String toString() {
        return "X: " + x + " Y: " + y;
    }
}
