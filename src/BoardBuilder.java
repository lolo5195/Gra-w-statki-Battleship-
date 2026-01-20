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

    public BoardBuilder addPlayerShips() {
        System.out.println("Rozmieszczanie statkow gracza losowo...");
        
        placeShipRandomly(4, false);
        
        placeShipRandomly(3, false);
        placeShipRandomly(3, false);
        
        placeShipRandomly(2, false);
        placeShipRandomly(2, false);
        placeShipRandomly(2, false);
        
        placeShipRandomly(1, false);
        placeShipRandomly(1, false);
        placeShipRandomly(1, true);
        placeShipRandomly(1, true);
        
        System.out.println("Statki gracza zostaly rozmieszczone!");
        return this;
    }

    public BoardBuilder addBotShips(int ignored) {
        System.out.println("Rozmieszczanie statkow Bota...");
        
        placeShipRandomly(4, false);
        
        placeShipRandomly(3, false);
        placeShipRandomly(3, false);
        
        placeShipRandomly(2, false);
        placeShipRandomly(2, false);
        placeShipRandomly(2, false);
        
        placeShipRandomly(1, false);
        placeShipRandomly(1, false);
        placeShipRandomly(1, true);
        placeShipRandomly(1, true);

        return this;
    }

    public Board build() {
        return board;
    }

    private void placeShipRandomly(int length, boolean armored) {
        int maxAttempts = 1000;
        int attempts = 0;
        
        while (attempts < maxAttempts) {
            boolean horizontal = random.nextInt(2) == 0;
            
            int startX, startY;
            
            if (horizontal) {
                startX = random.nextInt(11 - length);
                startY = random.nextInt(10);
            } else {
                startX = random.nextInt(10);
                startY = random.nextInt(11 - length);
            }
            
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
                if (length == 1) {
                    if (armored) {
                        board.placeShip(new ArmoredMast(coords.get(0).getX(), coords.get(0).getY()));
                    } else {
                        board.placeShip(new Mast(coords.get(0).getX(), coords.get(0).getY()));
                    }
                } else {
                    Warship warship = new Warship();
                    for (Coordinate coord : coords) {
                        warship.addComponent(new Mast(coord.getX(), coord.getY()));
                    }
                    board.placeShip(warship);
                }
                return;
            }
            
            attempts++;
        }
        
        System.out.println("OSTRZEZENIE: Nie udalo sie umiescic statku o dlugosci " + length);
    }

    private boolean isValidCoordinate(int x, int y) {
        if (x < 0 || x >= 10 || y < 0 || y >= 10) return false;

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
