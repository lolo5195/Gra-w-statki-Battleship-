import java.util.*;

public class Board {
    private char[][] grid;
    private List<ShipComponent> ships;
    private List<Observer> observers;

    public Board() {
        grid = new char[10][10];
        for (int i = 0; i < 10; i++) {
            Arrays.fill(grid[i], '~'); // '~' = woda
        }
        ships = new ArrayList<>();
        observers = new ArrayList<>();
    }

    // Pobranie planszy
    public char[][] getGrid() {
        return grid;
    }

    // Pobranie zawartości konkretnego pola
    public char getCell(Coordinate c) {
        return grid[c.getY()][c.getX()]; // Y = wiersz, X = kolumna
    }

    // Ustawienie wartości pola (np. 'X', 'O', 'S')
    public void setCell(Coordinate c, char value) {
        grid[c.getY()][c.getX()] = value; // Y = wiersz, X = kolumna
    }

    // Umieszczenie statku na planszy
    public void placeShip(ShipComponent ship) {
        ships.add(ship);
        for (Coordinate c : ship.getCoordinates()) {
            grid[c.getY()][c.getX()] = 'S'; // Y = wiersz, X = kolumna
        }
    }

    // Strzał w dane współrzędne
public void shotAt(Coordinate coord) {
    boolean hit = false;

    for (ShipComponent ship : ships) {
        if (ship.containsCoordinate(coord)) {
            ship.registerHit(coord);
            hit = true;

            System.out.println(">>> Hit!");
            if (ship.isSunk()) {
                System.out.println(">>> Statek zatopiony!");
            }
            break;
        }
    }

    grid[coord.getY()][coord.getX()] = hit ? 'X' : 'O';
    if (!hit) System.out.println(">>> Miss!");

    notifyObservers();
}



    // Sprawdzenie, czy wszystkie statki zostały zatopione
    public boolean isGameOver() {
        for (ShipComponent ship : ships) {
            if (!ship.isSunk()) return false;
        }
        return true;
    }

    // Obsługa obserwatorów (np. ConsoleView)
    public void attach(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }
}
