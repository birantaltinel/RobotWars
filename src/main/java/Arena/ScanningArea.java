package Arena;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class ScanningArea {
    Location currentLocation;
    int startingAngle;
    int finishingAngle;
    int range;
}
