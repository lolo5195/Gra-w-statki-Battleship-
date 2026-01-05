import java.util.ArrayList;
import java.util.List;

public class Warship implements ShipComponent {
    private List<ShipComponent> components;

    public Warship() {
        this.components = new ArrayList<>();

    }

    public void addMast(ShipComponent component) {
        this.components.add(component);
    }

    @Override
    public List<Coordinate> getCoordinates() {
        List<Coordinate> coords = new ArrayList<>();
        for (ShipComponent comp : components) {
            coords.addAll(comp.getCoordinates());
        }
        return coords;
    }

    @Override
    public boolean containsCoordinate(Coordinate c) {
        for (ShipComponent comp : components) {
            if (comp.containsCoordinate(c)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void registerHit(Coordinate c) {
        for (ShipComponent comp : components) {
            if (comp.containsCoordinate(c)) {
                comp.registerHit(c);
            }
        }
    }

    @Override
    public void hit() {
        for (ShipComponent comp : components) {
            comp.hit();
        }
    }

    @Override
    public boolean isSunk() {
        for (ShipComponent comp : components) {
            if (!comp.isSunk()) {
                return false;
            }
        }
        return true;
    }
    
}
