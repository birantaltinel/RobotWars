package Arena;

import java.awt.geom.Point2D;
import java.util.Random;

class Utils {
    private static final int scanningRange = 250;
    private static final int width = 500;
    private static final int height = 500;

    /**
     * Calculates the new location given the current location, direction and the speed
     * @param location The current location.
     * @param direction The direction in degrees.
     * @param speed The speed.
     * @return The new location.
     */
    static Location getNewLocationBy(Location location, double direction, int speed) {
        double magnitudeX = Math.cos(Math.toRadians(direction)) * speed;
        double magnitudeY = Math.sin(Math.toRadians(direction)) * speed;
        double newX = Math.max(Math.min(location.getX() + magnitudeX, width), 0);
        double newY = Math.max(Math.min(location.getY() + magnitudeY, height), 0);
        return new Location(newX, newY);
    }

    /**
     * Checks whether the target location is within the scanned area.
     * @param scanningSource The source location from where the scan is performed.
     * @param scanningTarget The target location that is checked.
     * @return true if the target location is within the scanned area, false otherwise.
     */
    static boolean isInsideTheScanningArea(Location scanningSource, Location scanningTarget) {
        return getDistanceBetween(scanningSource, scanningTarget) <= scanningRange;
    }

    static double angleBetweenPoints(Location source, Location target) {
        double y = target.getY() - source.getY();
        double x = target.getX() - source.getX();
        return Math.atan2(y, x);
    }

    /**
     * Calculates the distance between 2 locations in the arena.
     * @param location1 First location.
     * @param location2 Second location.
     * @return The distance between the locations as a double.
     */
    static double getDistanceBetween(Location location1, Location location2) {
        return Point2D.distance(location1.getX(), location1.getY(), location2.getX(), location2.getY());
    }

    /**
     * Returns a random location inside the arena.
     * @return a random location inside the arena.
     */
    static Location getRandomLocation() {
        Random rand = new Random();
        int x = rand.nextInt(width);
        int y = rand.nextInt(height);
        return new Location(x, y);
    }
}
