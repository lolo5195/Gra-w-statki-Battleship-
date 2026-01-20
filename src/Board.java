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
        observers = new ArrayList<>();
    }

    public char[][] getGrid() {
        return grid;
    }

    public char getCell(Coordinate c) {
        return grid[c.getY()][c.getX()];
    }

    public void setCell(Coordinate c, char value) {
        grid[c.getY()][c.getX()] = value;
    }

    public void placeShip(ShipComponent ship) {
        ships.add(ship);
        for (Coordinate c : ship.getCoordinates()) {
            grid[c.getY()][c.getX()] = 'S';
        }
    }

    public void shotAt(Coordinate coord) {
        char currentCell = grid[coord.getY()][coord.getX()];
        if (currentCell == 'X' || currentCell == 'O') {
            System.out.println("[BLAD] To pole bylo juz strzelane! Wybierz inne wspolrzedne.");
            return;
        }
        
        boolean hit = false;
        FireResult result = FireResult.MISS;
        ShipComponent hitShip = null;

        for (ShipComponent ship : ships) {
            if (ship.containsCoordinate(coord)) {
                ship.registerHit(coord);
                hit = true;
                hitShip = ship;

                ShipComponent component = getComponentAt(ship, coord);
                boolean componentSunk = (component != null && component.isSunk());

                if (componentSunk) {
                    System.out.println("[TRAFIENIE] Trafiono w statek!");
                    grid[coord.getY()][coord.getX()] = 'X';
                } else {
                    System.out.println("[TRAFIENIE] Trafiono opancerzony maszt! (wymaga jeszcze 1 trafienia)");
                }
                
                if (ship.isSunk()) {
                    int shipSize = ship.getCoordinates().size();
                    String shipType = getShipTypeName(shipSize);
                    System.out.println("+-------------------------------+");
                    System.out.println("|      STATEK ZATOPIONY!        |");
                    System.out.println("|   " + shipType + " zatonal!" + " ".repeat(Math.max(0, 17 - shipType.length())) + "|");
                    System.out.println("+-------------------------------+");
                }
                result = FireResult.HIT;
                break;
            }
        }

        if (!hit) {
            grid[coord.getY()][coord.getX()] = 'O';
            System.out.println("[PUDLO] Nie trafiono w zaden statek.");
            result = FireResult.MISS;
        }

        notifyObservers(result);
    }
    
    private ShipComponent getComponentAt(ShipComponent ship, Coordinate coord) {
        if (ship instanceof Warship warship) {
            ShipComponent component = warship.getComponentAt(coord);
            if (component != null) {
                return component;
            }
        }
        return ship;
    }

    public ShipComponent getShipAt(Coordinate coord) {
        for (ShipComponent ship : ships) {
            if (ship.containsCoordinate(coord)) {
                return ship;
            }
        }
        return null;
    }

    public boolean isGameOver() {
        for (ShipComponent ship : ships) {
            if (!ship.isSunk()) return false;
        }
        return true;
    }
    
    private String getShipTypeName(int size) {
        return switch (size) {
            case 1 -> "Jednomasztowiec";
            case 2 -> "Dwumasztowiec";
            case 3 -> "Trojmasztowiec";
            case 4 -> "Czteromasztowiec";
            default -> "Statek (" + size + " pol)";
        };
    }

    public void attach(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void notifyObservers(FireResult result) {
        for (Observer observer : observers) {
            observer.update(this, result);
        }
    }
    
    public void notifyObserversUndo(FireResult originalResult) {
        for (Observer observer : observers) {
            observer.onUndo(this, originalResult);
        }
    }
}
