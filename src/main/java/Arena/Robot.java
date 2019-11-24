package Arena;

import lombok.Builder;
import java.util.List;


public abstract class Robot {
    private int health;
    private int direction;
    private int speed;
    private Location location;
    private Arena arena;
    private List<Rocket> rockets;

    public Robot() {}
    /**
     * Specify the angles(0-359) between which the scan will be performed.
     * A maximum of 90 degrees can be scanned in 1 turn.
     * Has a maximum range of 500 meters.
     *
     * @param startingAngle  If a value exceeding the maximum angle is provided, the result of (startingAngle mod 360) will be used.
     * @param finishingAngle If a value exceeding the maximum angle is provided, the result of (finishingAngle mod 360) will be used.
     * @return Returns the distance to the closest enemy in the scanned region.
     */
    final public int scan(int startingAngle, int finishingAngle) {
        return 0;
    }

    /**
     * Starts moving the robot in the given direction(0-359) with the given speed(0-10).
     *
     * @param direction If a value exceeding the maximum angle is provided, the result of (direction mod 360) will be used.
     * @param speed     If a value exceeding the maximum speed of 10 is provided, the speed will be adjusted as 10.
     */
    final public void move(int direction, int speed) {
        this.direction = direction;
        this.speed = speed;
    }

    /**
     * Fires a missile towards the given direction and distance. If the cannon has not finished reloading yet, this function does nothing.
     *
     * @param direction If a value exceeding the maximum angle is provided, the result of (direction mod 360) will be used.
     * @param distance  The maximum distance is 700 meters. If the given value exceeds the maximum, the distance will be adjusted as 700.
     */
    final public Rocket fire(int direction, int distance) {
        Location target = calculateTargetLocation(direction, distance);
        int speed = 100;
        return new Rocket(direction, speed, location, target);
    }

    private Location calculateTargetLocation(int directionInDegrees, int distance) {
        double directionInRadians = Math.toRadians(directionInDegrees);
        double targetX = Math.cos(directionInRadians) * distance + location.getX();
        double targetY = Math.sin(directionInRadians) * distance + location.getY();
        return new Location(targetX, targetY); // address the precision problem here with casting
    }

    /**
     * @return True if the cannon has finished reloading and can fire another missile. False otherwise.
     */
//    final public boolean isCannonReloaded() {
//
//    }

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
}
