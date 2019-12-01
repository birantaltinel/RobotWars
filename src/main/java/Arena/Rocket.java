package Arena;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

@Getter
public class Rocket {
    private final int direction;
    private final int speed = 100;
    private Location location;
    private Location target;
    private Robot sender;
    private JPanel element;

    public Rocket(
        int direction,
        Location location,
        Location target,
        Robot sender
    ) {
        this.direction = direction;
        this.location = location;
        this.target = target;
        this.sender = sender;
    }

    /**
     * Sets the location of the rocket on the arena.
     * @param location
     */
    final public void setLocation(Location location) {
        this.location = location;
        if(this.element != null) this.element.setLocation(location.getPoint());
    }

    /**
     * Sets the graphical component that represents the rocket on the Arena GUI.
     * @param element
     */
    final public void setElement(JPanel element) {
        this.element = element;
        if(this.location != null) this.element.setLocation(this.location.getPoint());
    }
}
