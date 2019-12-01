package Arena;

import lombok.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class Robot implements Runnable {
    private int health = 100;
    private int direction = 0;
    private int speed = 0;
    private @Getter(AccessLevel.PROTECTED) Location location;
    private @Setter Arena arena;
    private @Getter JPanel element;

    private final int maxScanningAngle = 90;
    private final int maxSpeed = 10;
    private final int maxRocketDistance = 250;

    public Robot() { }
    /**
     * Specify the angles(0-359) between which the scan will be performed.
     * A maximum of 90 degrees can be scanned in 1 turn.
     * Has a range of 250 meters.
     *
     * @param startingAngle  If a value exceeding the 360 is provided, the result of (startingAngle mod 360) will be used.
     * @param finishingAngle If a value exceeding the 360 is provided, the result of (finishingAngle mod 360) will be used.
     * @return Returns the distance to the closest enemy in the scanned region. If no enemy is present, returns -1.0;
     */
    final public double scan(int startingAngle, int finishingAngle) {
        if(Math.abs(startingAngle - finishingAngle) > maxScanningAngle)
            return arena.getDistanceToClosestRobotFrom(this, startingAngle, startingAngle + maxScanningAngle);
        if(finishingAngle < startingAngle)
            return arena.getDistanceToClosestRobotFrom(this, finishingAngle, startingAngle);
        return arena.getDistanceToClosestRobotFrom(this, startingAngle, finishingAngle);
    }

    /**
     * Starts moving the robot in the given direction(0-359) with the given speed(0-10).
     *
     * @param direction If a value exceeding the maximum angle is provided, the result of (direction mod 360) will be used.
     * @param speed     If a value exceeding the maximum speed of 10 is provided, the speed will be adjusted as 10.
     */
    final public void move(int direction, int speed) {
        this.direction = direction % 360;
        this.speed = speed % maxSpeed;
    }

    /**
     * Fires a missile towards the given direction and distance. If the cannon has not finished reloading yet, this function does nothing.
     *
     * @param direction If a value exceeding the maximum angle is provided, the result of (direction mod 360) will be used.
     * @param distance  The maximum distance is 250 meters. If the given value exceeds the maximum, the distance will be adjusted as 250.
     */
    final public void fire(int direction, double distance) {
        if(!isCannonReloaded())
            return;

        int correctedDirection = direction % 360;
        double correctedDistance = distance % maxRocketDistance;
        Location target = calculateTargetLocation(correctedDirection, correctedDistance);
        Rocket rocket = new Rocket(correctedDirection, location, target, this);

        arena.sendRocket(rocket);
    }

    private Location calculateTargetLocation(int directionInDegrees, double distance) {
        double directionInRadians = Math.toRadians(directionInDegrees);
        double targetX = Math.cos(directionInRadians) * distance + location.getX();
        double targetY = Math.sin(directionInRadians) * distance + location.getY();
        return new Location(targetX, targetY);
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
    final public int getDirection() {
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
