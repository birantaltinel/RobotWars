package Arena;

import lombok.*;
import javax.swing.*;

@AllArgsConstructor
public class Robot implements Runnable {
    private int health = 100;
    private double direction = 0;
    private int speed = 0;
    private @Getter(AccessLevel.PROTECTED) Location location;
    private @Setter Arena arena;
    private @Getter JPanel element;
    private @Getter @Setter JTextArea info;

    //private final int maxScanningAngle = 90;
    private final int maxSpeed = 2;
    //private final int maxRocketDistance = 250;

    public Robot() { }
    /**
     * Performs a scan within 300 units range of the robot and returns the location of the closest enemy.
     * @return Returns the distance to the closest enemy in the scanned region. If no enemy is present, returns null;
     */
    final public double[] scan() {
        Robot closestRobot = arena.getClosestRobotFrom(this);
        if(closestRobot == null)
            return null;
        return new double[]{closestRobot.getXCoordinate(), closestRobot.getYCoordinate()};
    }

    /**
     * Starts moving the robot in the given direction(0-359) with the given speed(0-10).
     *
     * @param direction If a value exceeding the maximum angle is provided, the result of (direction mod 360) will be used.
     * @param speed     If a value exceeding the maximum speed of 10 is provided, the speed will be adjusted as 10.
     */
    final public void move(double direction, int speed) {
        this.direction = direction % 360;
        this.speed = speed % maxSpeed;
    }

    /**
     * Fires a missile towards the given location. The missile is not fired if the location is beyond the range or if the cannon is not reloaded yet.
     * @param targetX the x coordinate of the target.
     * @param targetY the y coordinate of the target.
     */
    final public void fire(double targetX, double targetY) {
        if(!isCannonReloaded())
            return;

        double direction = Utils.angleBetweenPoints(location, new Location(targetX, targetY));
        Location target = new Location(targetX, targetY);
        Rocket rocket = new Rocket(direction, location, target, this);

        arena.sendRocket(rocket);
    }

    /**
     * @return True if the cannon has finished reloading and can fire another missile. False otherwise.
     */
    final public boolean isCannonReloaded() {
        return arena.rocketsInTheAirFor(this) < 2;
    }

    /**
     * @return The current health of the robot. (0-100)
     */
    final public int getHealth() {
        return health;
    }

    /**
     * @return The current speed of the robot. (0-10)
     */
    final public int getSpeed() {
        return speed;
    }

    /**
     * @return The current direction of the robot in degrees. (0-359)
     */
    final public double getDirection() {
        return direction;
    }

    /**
     * @return the x coordinate of the current location.
     */
    final public double getXCoordinate() {
        return location.getX();
    }

    /**
     * @return the y coordinate of the current location.
     */
    final public double getYCoordinate() {
        return location.getY();
    }

    /**
     * Sets the location of the robot on the arena.
     * @param location
     */
    final public void setLocation(Location location) {
        this.location = location;
        if(this.element != null) this.element.setLocation(location.getPoint());
    }

    /**
     * Sets the graphical component that represents the robot on the Arena GUI.
     * @param element
     */
    final public void setElement(JPanel element) {
        this.element = element;
        if(this.location != null) this.element.setLocation(this.location.getPoint());
    }

    final public void decreaseHealthBy(int damage) {
        this.health -= damage;
    }

    public void run() {
    }
}
