import java.util.Random;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class BoardBuilder {

    private Board board;
    private Scanner scanner;
    private Random random;

    public BoardBuilder() {
        this.board = new Board();
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }

    // --- Dodanie statkow gracza losowo ---
    public BoardBuilder addPlayerShips() {
        System.out.println("Rozmieszczanie statkow gracza losowo...");
        
        // 1 czteromasztowiec (4 pola)
        placeShipRandomly(4, false);
        
        // 2 trojmasztowce (3 pola)
        placeShipRandomly(3, false);
        placeShipRandomly(3, false);
        
        // 3 dwumasztowce (2 pola)
        placeShipRandomly(2, false);
        placeShipRandomly(2, false);
        placeShipRandomly(2, false);
        
        // 4 jednomasztowce (1 pole) - 2 zwykle, 2 opancerzone
        placeShipRandomly(1, false);  // zwykly
        placeShipRandomly(1, false);  // zwykly
        placeShipRandomly(1, true);   // opancerzony
        placeShipRandomly(1, true);   // opancerzony
        
        System.out.println("Statki gracza zostaly rozmieszczone!");
        return this;
    }

    // --- Dodanie statkow Bota losowo ---
    public BoardBuilder addBotShips(int ignored) {
        System.out.println("Rozmieszczanie statkow Bota...");
        
        // 1 czteromasztowiec (4 pola)
        placeShipRandomly(4, false);
        
        // 2 trojmasztowce (3 pola)
        placeShipRandomly(3, false);
        placeShipRandomly(3, false);
        
        // 3 dwumasztowce (2 pola)
        placeShipRandomly(2, false);
        placeShipRandomly(2, false);
        placeShipRandomly(2, false);
        
        // 4 jednomasztowce (1 pole) - 2 zwykle, 2 opancerzone
        placeShipRandomly(1, false);  // zwykly
        placeShipRandomly(1, false);  // zwykly
        placeShipRandomly(1, true);   // opancerzony
        placeShipRandomly(1, true);   // opancerzony

        return this;
    }

    // --- Budowa planszy ---
    public Board build() {
        return board;
    }

    // --- Umieszczenie statku o okreslonej dlugosci losowo ---
    private void placeShipRandomly(int length, boolean armored) {
        int maxAttempts = 1000;
        int attempts = 0;
        
        while (attempts < maxAttempts) {
            // Losuj orientacje: 0 = poziomo, 1 = pionowo
            boolean horizontal = random.nextInt(2) == 0;
            
            int startX, startY;
            
            if (horizontal) {
                // Statek poziomy
                startX = random.nextInt(11 - length); // 0 do (10-length)
                startY = random.nextInt(10);
            } else {
                // Statek pionowy
                startX = random.nextInt(10);
                startY = random.nextInt(11 - length); // 0 do (10-length)
            }
            
            // Sprawdz czy mozna postawic statek
            List<Coordinate> coords = new ArrayList<>();
            boolean canPlace = true;
            
            for (int i = 0; i < length; i++) {
                int x = horizontal ? startX + i : startX;
                int y = horizontal ? startY : startY + i;
                
                if (!isValidCoordinate(x, y)) {
                    canPlace = false;
                    break;
                }
                coords.add(new Coordinate(x, y));
            }
            
            if (canPlace) {
                // Utworz statek
                if (length == 1) {
                    // Jednomasztowiec - zwykly lub opancerzony
                    if (armored) {
                        board.placeShip(new ArmoredMast(coords.get(0).getX(), coords.get(0).getY()));
                    } else {
                        board.placeShip(new Mast(coords.get(0).getX(), coords.get(0).getY()));
                    }
                } else {
                    // Wielomasztowiec (Warship)
                    Warship warship = new Warship();
                    for (Coordinate coord : coords) {
                        warship.addComponent(new Mast(coord.getX(), coord.getY()));
                    }
                    board.placeShip(warship);
                }
                return; // Statek umieszczony
            }
            
            attempts++;
        }
        
        System.out.println("OSTRZEZENIE: Nie udalo sie umiescic statku o dlugosci " + length);
    }

    // --- Walidacja pojedynczej wspolrzednej ---
    private boolean isValidCoordinate(int x, int y) {
        if (x < 0 || x >= 10 || y < 0 || y >= 10) return false;

        // Sprawdzenie 8 sasiednich pol + wlasne
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < 10 && ny >= 0 && ny < 10) {
                    if (board.getCell(new Coordinate(nx, ny)) == 'S') return false;
                }
            }
        }
        return true;
    }
}
