package Arena;

import java.awt.geom.Point2D;

public class Utils {
    private final int scanningRange = 250;

    /**
     * Calculates the new location given the current location, direction and the speed
     * @param location The current location.
     * @param direction The direction in degrees.
     * @param speed The speed.
     * @return The new location.
     */
    public static Location getNewLocationBy(Location location, int direction, int speed) {
        double magnitudeX = Math.cos(Math.toRadians(direction)) * speed;
        double magnitudeY = Math.sin(Math.toRadians(direction)) * speed;

        return new Location(location.getX() + magnitudeX, location.getY() + magnitudeY);
    }

    /**
     * Checks whether the target location is within the scanned area.
     * @param scanningSource The source location from where the scan is performed.
     * @param startingAngle Starting angle. Must be smaller than finishingAngle. (0-360)
     * @param finishingAngle Finishing angle. Must be larger than startingAngle. (0-360)
     * @param scanningTarget The target location that is checked.
     * @return true if the target location is within the scanned area, false otherwise.
     */
    public static boolean isInsideTheScanningArea(Location scanningSource, int startingAngle, int finishingAngle, Location scanningTarget) {
        double y = scanningTarget.getY() - scanningSource.getY();
        double x = scanningTarget.getX() - scanningSource.getX();
        double angleBetweenPoints = Math.atan2(y, x);

        return angleBetweenPoints > startingAngle &&
                angleBetweenPoints < finishingAngle &&
                getDistanceBetween(scanningSource, scanningTarget) <= scanningRange;
    }

    /**
     * Calculates the distance between 2 locations in the arena.
     * @param location1 First location.
     * @param location2 Second location.
     * @return The distance between the locations as a double.
     */
    public static double getDistanceBetween(Location location1, Location location2) {
        return Point2D.distance(location1.getX(), location1.getY(), location2.getX(), location2.getY());
    }
}
