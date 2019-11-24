package Arena;

import java.util.ArrayList;
import java.util.List;
import SampleRobots.Rabbit;
import lombok.Getter;

public class Arena {
    private List<Robot> robots;
    private @Getter final int width = 500;
    private @Getter final int height = 500;
    private ArenaGUI arenaGUI;

    public Arena() {
        this.arenaGUI = new ArenaGUI();
        this.robots = new ArrayList<Robot>();
    }

    public Robot addRobot(Robot robot) {
//        Arena.Robot robot = robotBuilder
//                .arena(this)
//                .location(getRandomLocation())
//                .direction(0)
//                .health(100)
//                .speed(0)
//                .build();
        this.robots.add(robot);
        return robot;
    }

//    private Arena.Location getRandomLocation() {
//        Random rand = new Random();
//        int x = rand.nextInt(width);
//        int y = rand.nextInt(height);
//        return new Arena.Location(x, y);
//    }

    public List<Robot> getRobotsInScanningArea(ScanningArea area) {
        return this.robots;
    }

    public static void main(String[] args) {
        Arena arena = new Arena();
        Robot robot = Rabbit.builder()
            .direction(0)
            .health(100)
            .location(new Location(50, 50))
            .speed(0)
            .arena(arena)
            .rockets(new ArrayList<Rocket>())
            .build();
        arena.addRobot(robot);

        System.out.println(robot.getHealth());
        System.out.println(robot.getXCoordinate());
        System.out.println(robot.getYCoordinate());
        System.out.println(robot.getDirection());
        System.out.println(robot.getSpeed());
        System.out.println(robot.isCannonReloaded());
    }
}
