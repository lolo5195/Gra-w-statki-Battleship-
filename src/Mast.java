import java.util.*;

public class Mast implements ShipComponent {
    private boolean isHit;
    private Coordinate coordinate;

    public Mast(int x, int y) {
        this.coordinate = new Coordinate(x, y);
        this.isHit = false;
    }

    @Override
    public void hit() {
        this.isHit = true;
    }
    
    @Override
    public boolean isSunk() {
        return isHit;
    }

    @Override
    public List<Coordinate> getCoordinates() {
        return List.of(this.coordinate);
    }

    @Override
    public boolean containsCoordinate(Coordinate c) {
        return this.coordinate.equals(c);
    }

    @Override
    public void registerHit(Coordinate c) {
        if (containsCoordinate(c)) {
            hit();
        }
    }

    @Override
    public void repair() {
        this.isHit = false;
    }
}