package Arena;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    private Arena() {
        this.arenaGUI = new ArenaGUI();
        this.robots = new ArrayList<Robot>();
        this.rockets = new ArrayList<Rocket>();
        this.robotLoader = new RobotLoader();
    }

    /**
     * Updates the arena to reflect the results of the turn that all robots have played.
     */
    private void update() {
        for(Robot robot: this.robots) {
            Location newLocation = Utils.getNewLocationBy(robot.getLocation(), robot.getDirection(), robot.getSpeed());
            robot.setLocation(newLocation);
        }
        for(Rocket rocket : this.rockets ) {
            Location newLocation = Utils.getNewLocationBy(rocket.getLocation(), rocket.getDirection(), rocket.getSpeed());
            rocket.setLocation(newLocation);
            if(Utils.getDistanceBetween(rocket.getLocation(), rocket.getTarget()) < 1) {
                explodeRocket(rocket);
            }
            for(Robot robot: this.robots) {
                if(Utils.getDistanceBetween(rocket.getLocation(), robot.getLocation()) < 1) {
                    explodeRocket(rocket);
                }
            }
        }
    }

    /**
     *
     * @return true if there is a winner of the battle, false otherwise.
     */
    private boolean thereIsAWinner() {
        return this.robots.size() == 1;
    }

    /**
     * Executes the winning sequence after a winner is declared.
     */
    private void winningSequence() {
        Robot winner = this.robots.get(0);
        this.arenaGUI.declareWinner(winner.getClass().getName());
    }

    /**
     * Executes the draw sequence after the match is declared as a draw.
     */
    private void drawSequence() {
        this.arenaGUI.declareDraw();
    }

    /**
     * Loads and initializes a robot from the given file system path to the robot description file.
     * @param robotFilePath Path to the robot file written in the custom robot language.
     * @throws RobotNotLoadedException If the dynamic class loader fails to load the robot file, this exception will be thrown.
     */
    private void addRobot(String robotFilePath) throws RobotNotLoadedException {
        Robot robot = robotLoader.load(robotFilePath);
        robot.setArena(this);

        JPanel element = new JPanel();
        robot.setLocation(Utils.getRandomLocation());
        this.arenaGUI.addRobotElement(element);
        robot.setElement(element);

        this.robots.add(robot);
    }

    /**
     * Calls all the custom robot codes that will run for one turn. This function should be called once every turn.
     */
    private void runRobotsForOneTurn() {
        this.robots
                .forEach(Robot::run);
    }

    /**
     * Returns the number of rockets in the air for the given robot.
     * @param robot The robot for which the number of rockets will be returned.
     * @return The number of rockets in the air.
     */
    int rocketsInTheAirFor(Robot robot) {
        return (int) this.rockets
                .stream()
                .filter(rocket -> rocket.getSender() == robot)
                .count();
    }

    /**
     * Accepts the sent rocket from the robot
     * @param rocket The rocket that is sent.
     */
    void sendRocket(Rocket rocket) {
        JPanel element = new JPanel();
        this.arenaGUI.addRocketElement(element);
        rocket.setElement(element);

        this.rockets.add(rocket);
    }

    /**
     * Explodes the given rocket and handles all corresponding events.
     * @param rocket The rocket to be exploded.
     */
    private void explodeRocket(Rocket rocket) {
        this.arenaGUI.animateExplosionOf(rocket.getElement());
        for(Robot robot: this.robots) {
            if(Utils.getDistanceBetween(rocket.getLocation(), robot.getLocation()) <= rocketExplosionRadius) {
                robot.decreaseHealthBy(rocketExplosionDamage);
                if(robot.getHealth() <= 0) {
                    killRobot(robot);
                }
            }
        }
        this.rockets.remove(rocket);
    }

    /**
     * Kills the robot and removes it from the arena.
     * @param robot The robot to be killed.
     */
    private void killRobot(Robot robot) {
        this.arenaGUI.removeElement(robot.getElement());
        this.robots.remove(robot);
    }

    /**
     * Finds the distance to the closest robot in the arena from the given robot within the range.
     * @param robot The Robot object for which the search will be performed.
     * @return The distance to the closest robot. If no robot is present in the scanned area, returns -1.0.
     */
    Robot getClosestRobotFrom(Robot robot) {
         List<Robot> robotsInRange = this.robots
                .stream()
                .filter(enemyRobot -> Utils.isInsideTheScanningArea(robot.getLocation(), enemyRobot.getLocation()))
                .collect(Collectors.toList());
         if(robotsInRange.isEmpty())
             return null;

         Robot closestRobot = robotsInRange.get(0);
         double closestDistance = Utils.getDistanceBetween(robot.getLocation(), closestRobot.getLocation());
         for(Robot robotInRange : robotsInRange) {
             double distance = Utils.getDistanceBetween(robot.getLocation(), robotInRange.getLocation());
             if(distance < closestDistance) {
                 closestRobot = robotInRange;
                 closestDistance = distance;
             }
         }
         return closestRobot;
    }

    public static void main(String[] args) throws RobotNotLoadedException, InterruptedException {
        Arena arena = new Arena();

        for(String robotFilePath: args)
            arena.addRobot(robotFilePath);

        for(int turn=0; turn < 5000; turn++){
            arena.runRobotsForOneTurn();
            arena.update();
            if(arena.thereIsAWinner()) {
                arena.winningSequence();
                return;
            }
            Thread.sleep(1000/60);
        }

        arena.drawSequence();
    }
}
