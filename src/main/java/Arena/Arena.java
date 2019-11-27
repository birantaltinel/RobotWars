package Arena;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import lombok.Getter;

import javax.swing.*;

public class Arena {
    private List<Robot> robots;
    private @Getter final int width = 500;
    private @Getter final int height = 500;
    private final int scanningRange = 250;
    private ArenaGUI arenaGUI;

    public Arena() {
        this.arenaGUI = new ArenaGUI();
        this.robots = new ArrayList<Robot>();
    }

    public Robot addRobot() {
        JPanel element = new JPanel();
        this.arenaGUI.addElement(element);
        Robot robot = Robot.builder()
                .direction(0)
                .health(100)
                .location(getRandomLocation())
                .speed(0)
                .arena(this)
                .rockets(new ArrayList<>())
                .element(element)
                .build();
        this.robots.add(robot);
        return robot;
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
                .map(enemyRobotLocation -> Point2D.distance(robot.getXCoordinate(), robot.getYCoordinate(), enemyRobotLocation.getX(), enemyRobotLocation.getY()))
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
        double distanceBetweenPoints = Point2D.distance(scanningSource.getX(), scanningSource.getY(), scanningTarget.getX(), scanningTarget.getY());
        return angleBetweenPoints > startingAngle && angleBetweenPoints < finishingAngle && distanceBetweenPoints <= scanningRange;
    }

    public static void main(String[] args) throws InterruptedException {
        Arena arena = new Arena();
//        while(true){
//            Random rand = new Random();
//            arena.element.setLocation( arena.element.getX() + rand.nextInt(3) - 1, arena.element.getY() + rand.nextInt(3) - 1);
//            Thread.sleep(1000/60);
//        }
        Robot robot = arena.addRobot();
    }
}
