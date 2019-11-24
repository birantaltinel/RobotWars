package Arena;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.Point;

@Getter @Setter @AllArgsConstructor
public class Location {
    private double x;
    private double y;

    /**
     * Gets java.awt.Point representation of the location
     * @return
     */
    public Point getPoint() {
        return new Point((int) x, (int) y);
    }
}
