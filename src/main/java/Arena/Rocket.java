package Arena;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @AllArgsConstructor
public class Rocket {
    private final int direction;
    private final int speed;
    private @Setter Location location;
    private Location target;
}
