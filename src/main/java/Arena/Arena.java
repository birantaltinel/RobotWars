package Arena;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import Arena.Exceptions.RobotNotLoadedException;
import lombok.Getter;

import javax.swing.*;

public class Arena {
    private @Getter List<Robot> robots;
    private List<Rocket> rockets;
    private @Getter final int width = 500;
    private @Getter final int height = 500;
    private final int scanningRange = 250;
    private final int rocketExplosionRadius = 25;
    private final int rocketExplosionDamage = 10;
    private ArenaGUI arenaGUI;
    private RobotLoader robotLoader;

    public Arena() {
        this.arenaGUI = new ArenaGUI();
        this.robots = new ArrayList<Robot>();
        this.rockets = new ArrayList<Rocket>();
        this.robotLoader = new RobotLoader();
    }

    public void update() {
        this.robots
                .stream()
                .forEach(robot -> {
                    Location newLocation = getNewLocationBy(robot.getLocation(), robot.getDirection(), robot.getSpeed());
                    robot.setLocation(newLocation);
                    //damage if collisions
                });
        this.rockets
                .stream()
                .forEach(rocket -> {
                    Location newLocation = getNewLocationBy(rocket.getLocation(), rocket.getDirection(), rocket.getSpeed());
                    rocket.setLocation(newLocation);
                    if( getDistanceBetween(rocket.getLocation(), rocket.getTarget()) < 1) {
                        explodeRocket(rocket);
                    }
                });
    }

    /**
     * Loads and initializes a robot from the given path to the robot description file.
     * @param robotFilePath Path to the robot file written in the custom robot language.
     * @return The new robot object.
     * @throws RobotNotLoadedException
     */
    public Robot addRobot(String robotFilePath) throws RobotNotLoadedException {
        Robot robot = robotLoader.load(robotFilePath);

        JPanel element = new JPanel();
        this.arenaGUI.addElement(element);
        robot.setElement(element);
        robot.setLocation(getRandomLocation());
        robot.setArena(this);

        this.robots.add(robot);
        return robot;
    }

    public void runRobotsForOneTurn() {
        this.robots
                .stream()
                .forEach(Robot::run);
    }

    public int rocketsInTheAirFor(Robot robot) {
        return (int) this.rockets
                .stream()
                .filter(rocket -> rocket.getSender() == robot)
                .count();
    }

    /**
     * Accepts the sent rocket from the robot
     * @param rocket
     */
    public void sendRocket(Rocket rocket) {
        JPanel element = new JPanel();
        this.arenaGUI.addElement(element);
        rocket.setElement(element);

        this.rockets.add(rocket);
    }

    /**
     * Explodes the given rocket and handles all corresponding events.
     * @param rocket
     */
    public void explodeRocket(Rocket rocket) {
        this.arenaGUI.animateExplosionOf(rocket.getElement());
        this.robots
                .stream()
                .filter(robot -> Point2D.distance(rocket.getLocation().getX(), rocket.getLocation().getY(), robot.getXCoordinate(), robot.getYCoordinate()) <= rocketExplosionRadius)
                .forEach(robot -> {
                    robot.decreaseHealthBy(rocketExplosionDamage);
                    if(robot.getHealth() <= 0) {
                        killRobot(robot);
                    }
                });
        this.rockets.remove(rocket);
    }

    /**
     * Kills the robot and removes it from the arena.
     * @param robot
     */
    public void killRobot(Robot robot) {
        this.arenaGUI.removeElement(robot.getElement());
    }

    /**
     * Returns a random location inside the arena.
     * @return a random location inside the arena.
     */
    private Location getRandomLocation() {
        Random rand = new Random();
        int x = rand.nextInt(width);
        int y = rand.nextInt(height);
        return new Location(x, y);
    }

    /**
     * Finds the distance to the smaller robot in the arena from the given robot.
     * @param robot The Robot object for which the search will be performed.
     * @param startingAngle Starting angle. Must be smaller than finishingAngle. (0-360)
     * @param finishingAngle Finishing angle. Must be larger than startingAngle. (0-360)
     * @return The distance to the closest robot. If no robot is present in the scanned area, returns -1.0.
     */
    public double getDistanceToClosestRobotFrom(Robot robot, int startingAngle, int finishingAngle) {
        return this.robots
                .stream()
                .map(Robot::getLocation)
                .filter(enemyRobotLocation -> isInsideTheScanningArea(robot.getLocation(), startingAngle, finishingAngle, enemyRobotLocation))
                .map(enemyRobotLocation -> getDistanceBetween(robot.getLocation(), enemyRobotLocation))
                .min(Comparator.comparing(Double::valueOf))
                .orElse(-1.0);
    }

    /**
     * Checks whether the target location is within the scanned area.
     * @param scanningSource The source location from where the scan is performed.
     * @param startingAngle Starting angle. Must be smaller than finishingAngle. (0-360)
     * @param finishingAngle Finishing angle. Must be larger than startingAngle. (0-360)
     * @param scanningTarget The target location that is checked.
     * @return true if the target location is within the scanned area, false otherwise.
     */
    private boolean isInsideTheScanningArea(Location scanningSource, int startingAngle, int finishingAngle, Location scanningTarget) {
        double y = scanningTarget.getY() - scanningSource.getY();
        double x = scanningTarget.getX() - scanningSource.getX();
        double angleBetweenPoints = Math.atan2(y, x);

        return angleBetweenPoints > startingAngle &&
                angleBetweenPoints < finishingAngle &&
                getDistanceBetween(scanningSource, scanningTarget) <= scanningRange;
    }

    private double getDistanceBetween(Location location1, Location location2) {
        return Point2D.distance(location1.getX(), location1.getY(), location2.getX(), location2.getY());
    }

    private Location getNewLocationBy(Location location, int direction, int speed) {
        double magnitudeX = Math.cos(Math.toRadians(direction)) * speed;
        double magnitudeY = Math.sin(Math.toRadians(direction)) * speed;

        return new Location(location.getX() + magnitudeX, location.getY() + magnitudeY);
    }

    public static void main(String[] args) throws RobotNotLoadedException, InterruptedException {
        Arena arena = new Arena();

        for(String robotFilePath: args)
            arena.addRobot(robotFilePath);

        while(true){
            arena.runRobotsForOneTurn();
            arena.update();
            Thread.sleep(1000/60);
        }
    }
}
