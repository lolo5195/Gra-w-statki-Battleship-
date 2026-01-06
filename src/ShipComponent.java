import java.util.List;

public interface ShipComponent {
    
    void hit();
    boolean isSunk();

    List<Coordinate> getCoordinates();
    boolean containsCoordinate(Coordinate c);
    void registerHit(Coordinate c);
}
