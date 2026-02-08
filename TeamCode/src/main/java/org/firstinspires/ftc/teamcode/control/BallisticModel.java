package org.firstinspires.ftc.teamcode.control;

public class BallisticModel {
    public static final double METERS_TO_INCHES = 39.37008, INCHES_TO_METERS = 0.0254;
    private static final double GRAVITY_ACC = 9.807;
    private static final double LAUNCH_ANGLE_SINE = 0.86602, LAUNCH_ANGLE_COSINE = 0.5;
    private static final double LAUNCH_FACTOR = 0.00385920; // ticks -> launch velocity
    private static final double DRAG_FACTOR = 0.05168416;
    private static final double LAUNCH_HEIGHT = 0.26, IMPACT_HEIGHT = 0.98;
    private static final double TIME_STEP = 0.002;  // in seconds (2 ms)

    private double distanceTravelled = 0;
    private double radialVelocity = 0;  // robot velocity towards target
    private int simulationFrames = 0;   // for calculating projectile flight time

    /**
     * Runs the projectile simulation, calculating the distance
     * reached by the projectile and the airtime of the projectile
     *
     * @param ticksPerSecond velocity of the flywheel
     */
    public void run(double ticksPerSecond) {
        int frames = 0;

        double accelerationX;
        double accelerationY;
        double velocity = ticksPerSecond * LAUNCH_FACTOR;
        double velocityX = velocity * LAUNCH_ANGLE_COSINE;
        double velocityY = velocity * LAUNCH_ANGLE_SINE;
        double x = 0;
        double y = LAUNCH_HEIGHT;

        while (velocityY > 0 || y >= IMPACT_HEIGHT) {
            velocity = Math.sqrt(velocityX * velocityX + velocityY * velocityY);

            accelerationX = -DRAG_FACTOR * velocity * velocityX;
            accelerationY = -GRAVITY_ACC - DRAG_FACTOR * velocity * velocityY;

            velocityX += accelerationX * TIME_STEP;
            velocityY += accelerationY * TIME_STEP;

            x += velocityX * TIME_STEP;
            y += velocityY * TIME_STEP;

            frames++;
        }

        simulationFrames = frames;
        distanceTravelled = x;
    }

    /**
     * @return distance travelled by the projectile in INCHES according
     * to the simulation, accounting for robot radial velocity
     */
    public double getDistanceTravelled() {
        return distanceTravelled * METERS_TO_INCHES + radialVelocity * getAirTime();
    }

    /**
     * @return air time of the projectile in seconds according to the simulation
     */
    public double getAirTime() {
        return TIME_STEP * simulationFrames;
    }

    /**
     * @param radialVelocity set the velocity of the robot going
     *                       towards the goal in INCHES/SECOND
     */
    public void setRadialVelocity(double radialVelocity) {
        this.radialVelocity = radialVelocity;
    }
}
