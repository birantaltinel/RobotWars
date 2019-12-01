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
                    Location newLocation = Utils.getNewLocationBy(robot.getLocation(), robot.getDirection(), robot.getSpeed());
                    robot.setLocation(newLocation);
                    //damage if collisions
                });
        this.rockets
                .stream()
                .forEach(rocket -> {
                    Location newLocation = Utils.getNewLocationBy(rocket.getLocation(), rocket.getDirection(), rocket.getSpeed());
                    rocket.setLocation(newLocation);
                    if( Utils.getDistanceBetween(rocket.getLocation(), rocket.getTarget()) < 1) {
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
                .filter(enemyRobotLocation -> Utils.isInsideTheScanningArea(robot.getLocation(), startingAngle, finishingAngle, enemyRobotLocation))
                .map(enemyRobotLocation -> Utils.getDistanceBetween(robot.getLocation(), enemyRobotLocation))
                .min(Comparator.comparing(Double::valueOf))
                .orElse(-1.0);
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
