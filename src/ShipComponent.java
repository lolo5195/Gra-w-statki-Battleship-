import java.util.List;

public interface ShipComponent {
    
    public void hit();
    public boolean isSunk();

    List<Coordinate> getCoordinates();
    boolean containsCoordinate(Coordinate c);
    void registerHit(Coordinate c);
}
