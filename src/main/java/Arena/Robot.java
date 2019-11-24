package Arena;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Builder @AllArgsConstructor
public class Robot {
    private @Builder.Default int health = 100;
    private @Builder.Default int direction = 0;
    private @Builder.Default int speed = 0;
    private Location location;
    private Arena arena;
    private List<Rocket> rockets;
    private final int scanningRange = 500;
    private final int maxScanningAngle = 90;
    private final int maxSpeed = 10;
    private final int maxRocketDistance = 700;

    public Robot() {
        location = new Location((int) Math.random() * 500, (int) Math.random() * 500);
        rockets = new ArrayList<Rocket>();
    }
    /**
     * Specify the angles(0-359) between which the scan will be performed.
     * A maximum of 90 degrees can be scanned in 1 turn.
     * Has a range of 500 meters.
     *
     * @param startingAngle  If a value exceeding the maximum angle is provided, the result of (startingAngle mod 360) will be used.
     * @param finishingAngle If a value exceeding the maximum angle is provided, the result of (finishingAngle mod 360) will be used.
     * @return Returns the distance to the closest enemy in the scanned region. If no enemy is present, returns -1.0;
     */
    final public double scan(int startingAngle, int finishingAngle) {
        List<Robot> foundRobots = arena.getRobotsInScanningArea(
                new ScanningArea(location, startingAngle, finishingAngle, scanningRange)
        );
        return foundRobots
                .stream()
                .map(robot -> new Location(robot.getXCoordinate(), robot.getYCoordinate()))
                .map(enemyRobotLocation -> Point2D.distance(location.getX(), location.getY(), enemyRobotLocation.getX(), enemyRobotLocation.getY()))
                .min(Comparator.comparing(Double::valueOf))
                .orElse(-1.0);
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
     * @param distance  The maximum distance is 700 meters. If the given value exceeds the maximum, the distance will be adjusted as 700.
     */
    final public Rocket fire(int direction, double distance) {
        int correctedDirection = direction % 360;
        double correctedDistance = distance % maxRocketDistance;
        Location target = calculateTargetLocation(correctedDirection, correctedDistance);
        int speed = 100;
        return new Rocket(correctedDirection, speed, location, target);
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
        return rockets.size() < 2;
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
}
