package Arena;

import java.util.List;

public class Arena {
    private List<Robot> robots;
    private final int width;
    private final int height;
    private ArenaGUI arenaGUI;

    public Arena(int width, int height){
        this.width = width;
        this.height = height;
        this.arenaGUI = new ArenaGUI();
    }

    public Arena() {
        this.width = 500;
        this.height = 500;
        this.arenaGUI = new ArenaGUI();
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
    }
}
