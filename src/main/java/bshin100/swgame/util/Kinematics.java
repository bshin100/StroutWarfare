package bshin100.swgame.util;

/**
 * Kinematics
 * Created on 6/13/2018.
 */
public class Kinematics {
    public static Vector2 getVelocity(Vector2 velocity, Vector2 acceleration, double time) {
        double vx = velocity.x + (acceleration.x * time);
        double vy = velocity.y + (acceleration.y * time);

        return new Vector2(vx, vy);
    }

    public static Vector2 getDisplacement(Vector2 initialVelocity, Vector2 acceleration, double time) {
        double dx = (initialVelocity.x * time) + (0.5*(acceleration.x * Math.pow(time, 2)));
        double dy = (initialVelocity.y * time) + (0.5*(acceleration.y * Math.pow(time, 2)));

        return new Vector2(dx, dy);
    }
}
