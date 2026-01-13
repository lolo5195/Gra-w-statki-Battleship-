import java.util.*;

public class Board {
    private char[][] grid;
    private List<ShipComponent> ships;
    private List<Observer> observers;

    public Board() {
        grid = new char[10][10];
        for (int i = 0; i < 10; i++) {
            Arrays.fill(grid[i], '~');
        }
        ships = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public char[][] getGrid() {
        return grid;
    }

    public void placeShip(ShipComponent ship) {
        // TODO: Osoba 5 dodawanie statku do listy i gridu
        
    }

    public void shotAt(Coordinate coord) {
        // TODO: Osoba 3 (sprawdzanie czy trafiony)

    }

    public void attach(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
        observers.add(observer);
    }
    }

    public void notifyObservers() {
        for(Observer observer : observers) {
            observer.update(this);
        }
    }
}